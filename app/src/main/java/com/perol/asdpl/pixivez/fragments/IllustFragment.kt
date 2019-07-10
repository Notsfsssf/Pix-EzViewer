package com.perol.asdpl.pixivez.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nightonke.boommenu.BoomButtons.HamButton
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.dialog.SearchSectionDialog
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.illustFactory
import kotlinx.android.synthetic.main.fragment_illust.*
import org.jetbrains.anko.isSelectable


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "word"


/**

 * Use the [IllustFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IllustFragment : LazyV4Fragment(), AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (view != null) {
            selectsort = position
            illustfragmentViewModel.firstsetdata(param1!!, sort[selectsort], null, null)

        }


    }

    private val starnum = intArrayOf(10000, 5000, 1000, 500, 250, 100)
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    lateinit var searchIllustAdapter: RecommendAdapter
    var sort = arrayOf("date_desc", "date_asc", "popular_desc")
    var search_target = arrayOf("partial_match_for_tags", "exact_match_for_tags", "title_and_caption")
    var duration = arrayOf("within_last_day", "within_last_week", "within_last_month")
    var selectsort: Int = 0
    var selecttarget: Int = 0
    var selectduration: Int = 0
    private lateinit var illustfragmentViewModel: IllustfragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        isViewCreated = true
        return inflater.inflate(R.layout.fragment_illust, container, false)
    }


    override fun lazyLoad() {
        sharedPreferencesServices = SharedPreferencesServices(activity!!.applicationContext)
        val searchtext=activity!!.findViewById<TextView>(R.id.searchtext)
        searchIllustAdapter = RecommendAdapter(R.layout.view_recommand_item, ArrayList<IllustsBean>(), sharedPreferencesServices.getBoolean("r18on"))
        searchtext.text=param1
        recyclerview_illust.adapter = searchIllustAdapter
        recyclerview_illust.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        val view = layoutInflater.inflate(R.layout.headerview_illust, null)
//        val imageView = view.findViewById<ImageButton>(R.id.imagebutton_section)
//        searchIllustAdapter.addHeaderView(view)
        illustfragmentViewModel = ViewModelProviders.of(this, illustFactory()).get(IllustfragmentViewModel::class.java)

//        searchIllustAdapter.setNewData(illustfragmentViewModel.llustlivedata.value)
        illustfragmentViewModel.llustlivedata.observe(this, Observer {
            updateillust(it)
        })

        illustfragmentViewModel.nexturl.observe(this, Observer {
            nexturl(it)
        })
        illustfragmentViewModel.bookmarkid.observe(this, Observer {
            changetoblue(it)
        })

        for (i in 0 until bmbill.piecePlaceEnum.pieceNumber()) {
            val builder = HamButton.Builder().listener { index ->
                val query = param1 + " " + starnum[index].toString() + "users入り"
                illustfragmentViewModel.firstsetdata(query, sort[selectsort], null, null)
                reloadata = true
                recyclerview_illust.scrollToPosition(0)
            }
                    .normalImageRes(R.drawable.ic_starx)
                    .normalText(starnum[i].toString() + " users入り!")
                    .subNormalText("超过 " + starnum[i] + " 收藏！")
            bmbill.addBuilder(builder)

        }
        val imagebutton = activity!!.findViewById<ImageButton>(R.id.imagebutton_section)
        imagebutton.setOnClickListener {
            val searchSectionDialog = SearchSectionDialog()
            searchSectionDialog.callback = object : SearchSectionDialog.Callback {
                override fun onClick(search_target: Int, sort: Int, duration: Int) {
                    reloadata = true
                    selectsort = sort
                    selecttarget = search_target
                    selectduration = duration
                    illustfragmentViewModel.firstsetdata(param1!!, this@IllustFragment.sort[sort], this@IllustFragment.search_target[search_target],
                            this@IllustFragment.duration[duration])
                }

            }
            searchSectionDialog.show(childFragmentManager)
        }
        searchIllustAdapter.setOnLoadMoreListener({
            illustfragmentViewModel.OnLoadMoreListen()
        }, recyclerview_illust)
        swiperefresh.setOnRefreshListener {
            illustfragmentViewModel.firstsetdata(param1!!, sort[selectsort], search_target[selecttarget], duration[selectduration])
            reloadata = true
        }
        val spinner: Spinner = activity!!.findViewById<Spinner>(R.id.spinner_result)
        spinner.onItemSelectedListener = this
    }

    private var position: Int? = null
    private fun changetoblue(it: Long?) {
        if (it != null) {
            val item = searchIllustAdapter.getViewByPosition(recyclerview_illust, position!!, R.id.linearlayout_isbookmark) as LinearLayout
            item.setBackgroundColor(resources.getColor(R.color.yellow))
            Toasty.success(activity!!.applicationContext, "收藏成功", Toast.LENGTH_SHORT).show()
        }
    }

    private var reloadata = false
    private fun nexturl(it: String?) {
        if (it != null) {
            searchIllustAdapter.loadMoreComplete()
        } else {
            searchIllustAdapter.loadMoreEnd()
        }
    }

    private fun updateillust(it: ArrayList<IllustsBean>?) {
        if (it != null) {
            if (searchIllustAdapter.data.size == 0 || swiperefresh.isRefreshing || reloadata) {
                searchIllustAdapter.setNewData(it)
                reloadata = false
                swiperefresh.isRefreshing = false

            } else if (!reloadata && searchIllustAdapter.data.size != 0) {
                searchIllustAdapter.addData(it)
            }
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
