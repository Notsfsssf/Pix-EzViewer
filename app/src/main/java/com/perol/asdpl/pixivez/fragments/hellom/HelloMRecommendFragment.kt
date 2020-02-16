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


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PixivsionActivity
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.HelloMRecomModel
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recommend.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMRecommendFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMRecommendFragment : LazyV4Fragment() {
    val disposables = CompositeDisposable()
    fun Disposable.add() {
        disposables.add(this)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private var nextUrl = ""
    override fun loadData() {
        swiperefresh_recom.isRefreshing = true
        viewmodel.firstRxGet().subscribe({
            viewmodel.getBanner().subscribe(
                {
                    val arrayList = ArrayList<String>()
                    it.spotlight_articles.map {
                        arrayList.add(it.thumbnail)
                    }
                    banner.setDelayTime(3800);
                    banner.setImages(arrayList)
                    banner.setOnBannerListener {
                        startActivity(
                            Intent(
                                activity!!.applicationContext,
                                PixivsionActivity::class.java
                            )
                        )
                    }
                    banner.start()
                }, {}, {}
            ).add()
            nextUrl = it.next_url
            swiperefresh_recom.isRefreshing = false
            rankingAdapter.setNewData(it.illusts)

        }, {
            swiperefresh_recom.isRefreshing = false
        }, {}).add()

    }

    lateinit var rankingAdapter: RecommendAdapter
    lateinit var viewmodel: HelloMRecomModel
    lateinit var banner: Banner


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProviders.of(this).get(HelloMRecomModel::class.java)
    }

    var exitTime = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swiperefresh_recom.setOnRefreshListener {
            loadData()
        }
        rankingAdapter.setOnLoadMoreListener({
            if (!nextUrl.isNullOrBlank())
                viewmodel.onLoadMoreRxRequested(nextUrl).subscribe({
                    rankingAdapter.addData(it.illusts)
                    nextUrl = it.next_url
                }, {
                    rankingAdapter.loadMoreFail()
                }, {})
            else rankingAdapter.loadMoreEnd()
        }, recyclerview_recom)
        banner = bannerView.findViewById<Banner>(R.id.banner)
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any?, imageView: ImageView?) {
                GlideApp.with(context).load(path).into(imageView!!)
            }
        })
        recyclerview_recom.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerview_recom.adapter = rankingAdapter

        parentFragment?.view?.findViewById<TabLayout>(R.id.tablayout)?.getTabAt(0)
            ?.view?.setOnClickListener {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(
                    PxEZApp.instance,
                    getString(R.string.back_to_the_top),
                    Toast.LENGTH_SHORT
                ).show();
                exitTime = System.currentTimeMillis()
            } else {
                recyclerview_recom.smoothScrollToPosition(0)
            }

        }
    }

    private lateinit var bannerView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rankingAdapter = RecommendAdapter(
            R.layout.view_recommand_item,
            null,
            PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("r18on", false)
        )
        bannerView = inflater.inflate(R.layout.header_recom, container, false)
        rankingAdapter.apply {
            addHeaderView(bannerView)
        }

        return inflater.inflate(R.layout.fragment_recommend, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMRecommendFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelloMRecommendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
