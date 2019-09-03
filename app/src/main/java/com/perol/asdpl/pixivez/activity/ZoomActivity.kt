package com.perol.asdpl.pixivez.activity

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ZoomPagerAdapter
import kotlinx.android.synthetic.main.activity_zoom.*
import java.util.*

class ZoomActivity : RinkActivity() {

    private var str: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom)
        val intent = intent
        val bundle = intent.extras
        str = bundle!!.getStringArrayList("url")
        val num = bundle.getInt("num", 0)
        val zoomPagerAdapter = ZoomPagerAdapter(this, str!!)
        textview_zoom.text = 1.toString() + "/" + str!!.size
        viewpage_zoom.adapter = zoomPagerAdapter
        viewpage_zoom.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                viewpage_zoom.tag = position
                val positonx = position + 1
                textview_zoom.text =  "$positonx/${str!!.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        viewpage_zoom.currentItem = num

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
