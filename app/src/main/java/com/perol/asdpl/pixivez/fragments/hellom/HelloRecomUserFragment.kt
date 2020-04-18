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

package com.perol.asdpl.pixivez.fragments.hellom


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.objects.BaseFragment
import com.perol.asdpl.pixivez.viewmodel.HelloRecomUserViewModel
import kotlinx.android.synthetic.main.fragment_recom_user.*


/**
 * A simple [Fragment] subclass.
 * Use the [HelloRecomUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloRecomUserFragment : BaseFragment() {
    override fun loadData() {
        viewmodel!!.reData()
    }

    var viewmodel: HelloRecomUserViewModel? = null
    private fun lazyLoad() {

        viewmodel!!.adddata.observe(this, Observer {
            if (it != null) {
                userShowAdapter.addData(it)
            } else {
                userShowAdapter.loadMoreModule?.loadMoreFail()
            }
        })
        viewmodel!!.data.observe(this, Observer {
            userShowAdapter.setNewData(it.toMutableList())
            swipe.isRefreshing = false
        })
        viewmodel!!.nexturl.observe(this, Observer {
            if (it != null) {
                userShowAdapter.loadMoreModule?.loadMoreComplete()
            } else {
                userShowAdapter.loadMoreModule?.loadMoreEnd()
            }
        })


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(HelloRecomUserViewModel::class.java)
        lazyLoad()
    }

    val userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = userShowAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        userShowAdapter.loadMoreModule?.setOnLoadMoreListener { viewmodel!!.getNext() }
        swipe.setOnRefreshListener {
            viewmodel!!.reData()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recom_user, container, false)
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelloRecomUserFragment()
    }
}
