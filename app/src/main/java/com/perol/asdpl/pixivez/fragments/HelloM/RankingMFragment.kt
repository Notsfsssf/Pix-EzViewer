package com.perol.asdpl.pixivez.fragments.HelloM


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.SharedMemory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RankingAdapter
import com.perol.asdpl.pixivez.dialog.DateDialog
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.RankingMViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.RankingShareViewModel
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [RankingMFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RankingMFragment : LazyV4Fragment(), DateDialog.Callback {
    override fun onClick(data: String?) {
        Log.d("s",data)
        sharemodel!!.picdateshare.value=data
    }

    var picdate: String? = null
    var viewmodel: RankingMViewModel? = null
    var sharemodel:RankingShareViewModel?=null
    override fun lazyLoad() {

        viewmodel = ViewModelProviders.of(this).get(RankingMViewModel::class.java)
      sharemodel  = ViewModelProviders.of(activity!!).get(RankingShareViewModel::class.java)
        sharemodel!!.picdateshare.observe(this, Observer {
            viewmodel!!.datapick(param1!!,it)
        })
        viewmodel!!.addillusts.observe(this, Observer {
            if (it != null) {
                rankingAdapter.addData(it)
            }
        })
        viewmodel!!.illusts.observe(this, Observer {
            swiperefresh_rankingm.isRefreshing = false
            rankingAdapter.setNewData(it)
        })
        viewmodel!!.bookmarknum.observe(this, Observer {
            if (it != null) {
                viewmodel!!.OnItemChildLongClick(it)
            }
        })
        viewmodel!!.nexturl.observe(this, Observer {
            if (it == null) {
                rankingAdapter.loadMoreEnd()
            } else {
                rankingAdapter.loadMoreComplete()
            }
        })
        swiperefresh_rankingm.setOnRefreshListener {
            viewmodel!!.OnRefresh(param1!!, picdate)
        }
        rankingAdapter.setOnLoadMoreListener({
            viewmodel!!.OnLoadMore()
        }, recyclerview_rankingm)
        viewmodel!!.first(param1!!, picdate)
        activity!!.findViewById<ImageView>(R.id.imageview_triangle).apply {
        setOnClickListener {
            val dateDialog = DateDialog()
            dateDialog.callback = this@RankingMFragment
            dateDialog.show(childFragmentManager)
        }
    }


    }

    lateinit var rankingAdapter: RankingAdapter
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var swiperefresh_rankingm: SwipeRefreshLayout
    lateinit var recyclerview_rankingm: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rankingAdapter = RankingAdapter(R.layout.view_ranking_item, null, PxEZApp.isR18On)
        isViewCreated=true
        return UI {
            coordinatorLayout {
                swiperefresh_rankingm = swipeRefreshLayout {
                    recyclerview_rankingm = recyclerView {
                        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        adapter = rankingAdapter
                    }
                }.lparams(width = matchParent, height = matchParent)

            }
        }.view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RankingMFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                RankingMFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)

                    }
                }
    }
}
