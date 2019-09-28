package com.perol.asdpl.pixivez.fragments.HelloM


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.preference.PreferenceManager

import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.HelloMMyViewModel
import kotlinx.android.synthetic.main.fragment_hello_mmy.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMMyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMMyFragment : LazyV4Fragment() {
    override fun loadData() {

    }

    lateinit var rankingAdapter:RecommendAdapter
    var viewmodel:HelloMMyViewModel?=null
    var restrict="all"
    fun lazyLoad() {
        viewmodel= ViewModelProviders.of(this).get(HelloMMyViewModel::class.java)
        viewmodel!!.addillusts.observe(this, Observer {
            if (it != null) {
                rankingAdapter.addData(it)
            }
        })
        viewmodel!!.illusts.observe(this, Observer {
            swiperefresh_mym.isRefreshing = false
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
        lazyLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        rankingAdapter= RecommendAdapter(R.layout.view_recommand_item, null,PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("r18on",false));
        return inflater.inflate(R.layout.fragment_hello_mmy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview_mym.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerview_mym.adapter=rankingAdapter
        swiperefresh_mym.setOnRefreshListener {
            viewmodel!!.OnRefreshListener(restrict)
        }
        rankingAdapter.setOnLoadMoreListener({
            viewmodel!!.onLoadMoreRequested()
        }, recyclerview_mym)
        spinner_mmy.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        restrict="all"
                    }
                    1 -> {
                        restrict="public"
                    }
                    2->{
                        restrict="private"
                    }
                }
                viewmodel!!.OnRefreshListener(restrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMMyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HelloMMyFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
