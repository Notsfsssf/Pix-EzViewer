package com.perol.asdpl.pixivez.dialog


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.TagsShowAdapter
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.AppApiPixivService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking


class TagsShowDialog : DialogFragment() {

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "tagshowDialog")
    }


    var callback: Callback? = null
    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    interface Callback {
        fun onClick(string: String,public:String)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val restClient = RestClient()
        val appApiPixivService = restClient.retrofit_AppAPI.create(AppApiPixivService::class.java)

        val bundle = arguments
        val inflater = LayoutInflater.from(activity)
        val arrayList = bundle!!.getStringArrayList("tags")
        val arrayList1 = bundle!!.getIntegerArrayList("counts")
        val id = bundle!!.getLong("id")
        var nexturl = bundle.getString("nexturl")
        val builder = AlertDialog.Builder(activity)
        val dialogView = inflater.inflate(R.layout.view_tagsshow, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerview_tags)
        val all = dialogView.findViewById<ConstraintLayout>(R.id.all)
//        val viewPager = dialogView.findViewById<ViewPager>(R.id.viewpager)
        val tagLayOut = dialogView.findViewById<TabLayout>(R.id.tablayout_tagsshow)
        val tagsShowAdapter = TagsShowAdapter(R.layout.view_tagsshow_item, arrayList, arrayList1!!)
        recyclerView.adapter = tagsShowAdapter
        tagsShowAdapter.setOnItemClickListener { adapter, view, position ->
            callback!!.onClick(tagsShowAdapter.data[position],if (tagLayOut.selectedTabPosition == 0) {
                "public"
            } else {
                "private"
            })
            this.dismiss()
        }
        all.setOnClickListener {
            callback!!.onClick("",if (tagLayOut.selectedTabPosition == 0) {
                "public"
            } else {
                "private"
            })
            this.dismiss()
        }
        tagLayOut.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                var Authorization:String=""
                runBlocking {
                    Authorization = AppDataRepository.getUser().Authorization
                }
                if (p0 != null)
                    appApiPixivService.getIllustBookmarkTags(Authorization, id, if (p0.position == 0) {
                        "public"
                    } else {
                        "private"
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribe({
                                nexturl = it.next_url
                                val x = ArrayList<String>();
                                tagsShowAdapter.counts.clear()
                                it.bookmark_tags.map { ot ->
                                    x.add(ot.name)

                                    tagsShowAdapter.counts.add(ot.count)
                                }
                                tagsShowAdapter.setNewData(x)
                            }, {}, {})


            }
        })
        tagsShowAdapter.setOnLoadMoreListener({
            if (!nexturl.isNullOrBlank()) {
                var Authorization:String=""
                runBlocking {
                    Authorization = AppDataRepository.getUser().Authorization
                }
                appApiPixivService.getNexttags(Authorization, nexturl!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            nexturl = it.next_url
                            val arrayList = ArrayList<String>()
                            it.bookmark_tags.map {
                                arrayList.add(it.name)
                                tagsShowAdapter.counts.add(it.count)
                            }
                            tagsShowAdapter.addData(arrayList)
                        }, { tagsShowAdapter.loadMoreFail() }, { tagsShowAdapter.loadMoreComplete() })
            } else {
                tagsShowAdapter.loadMoreEnd()
            }
        }, recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        builder.setView(dialogView)
        return builder.create()

    }
}