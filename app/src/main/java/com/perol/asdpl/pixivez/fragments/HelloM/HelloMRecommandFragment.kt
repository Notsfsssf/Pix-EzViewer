package com.perol.asdpl.pixivez.fragments.HelloM


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.github.ybq.android.spinkit.SpinKitView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PixivsionActivity
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.HelloMRecomModel
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader
import com.zhy.view.flowlayout.TagFlowLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.custom.style
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMRecommandFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMRecommandFragment : LazyV4Fragment() {
    override fun loadData() {
        viewmodel!!.firstget()
    }

    lateinit var rankingAdapter: RecommendAdapter
    var viewmodel: HelloMRecomModel? = null
    var banner: Banner? = null
    fun lazyLoad() {
        viewmodel!!.articles.observe(this, Observer { it ->
            if (it != null) {
                val arrayList = ArrayList<String>()
                it.spotlight_articles.map {
                    arrayList.add(it.thumbnail)
                }
                banner!!.setDelayTime(3800);
                banner!!.setImages(arrayList)
                banner!!.setOnBannerListener {
                    startActivity(Intent(activity!!.applicationContext, PixivsionActivity::class.java))
                }
                banner!!.start()
            }

        })
        viewmodel!!.addillusts.observe(this, Observer {
            if (it != null) {
                rankingAdapter.addData(it)
            }
        })
        viewmodel!!.illusts.observe(this, Observer {
            val view=layoutInflater.inflate(R.layout.empty_erro, null)
            rankingAdapter.emptyView=view
            swiperefresh_recom.isRefreshing = false
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

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProviders.of(this).get(HelloMRecomModel::class.java)
        lazyLoad()
    }

    lateinit var recyclerview_recom: RecyclerView
    lateinit var banerview: View
    lateinit var swiperefresh_recom: SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rankingAdapter = RecommendAdapter(R.layout.view_recommand_item, null, PxEZApp.isR18On)

        val view = UI {
            coordinatorLayout {
                swiperefresh_recom = swipeRefreshLayout {
                    recyclerview_recom = recyclerView {
                        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        adapter = rankingAdapter.apply {
                            banerview = UI {

                                include<View>(R.layout.header_recom) {
                                }.lparams(width = matchParent, height = wrapContent) {
                                    //                                        scrollFlags =SCROLL_FLAG_SCROLL
                                }
                            }.view
                            addHeaderView(banerview)
                        }
                    }
                }.lparams(width = matchParent, height = matchParent) {
                }
            }
        }.view
        banner = banerview.findViewById<Banner>(R.id.banner)
        banner!!.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                GlideApp.with(context!!).load(path).into(imageView!!)
            }
        })
        swiperefresh_recom.setOnRefreshListener {
            viewmodel!!.OnRefreshListener()

        }
        rankingAdapter.setOnLoadMoreListener({
            viewmodel!!.onLoadMoreRequested()
        }, recyclerview_recom)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMRecommandFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HelloMRecommandFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
