package com.perol.asdpl.pixivez.fragments.HelloM


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.viewmodel.HelloRecomUserViewModel
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7._RecyclerView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloRecomUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloRecomUserFragment : LazyV4Fragment() {
    var viewmodel:HelloRecomUserViewModel?=null
    override fun lazyLoad() {

        viewmodel!!.adddata.observe(this, Observer {
            if (it != null) {
                userShowAdapter.addData(it)
            } else {
                userShowAdapter.loadMoreFail()
            }
        })
        viewmodel!!.data.observe(this, Observer {
            userShowAdapter.setNewData(it)
            swipe.isRefreshing = false
        })
        viewmodel!!.nexturl.observe(this, Observer {
            if (it != null) {
                userShowAdapter.loadMoreComplete()
            } else {
                userShowAdapter.loadMoreEnd()
            }
        })


    }

    override fun onResume() {
        super.onResume()
        viewmodel!!.reData()
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProviders.of(this).get(HelloRecomUserViewModel::class.java)
        lazyLoad()
    }

    val userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)
    lateinit var recyclerView: RecyclerView
    lateinit var swipe: SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

     val view =UI {
            swipe = swipeRefreshLayout {
                coordinatorLayout {
                    recyclerView = recyclerView {
                        adapter = userShowAdapter
                        layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    }.lparams(width = matchParent, height = matchParent)
                }
            }
        }.view

            userShowAdapter.setOnLoadMoreListener({
            viewmodel!!.getNext()
        }, recyclerView)
        swipe.setOnRefreshListener {
            viewmodel!!.reData()

        }
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloRecomUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HelloRecomUserFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
