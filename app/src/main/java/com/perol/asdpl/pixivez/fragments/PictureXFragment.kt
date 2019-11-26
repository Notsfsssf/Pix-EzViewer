/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.PictureXAdapter
import com.perol.asdpl.pixivez.adapters.TagsAdapter
import com.perol.asdpl.pixivez.databinding.FragmentPictureXBinding
import com.perol.asdpl.pixivez.dialog.CommentDialog
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.viewmodel.PictureXViewModel
import kotlinx.android.synthetic.main.fragment_picture_x.*
import kotlin.collections.set


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [PictureXFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PictureXFragment : Fragment() {

    private var param1: Long? = null
    lateinit var pictureXViewModel: PictureXViewModel
    fun loadData() {
//        val item = activity?.intent?.extras
//        val illust = item?.getParcelable<Illust>(param1.toString())
        pictureXViewModel.firstGet(param1!!, null)


    }

    override fun onDestroy() {
        if (pictureXAdapter != null) {
            pictureXAdapter?.setListener { }
            pictureXAdapter?.setViewCommentListen { }
            pictureXAdapter?.setUserPicLongClick { }
        }
        super.onDestroy()

    }

    var pictureXAdapter: PictureXAdapter? = null
    private fun initViewModel() {

        pictureXViewModel = ViewModelProviders.of(this).get(PictureXViewModel::class.java)
        pictureXViewModel.illustDetailResponse.observe(this, Observer {
            if (it != null) {
                rootBinding.illust = it.illust
                if (it.illust.meta_pages.isNotEmpty())
                    position = it.illust.meta_pages.size
                else position = 1

                pictureXAdapter = PictureXAdapter(pictureXViewModel, it.illust, activity!!).also {
                    it.setListener {
                        //  startPostponedEnterTransition()
                        if (!hasMoved) {
                            recyclerview.scrollToPosition(0)
                            (recyclerview.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                        }
                        pictureXViewModel.getRelative(param1!!)

                    }
                    it.setViewCommentListen {
                        val commentDialog = CommentDialog.newInstance(pictureXViewModel.illustDetailResponse.value!!.illust.id)
                        commentDialog.show(childFragmentManager)
                    }
                    it.setUserPicLongClick {
                        pictureXViewModel.likeUser()
                    }

                }

                recyclerview.adapter = pictureXAdapter


                imageView5.setOnClickListener { ot ->
                    val intent = Intent(context, UserMActivity::class.java)
                    intent.putExtra("data", it.illust.user.id)
                    startActivity(intent)
                }
                fab.show()

            }
        })
        pictureXViewModel.aboutPics.observe(this, Observer {
            if (pictureXAdapter != null) {
                pictureXAdapter?.setRelativeNow(it)
            }
        })
        pictureXViewModel.likeIllust.observe(this, Observer {
            if (it != null) {
                if (it) {
                    GlideApp.with(this).load(R.drawable.heart_red).into(fab)
                } else {
                    GlideApp.with(this).load(R.drawable.ic_action_heart).into(fab)
                }

            }
        })
        pictureXViewModel.followUser.observe(this, Observer {
            pictureXAdapter?.setUserPicColor(it)
        })
        pictureXViewModel.progress.observe(this, Observer {
            if (pictureXAdapter != null) {
                pictureXAdapter!!.setProgress(it)
            }
        })
        pictureXViewModel.downloadGifSuccess.observe(this, Observer {
            if (pictureXAdapter != null)
                pictureXAdapter?.setProgressComplete(it)
        })
        pictureXViewModel.tags.observe(this, Observer { itRaw ->
            val it = itRaw.tags
            val arrayList = ArrayList<String>()
            if (it != null && !pictureXViewModel.likeIllust.value!!) {
                it.map {
                    arrayList.add(it.name)
                }
                val checkStatus = HashMap<Int, Boolean>()
                for (i in it.indices) {
                    checkStatus[i] = false
                }
                val tagsAdapter = TagsAdapter(R.layout.view_tags_item, arrayList, checkStatus)
                val alertBuilder = MaterialAlertDialogBuilder(activity!!)
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_star, null)

                val switch = view.findViewById<Switch>(R.id.switch_private).also {
                    it.isChecked = itRaw.restrict != "public"
                }
                val edit = view.findViewById<TextInputEditText>(R.id.edit)

                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview).also {
                    it.layoutManager = LinearLayoutManager(activity)
                    it.adapter = tagsAdapter
                }
                val add = view.findViewById<Button>(R.id.add).also {
                    it.setOnClickListener {
                        if (!edit.text.isNullOrBlank()) {
                            tagsAdapter.addData(0, edit.text.toString())
                            checkStatus[checkStatus.count()] = false
                            recyclerView.smoothScrollToPosition(0)
                        }
                    }
                }
                alertBuilder.setView(view)
                alertBuilder.setPositiveButton("LIKE!") { a, b ->
                    val taglist = ArrayList<String>()
                    if (tagsAdapter.checkStatus.isNotEmpty())
                        for (i in 0 until tagsAdapter.checkStatus.size) {
                            if (tagsAdapter.checkStatus[i]!!) {
                                taglist.add(arrayList[i])
                            }
                        }
                    println(taglist)
                    pictureXViewModel.onDialogClick(taglist, switch!!.isChecked, pictureXViewModel.illustDetailResponse.value!!.illust.id);
                    a.dismiss()
                }
                alertBuilder.create().show()
//              no more anko
            }
        })

    }

    var hasMoved = false
    private fun initView() {
        fab.setOnClickListener {
            pictureXViewModel.FabClick()
        }
        fab.setOnLongClickListener {
            Toasty.info(activity!!, resources.getString(R.string.fetchtags), Toast.LENGTH_SHORT).show()
            pictureXViewModel.fabOnLongClick()
            true
        }
        imageButton.setOnClickListener {
            recyclerview.scrollToPosition(position)
        }
//        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity!!)
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hasMoved = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == position || linearLayoutManager.findFirstCompletelyVisibleItemPosition() == position || linearLayoutManager.findFirstVisibleItemPosition() == position || linearLayoutManager.findLastVisibleItemPosition() == position) {
                    constraintLayout_fold.visibility = View.INVISIBLE
                } else
                    constraintLayout_fold.visibility = View.VISIBLE
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
        initViewModel()
    }

    lateinit var rootBinding: FragmentPictureXBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootBinding = FragmentPictureXBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PictureXFragment
        }
        return rootBinding.root
    }

    var position = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PictureXFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long) =
                PictureXFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)
                    }
                }
    }
}
