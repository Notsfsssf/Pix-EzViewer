package com.perol.asdpl.pixivez.activity


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PicturePagerAdapter
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.responses.IllustBean
import kotlinx.android.synthetic.main.activity_picture.*
import java.util.*

class PictureActivity: RinkActivity() {

    private var i: Long = 0
    private var x: LongArray? = null
    private var nowpostion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences =SharedPreferencesServices.getInstance()
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        setContentView(R.layout.activity_picture)
        if (!sharedPreferences.getBoolean("needstatusbar")) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        val bundle = this.intent.extras

        i = bundle!!.getLong("illustid")
        if (bundle.getLongArray("illustlist") != null) {
            x = bundle.getLongArray("illustlist")
            nowpostion = x!!.indexOf(i)
        } else {
            x = LongArray(1)
            x!![0]=i
        }
        val picturePagerAdapter = PicturePagerAdapter(supportFragmentManager, x!!)
        viewpage_picture!!.offscreenPageLimit = 1
        viewpage_picture!!.adapter = picturePagerAdapter
        viewpage_picture!!.currentItem = nowpostion
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_picture, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            }else finish()
            R.id.action_share -> share()

        }
        return super.onOptionsItemSelected(item)
    }



    private fun share() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + x!![nowpostion])
        startActivity(Intent.createChooser(textIntent, "分享"))
    }


}
