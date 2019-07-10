package com.perol.asdpl.pixivez.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.dialog.TagsShowDialog
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.UserMillustViewModel
import kotlinx.android.synthetic.main.fragment_user_illust.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserIllustFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserIllustFragment : LazyV4Fragment() {
    override fun lazyLoad() {
        mrecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mrecyclerview.adapter = recommendAdapter
        initvoid()
    }

    // TODO: Rename and change types of parameters
    private var param1: Long? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun initvoid() {
        val viewmodel = ViewModelProviders.of(this).get(UserMillustViewModel::class.java)
        viewmodel.nexturl.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                recommendAdapter.loadMoreEnd()
            } else {
                recommendAdapter.loadMoreComplete()
            }
        })
        viewmodel.data.observe(this, Observer {
            if (it != null) {
                mrefreshlayout.isRefreshing = false
                recommendAdapter.setNewData(it)
            }

        })
        viewmodel.adddata.observe(this, Observer {
            if (it != null) {
                recommendAdapter.addData(it)
                recommendAdapter.loadMoreComplete()
            }
        })
        viewmodel.first(param1!!,param2!!)
        recommendAdapter.setOnLoadMoreListener({
            viewmodel.OnLoadMoreListener()
        }, mrecyclerview)
        mrefreshlayout.setOnRefreshListener {
            viewmodel.OnRefreshListener(param1!!,param2!!)
        }
    }


    lateinit var recommendAdapter: RecommendAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        recommendAdapter = RecommendAdapter(R.layout.view_recommand_item, null, PxEZApp.isR18On)
        isViewCreated=true
        return inflater.inflate(R.layout.fragment_user_illust, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserIllustFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long, param2: String) =
                UserIllustFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
