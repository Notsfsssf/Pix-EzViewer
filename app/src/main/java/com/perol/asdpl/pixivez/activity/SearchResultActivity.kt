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
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.SearchResultAdapter
import com.perol.asdpl.pixivez.databinding.ActivitySearchResultBinding
import com.perol.asdpl.pixivez.fragments.IllustFragment
import com.perol.asdpl.pixivez.fragments.UserFragment
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.content_search_result.*


class SearchResultActivity : RinkActivity() {
    lateinit var searchword: String;

    lateinit var binding: ActivitySearchResultBinding
    var arrayList = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        binding.lifecycleOwner = this
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        searchword = if (intent.extras != null) {
            intent.extras!!.getString("searchword")!!
        } else {
            "1"
        }
        initView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_block, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                this.finish() // back button
                return true
            }
            R.id.action_bloctag -> {
                startActivity(Intent(this, BlockActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initView() {

        tablayout_searchresult.setupWithViewPager(viewpage_searchresult)
        arrayList.add(IllustFragment.newInstance(searchword))
        arrayList.add(UserFragment.newInstance(searchword))
        viewpage_searchresult.adapter = SearchResultAdapter(this, supportFragmentManager, arrayList)
        viewpage_searchresult.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        pickbar.visibility = View.VISIBLE
                    }
                    else -> {
                        pickbar.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        searchtext.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("word", searchtext.text.toString())
            })
            finish()
        }

    }

}
