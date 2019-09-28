package com.perol.asdpl.pixivez.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.TrendTagFragment
import com.perol.asdpl.pixivez.fragments.TrendTagViewModel
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.viewmodel.TagsTextViewModel


import kotlinx.android.synthetic.main.activity_search_r.*

class SearchRActivity : RinkActivity() {
    lateinit var searchRActivityFragment: SearchRActivityFragment
    lateinit var trendTagFragment: TrendTagFragment
    lateinit var tagsTextViewModel: TagsTextViewModel
    lateinit var trendTagViewModel: TrendTagViewModel
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                if (searchRActivityFragment.isHidden) {
                    this.finish()
                } else {
                    supportFragmentManager.beginTransaction().hide(searchRActivityFragment).show(trendTagFragment).commit()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK&&data!=null) {
                val word = data.getStringExtra("word")
                searchview_searchm.setQuery(word, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_search_r)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tagsTextViewModel = ViewModelProviders.of(this).get(TagsTextViewModel::class.java)
        trendTagViewModel = ViewModelProviders.of(this).get(TrendTagViewModel::class.java)
        searchRActivityFragment = SearchRActivityFragment()
        trendTagFragment = TrendTagFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction().apply {
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            add(R.id.fragment, searchRActivityFragment)
            add(R.id.fragment, trendTagFragment)
            hide(searchRActivityFragment)
        }
        transaction.commit()
        tablayout_searchm.clearOnTabSelectedListeners()
        tablayout_searchm.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) {
                        searchview_searchm.inputType = EditorInfo.TYPE_CLASS_TEXT
                    } else {
                        searchview_searchm.inputType = EditorInfo.TYPE_CLASS_NUMBER
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        searchview_searchm.onActionViewExpanded()
        searchview_searchm.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null && !query.isBlank())
                    when (tablayout_searchm.selectedTabPosition) {
                        0 -> {

                            trendTagViewModel.addhistory(query)
                            uptopage(query)
                        }
                        1 -> {
                            for (i in query) {
                                if (!i.isDigit())
                                    return true
                            }
                            val bundle = Bundle()
                            val arrayList = LongArray(1)
                            arrayList[0] = query.toLong()
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
                    } else {
                    supportFragmentManager.beginTransaction().hide(searchRActivityFragment).show(trendTagFragment).commit()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (tablayout_searchm.selectedTabPosition != 0) {
                    return true
                }
                if (!newText.isNullOrBlank())
                    tagsTextViewModel.onQueryTextChange(newText)
                supportFragmentManager.beginTransaction().hide(trendTagFragment).show(searchRActivityFragment).commit()
                return true
            }

        })

    }

    private fun uptopage(query: String) {
        val bundle = Bundle()
        bundle.putString("searchword", query)
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent,775)

    }
}
