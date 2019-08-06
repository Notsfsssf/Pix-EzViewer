package com.perol.asdpl.pixivez.dialog


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.CommentAdapter
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ReFreshFunction
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.IllustCommentsResponse
import com.perol.asdpl.pixivez.services.AppApiPixivService

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.HttpException

class CommentDialog : DialogFragment() {

    lateinit var recyclerviewPicture: RecyclerView


    lateinit var edittextComment: EditText

    lateinit var button: Button

    private val textviewuserhead: ImageView? = null
    private val textviewguanzhuzhe: TextView? = null
    private val textviewhaoPyou: TextView? = null
    private val textviewuserbody: TextView? = null
    private val textViewhold: TextView? = null
    private var Authorization: String? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var commentAdapter: CommentAdapter? = null
    private var id: Long? = null
    private var Parent_comment_id = 1
    private var appApiPixivService: AppApiPixivService? = null

    private var callback: Callback? = null

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "ViewDialogFragment")
    }

    private fun getData() {

        val restClient = RestClient()
        appApiPixivService = restClient.retrofit_AppAPI.create(AppApiPixivService::class.java)
        Observable.just(1).flatMap {

            runBlocking {
                Authorization = AppDataRepository.getUser().Authorization
            }
            appApiPixivService!!.getIllustComments(Authorization!!, id!!)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .retryWhen(ReFreshFunction(context!!))
                .subscribe(object : Observer<IllustCommentsResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(illustCommentsResponse: IllustCommentsResponse) {
                        button.isClickable=true
                        commentAdapter = CommentAdapter(R.layout.view_comment_item, illustCommentsResponse.comments, context)
                        recyclerviewPicture.isNestedScrollingEnabled = false
                        recyclerviewPicture.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        recyclerviewPicture.adapter = commentAdapter
                        recyclerviewPicture.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
                        commentAdapter!!.setOnItemClickListener { adapter, view, position ->
                            val builder = AlertDialog.Builder(activity!!)

                            val comment = illustCommentsResponse.comments[position].comment
                            builder.setMessage(comment)
                            val dialog = builder.create()
                            dialog.show()
                        }
                        commentAdapter!!.setOnItemChildClickListener { adapter, view, position ->
                            if (view.id == R.id.commentuserimage) {
                                val intent = Intent(context, UserMActivity::class.java)

                                intent.putExtra("data", illustCommentsResponse.comments[position].user.id)
                                startActivity(intent)
                            }
                            if (view.id == R.id.repley_to_hit) {
                                Parent_comment_id = illustCommentsResponse.comments[position].id
                                edittextComment.hint = "回复:" + illustCommentsResponse.comments[position].user.name
                            }
                        }
                        button.setOnClickListener { commit() }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })


    }

    fun commit() {
        appApiPixivService!!
                .postIllustComment(Authorization!!, id!!, edittextComment.text.toString(), if (Parent_comment_id == 1) null else Parent_comment_id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(responseBody: ResponseBody) {
                        Observable.just(1).flatMap(object : Function<Int, ObservableSource<IllustCommentsResponse>> {
                            override fun apply(t: Int): ObservableSource<IllustCommentsResponse> {
                                var Authorization: String = ""
                                runBlocking {
                                    Authorization = AppDataRepository.getUser().Authorization
                                }
                                return appApiPixivService!!.getIllustComments(Authorization, id!!)
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .retryWhen(ReFreshFunction(context!!))
                                .subscribe(object : Observer<IllustCommentsResponse> {
                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onNext(illustCommentsResponse: IllustCommentsResponse) {

                                        commentAdapter!!.setNewData(illustCommentsResponse.comments)
                                        Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT).show()
                                        edittextComment.setText("")
                                        Parent_comment_id = 1
                                        edittextComment.hint = ""
                                    }

                                    override fun onError(e: Throwable) {
                                        if ((e as HttpException).response()!!.code() == 403) {

                                            Toasty.warning(context!!, "Rate Limit", Toast.LENGTH_SHORT).show()


                                        } else if (e.response()!!.code() == 404) {


                                        }

                                    }

                                    override fun onComplete() {

                                    }
                                })

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })


    }


    override fun onStart() {
        super.onStart()


        val window = dialog!!.window
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度填充满屏
        window.attributes = params

        // 这里用透明颜色替换掉系统自带背景
        val color = ContextCompat.getColor(activity!!, android.R.color.transparent)
        window.setBackgroundDrawable(ColorDrawable(color))
    }


    interface Callback {
        fun onClick()

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initbind()
        sharedPreferencesServices = SharedPreferencesServices(context!!)
    }

    private fun initbind() {
        getData()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        id = bundle!!.getLong("id")

        val builder = AlertDialog.Builder(activity!!, R.style.AlertDialogCustom)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_comment, null)
        recyclerviewPicture = view.findViewById(R.id.recyclerview_picture)
        edittextComment = view.findViewById(R.id.edittext_comment)
        button = view.findViewById(R.id.button)
        builder.setView(view)
        sharedPreferencesServices = SharedPreferencesServices(context!!)
        getData()
        return builder.create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        } else {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    companion object {

        fun newInstance(id: Long?): CommentDialog {
            val commentDialog = CommentDialog()
            val bundle = Bundle()
            bundle.putLong("id", id!!)
            commentDialog.arguments = bundle
            return commentDialog
        }
    }
}
