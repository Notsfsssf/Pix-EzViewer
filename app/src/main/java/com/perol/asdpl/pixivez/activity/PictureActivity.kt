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


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PicturePagerAdapter
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.activity_picture.*


class PictureActivity : RinkActivity() {

    private var i: Long = 0
    private var x: LongArray? = null
    private var nowpostion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_picture)
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("needstatusbar", false)) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        val bundle = this.intent.extras
        i = bundle?.getLong("illustid")!!
        if (bundle.getLongArray("illustlist") != null) {
            x = bundle.getLongArray("illustlist")
            nowpostion = x!!.indexOf(i)
        } else {
            x = LongArray(1)
            x!![0] = i
        }
        val picturePagerAdapter = PicturePagerAdapter(supportFragmentManager, x!!)
        viewpage_picture!!.adapter = picturePagerAdapter
        viewpage_picture!!.currentItem = nowpostion
        viewpage_picture.offscreenPageLimit = 5
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        viewpage_picture!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                nowpostion = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        prevBtn.setOnClickListener {
            if (nowpostion != 0)
                viewpage_picture.setCurrentItem(nowpostion - 1)
        }
        nextBtn.setOnClickListener {
            if (nowpostion < x?.size!!)
                viewpage_picture.setCurrentItem(nowpostion + 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_picture, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finishAfterTransition()
            R.id.action_share -> share()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun share() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, "https://www.pixiv.net/member_illust.php?illust_id=${x!![nowpostion]}&mode=medium")
        startActivity(Intent.createChooser(textIntent, "分享"))
    }


}
