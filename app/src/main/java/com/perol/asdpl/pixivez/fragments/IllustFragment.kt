package com.perol.asdpl.pixivez.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.dialog.SearchSectionDialog
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.illustFactory
import kotlinx.android.synthetic.main.fragment_illust.*
import kotlinx.coroutines.runBlocking


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "word"


/**

 * Use the [IllustFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IllustFragment : LazyV4Fragment(), AdapterView.OnItemSelectedListener {
    override fun loadData() {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (view != null) {
            selectSort = position
            if (position == 2) {
                runBlocking {
                    val user = AppDataRepository.getUser()
                    if (!user.ispro) {
                        Toasty.error(PxEZApp.instance, "not premium!").show()
                        viewModel.setPreview(param1!!, sort[position], null, null)
                    } else
                        viewModel.firstsetdata(param1!!, sort[selectSort], null, null)

                }
            } else
                viewModel.firstsetdata(param1!!, sort[selectSort], null, null)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchtext = activity!!.findViewById<TextView>(R.id.searchtext)
        searchIllustAdapter = RecommendAdapter(R.layout.view_recommand_item, ArrayList<Illust>(), PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("r18on", false))
        searchtext.text = param1
        recyclerview_illust.adapter = searchIllustAdapter
        recyclerview_illust.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        for (i in 0 until bmbill.piecePlaceEnum.pieceNumber()) {
//            val builder = HamButton.Builder().listener { index ->
//                val query = param1 + " " + starnum[index].toString() + "users入り"
//                viewModel.firstsetdata(query, sort[selectSort], null, null)
//                recyclerview_illust.scrollToPosition(0)
//            }
//                    .normalImageRes(R.drawable.ic_starx)
//                    .normalText(starnum[i].toString() + " users入り!")
//                    .subNormalText("超过 " + starnum[i] + " 收藏！")
//            bmbill.addBuilder(builder)
//
//        }
        fab.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(activity)
            val arrayList = arrayOfNulls<String>(starnum.size)
            for (i in starnum.indices) {
                arrayList[i] = (param1 + " " + starnum[i].toString() + "users入り")
            }
            builder.setTitle("users入り")
                    .setItems(arrayList
                    ) { dialog, which ->
                        val query = param1 + " " + starnum[which].toString() + "users入り"
                        viewModel.firstsetdata(query, sort[selectSort], null, null)
                        recyclerview_illust.scrollToPosition(0)
                    }
            builder.create().show()
        }
        val imageButton = activity!!.findViewById<ImageButton>(R.id.imagebutton_section)
        imageButton.setOnClickListener {
            val searchSectionDialog = SearchSectionDialog()
            searchSectionDialog.callback = object : SearchSectionDialog.Callback {
                override fun onClick(search_target: Int, sort: Int, duration: Int) {
                    selectSort = sort
                    selectTarget = search_target
                    selectDuration = duration
                    viewModel.firstsetdata(param1!!, this@IllustFragment.sort[sort], this@IllustFragment.search_target[search_target],
                            this@IllustFragment.duration[duration])
                }

            }
            searchSectionDialog.show(childFragmentManager)
        }
        searchIllustAdapter.setOnLoadMoreListener({
            viewModel.onLoadMoreListen()
        }, recyclerview_illust)
        swiperefresh.setOnRefreshListener {
            runBlocking {
                val user = AppDataRepository.getUser()
                if (!user.ispro && selectSort == 2) {
                    Toasty.error(PxEZApp.instance, "not premium!").show()
                    viewModel.setPreview(param1!!, sort[selectSort], search_target[selectTarget], duration[selectDuration])
                } else
                    viewModel.firstsetdata(param1!!, sort[selectSort], search_target[selectTarget], duration[selectDuration])
            }
        }
        val spinner: Spinner = activity!!.findViewById<Spinner>(R.id.spinner_result)
        spinner.onItemSelectedListener = this
    }

    private val starnum = intArrayOf(50000, 30000, 20000, 10000, 5000, 1000, 500, 250, 100)
    private var param1: String? = null
    lateinit var searchIllustAdapter: RecommendAdapter
    var sort = arrayOf("date_desc", "date_asc", "popular_desc")
    var search_target = arrayOf("partial_match_for_tags", "exact_match_for_tags", "title_and_caption")
    var duration = arrayOf("within_last_day", "within_last_week", "within_last_month")
    var selectSort: Int = 0
    var selectTarget: Int = 0
    var selectDuration: Int = 0
    private lateinit var viewModel: IllustfragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
        lazyLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_illust, container, false)
    }


    fun lazyLoad() {

//        val view = layoutInflater.inflate(R.layout.headerview_illust, null)
//        val imageView = view.findViewById<ImageButton>(R.id.imagebutton_section)
//        searchIllustAdapter.addHeaderView(view)
        viewModel = ViewModelProviders.of(this, illustFactory()).get(IllustfragmentViewModel::class.java)

        viewModel.illusts.observe(this, Observer {
            updateillust(it)
        })
        viewModel.addIllusts.observe(this, Observer {
            addIllust(it)
        })
        viewModel.nexturl.observe(this, Observer {
            nexturl(it)
        })
        viewModel.bookmarkid.observe(this, Observer {
            changetoblue(it)
        })

    }

    private fun addIllust(it: java.util.ArrayList<Illust>) {
        searchIllustAdapter.addData(it)
        searchIllustAdapter.loadMoreComplete()
    }

    private var position: Int? = null
    private fun changetoblue(it: Long?) {
        if (it != null) {
            val item = searchIllustAdapter.getViewByPosition(recyclerview_illust, position!!, R.id.linearlayout_isbookmark) as LinearLayout
            item.setBackgroundColor(resources.getColor(R.color.yellow))
            Toasty.success(activity!!.applicationContext, "收藏成功", Toast.LENGTH_SHORT).show()
        }
    }

    private fun nexturl(it: String?) {
        if (it != null) {

        } else {
            searchIllustAdapter.loadMoreEnd()
        }
    }

    private fun updateillust(it: ArrayList<Illust>?) {
        if (it != null) {
            searchIllustAdapter.setNewData(it)
            swiperefresh.isRefreshing = false
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment IllustFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                IllustFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)

                    }
                }
    }
}
