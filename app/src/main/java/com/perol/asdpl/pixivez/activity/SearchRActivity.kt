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

package com.perol.asdpl.pixivez.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.TrendTagFragment
import com.perol.asdpl.pixivez.fragments.TrendTagViewModel
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.viewmodel.TagsTextViewModel
import kotlinx.android.synthetic.main.activity_search_r.*

class SearchRActivity : RinkActivity(), SearchRActivityFragment.OnFragmentInteractionListener {
    var lastSearchQuery: String = ""
    override fun onFragmentInteraction(search: String) {

        if (tablayout_searchm.selectedTabPosition == 0){
            if (lastSearchQuery == "")
                lastSearchQuery = "$search "
            else
                lastSearchQuery += "$search "
            searchview_searchm.setQuery(lastSearchQuery,false)
        }else{
            searchview_searchm.setQuery(search,false)
        }

    }

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
        if (resultCode == Activity.RESULT_OK && data != null) {
            val word = data.getStringExtra("word")
//            word += " R-18"
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

                            var trimmedQuery = query.substring(0,query.length -1)
                            trendTagViewModel.addhistory(trimmedQuery)
                            uptopage(trimmedQuery)
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
                if (!newText.isNullOrBlank()){
                    var searchText = newText.replace(lastSearchQuery, "")
                    tagsTextViewModel.onQueryTextChange(searchText)
                }
                    //find previous query and remove to search for new query

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
        startActivityForResult(intent, 775)

    }
}

