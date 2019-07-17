package com.perol.asdpl.pixivez.activity


import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
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
        ThemeUtil.Themeinit(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            searchword = intent.extras!!.getString("searchword")!!

        } else {
            searchword = "1"
        }
        initview()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                this.finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initdata() {

    }

    private fun initlisen() {

    }

    private fun initview() {

        tablayout_searchresult.setupWithViewPager(viewpage_searchresult)
        arrayList.add(IllustFragment.newInstance(searchword))
        arrayList.add(UserFragment.newInstance(searchword))
        viewpage_searchresult.adapter = SearchResultAdapter(this,supportFragmentManager, arrayList)
        viewpage_searchresult.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

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

    }

}
