package com.perol.asdpl.pixivez.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.SearchView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.TrendingtagAdapter
import com.perol.asdpl.pixivez.databinding.ActivitySearchMBinding
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.responses.TrendingtagResponse
import com.perol.asdpl.pixivez.viewmodel.SearchMViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.SearchMFactory
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search_m.*


class SearchMActivity : RinkActivity() {
    lateinit var searchMBinding: ActivitySearchMBinding
    lateinit var listPopupWindow: ListPopupWindow
    lateinit var autowordadapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        searchMBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_m)
        searchMBinding.setLifecycleOwner(this)

        listPopupWindow = ListPopupWindow(this)
        autowordadapter = ArrayAdapter<String>(applicationContext, R.layout.list_item_1, ArrayList<String>())
        listPopupWindow.setAdapter(autowordadapter)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initdata()
        initview()
        initlisten()
    }

    private lateinit var searchMViewModel: SearchMViewModel
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            android.R.id.home -> {
                this.finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initdata() {

        searchMViewModel = ViewModelProviders.of(this, SearchMFactory()).get(SearchMViewModel::class.java)
        searchMViewModel.searchhistroy.observe(this, Observer {
            updateView(it);
        })
        searchMViewModel.autoword.observe(this, Observer {
            popupautoword(it)
        })
        searchMBinding.searchm = searchMViewModel

        searchMViewModel.trendtag.observe(this, Observer {
            settrendtags(it)
        })
        searchMViewModel.getIllustTrendTags()
        searchview_searchm.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && !query.isBlank())
                    when (tablayout_searchm.selectedTabPosition) {
                        0 -> {
                            val arrayList = ArrayList<String>()
                            arrayList.add(query)
                            searchMViewModel.onQueryTextSubmit(arrayList)
                            uptopage(query)
                        }
                        1 -> {
                            for (i in query) {
                                if (!i.isDigit())
                                    return true
                            }
                            val bundle = Bundle()
                            val arrayList = LongArray(1)
                            arrayList[0]=query.toLong()
                            bundle.putLongArray("illustlist", arrayList)
                            bundle.putLong("illustid", query.toLong())
                            val intent = Intent(applicationContext, PictureActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        2 -> {
                            for (i in query) {
                                if (!i.isDigit())
                                    return true
                            }
                            val intent = Intent(applicationContext, UserMActivity::class.java)
                            intent.putExtra("data", query.toLong())
                            startActivity(intent)
                        }
                    } else listPopupWindow.dismiss()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank())
                    searchMViewModel.onQueryTextChange(newText!!)

                return true
            }

        })
        textView_clearn.setOnClickListener {

            searchMViewModel.sethis()
        }
        fab.setOnClickListener { startActivity(Intent(this, SaucenaoActivity::class.java)) }

    }

    private fun settrendtags(it: TrendingtagResponse?) {
        if (it != null) {
            recyclerview_searhm.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            val trendingtagAdapter = TrendingtagAdapter(R.layout.view_trendingtag_item, it.trend_tags)
            recyclerview_searhm.adapter = trendingtagAdapter
            recyclerview_searhm.isNestedScrollingEnabled = false
            trendingtagAdapter.setOnItemClickListener { adapter, view, position ->
                val searchword = it.trend_tags[position].tag
                uptopage(searchword)
                searchMViewModel.addhistory(searchword)
            }

        }
    }

    private fun uptopage(query: String) {
        val bundle = Bundle()
        bundle.putString("searchword", query)
        val intent = Intent(applicationContext, SearchResultActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun popupautoword(it: ArrayList<String>?) {
        if (it != null) {
            autowordadapter.clear()
            autowordadapter.addAll(it)
            listPopupWindow.show()
        }

    }


    private fun initlisten() {

    }


    private fun initview() {


        // 选择item的监听事件
        listPopupWindow.setOnItemClickListener { parent, view, pos, id ->
            // listPopupWindow.dismiss();
            searchMViewModel.addhistory(searchMViewModel.autoword.value!!.get(pos))

            uptopage(searchMViewModel.autoword.value!!.get(pos))


        }
        searchview_searchm.onActionViewExpanded()




        listPopupWindow.anchorView = toolbar
        // ListPopupWindow 距锚view的距离
        listPopupWindow.horizontalOffset = 0
        listPopupWindow.verticalOffset = 0

        listPopupWindow.isModal = false
    }

    private fun updateView(arrayList: ArrayList<String>?) {
        val mVals = arrayList!!.toTypedArray()
        if (arrayList.isNotEmpty()) textView_clearn.visibility = View.VISIBLE
        else textView_clearn.visibility = View.GONE
        search_page_flowlayout.adapter = object : TagAdapter<String>(mVals) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = LayoutInflater.from(applicationContext).inflate(R.layout.picture_tagm, search_page_flowlayout, false) as TextView
                tv.text = s
                return tv
            }
        }
        search_page_flowlayout.setOnTagClickListener { view, position, parent ->
            uptopage(arrayList[position])

            true
        }

    }

}
