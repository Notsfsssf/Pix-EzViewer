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
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PixivsionActivity
import com.perol.asdpl.pixivez.activity.WebViewActivity
import com.perol.asdpl.pixivez.adapters.PicItemAdapter
import com.perol.asdpl.pixivez.adapters.PixiVisionAdapter
import com.perol.asdpl.pixivez.adapters.RankingAdapter
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.objects.BaseFragment
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.HelloMRecomModel
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMRecommendFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMRecommendFragment : BaseFragment() {
    val disposables = CompositeDisposable()
    fun Disposable.add() {
        disposables.add(this)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AdapterRefreshEvent) {
        runBlocking {
            val allTags = blockViewModel.getAllTags()
            blockTags = allTags.map {
                it.name
            }
            rankingAdapter.blockTags = blockTags
            rankingAdapter.notifyDataSetChanged()
        }
    }

    private var nextUrl = ""
    private var oldBanner = false
    private var nextPixivisonUrl = ""
    override fun loadData() {
        swiperefresh_recom.isRefreshing = true
        viewmodel.firstRxGet().subscribe({
            swiperefresh_recom.isRefreshing = false
            viewmodel.getBanner().subscribe(
                {
                    if(oldBanner)
                    {
                        val arrayList = ArrayList<String>()
                        it.spotlight_articles.map {
                            arrayList.add(it.thumbnail)
                        }
                        banner.setDelayTime(3800)
                        banner.setImages(arrayList)
                        banner.setOnBannerListener {
                            startActivity(
                                Intent(
                                    requireActivity().applicationContext,
                                    PixivsionActivity::class.java
                                )
                            )
                        }
                        banner.start()
                    } else {
                        nextPixivisonUrl  = it.next_url
                        val spotlightView = bannerView.findViewById<RecyclerView>(R.id.pixivisionList)
                        spotlightView.layoutAnimation =
                            LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.left_in)).also {
                                it.order = LayoutAnimationController.ORDER_NORMAL
                                it.delay = 1f
                                it.interpolator = AccelerateInterpolator(0.5f)
                            }
                        pixiVisionAdapter.setNewData(it.spotlight_articles)
                        pixiVisionAdapter.addChildClickViewIds(R.id.imageView_pixivision)
                        pixiVisionAdapter.setOnItemChildClickListener { adapter, view, position ->
                            val intent = Intent(requireContext(), WebViewActivity::class.java)
                            intent.putExtra(
                                "url",
                                it.spotlight_articles[position].article_url
                            )
                            startActivity(intent)
                        }
                       spotlightView.setLayoutAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                spotlightView.layoutAnimation = null //show animation only at first time
                            }
                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                    }
                }, {}, {}
            ).add()
            nextUrl = it.next_url
            rankingAdapter.setNewData(it.illusts)

        }, {}, {}).add()

    }

    private lateinit var rankingAdapter: PicItemAdapter
    private lateinit var pixiVisionAdapter: PixiVisionAdapter
    lateinit var viewmodel: HelloMRecomModel
    private lateinit var banner: Banner

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProvider(this).get(HelloMRecomModel::class.java)
    }

    var exitTime = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swiperefresh_recom.setOnRefreshListener {
            loadData()
        }
        rankingAdapter.loadMoreModule?.setOnLoadMoreListener {
            if (!nextUrl.isNullOrBlank())
                viewmodel.onLoadMoreRxRequested(nextUrl).subscribe({
                    rankingAdapter.addData(it.illusts)
                    nextUrl = it.next_url
                    rankingAdapter.loadMoreComplete()
                }, {
                    rankingAdapter.loadMoreFail()
                }, {})
            else rankingAdapter.loadMoreEnd()
        }

        if(oldBanner){
            banner = bannerView.findViewById<Banner>(R.id.banner)
            banner.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context, path: Any?, imageView: ImageView?) {
                    GlideApp.with(context).load(path).into(imageView!!)
                }
            })
        } else {
            val spotlightView = bannerView.findViewById<RecyclerView>(R.id.pixivisionList)
            pixiVisionAdapter.loadMoreModule?.setOnLoadMoreListener{
                if (!nextPixivisonUrl.isNullOrBlank())
                    viewmodel.onLoadMoreBannerRequested(nextPixivisonUrl).subscribe({
                        nextPixivisonUrl = it.next_url
                        pixiVisionAdapter.addData(it.spotlight_articles)
                        pixiVisionAdapter.loadMoreModule?.loadMoreComplete()
                        spotlightView.scheduleLayoutAnimation()
                    }, {
                        pixiVisionAdapter.loadMoreModule?.loadMoreFail()
                    }, {})
                else pixiVisionAdapter.loadMoreModule?.loadMoreEnd()
            }
            val manager = LinearLayoutManager(requireContext())
            manager.orientation = LinearLayoutManager.HORIZONTAL
            spotlightView.layoutManager = manager
            spotlightView.adapter = pixiVisionAdapter
            PagerSnapHelper().attachToRecyclerView(spotlightView)
        }

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
                ).show()
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
        rankingAdapter =
            if(PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getBoolean("show_user_img_main",true)){

                RankingAdapter(
                R.layout.view_ranking_item_mid,
                    null,
                    isR18on,
                    blockTags,
                    singleLine = true,
                    hideBookmarked = false)
            } else{
                RecommendAdapter(
                R.layout.view_recommand_item,
                null,
                isR18on,
                blockTags,
                hideBookmarked = false)
            }
            if(PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getBoolean("use_new_banner",true)){
                oldBanner = false
                bannerView = inflater.inflate(R.layout.header_pixvision, container, false)
                pixiVisionAdapter = PixiVisionAdapter(
                    R.layout.view_pixivision_item_small,
                    null,
                    requireActivity())
            } else {
                oldBanner = true
                bannerView = inflater.inflate(R.layout.header_recom, container, false)
            }
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
