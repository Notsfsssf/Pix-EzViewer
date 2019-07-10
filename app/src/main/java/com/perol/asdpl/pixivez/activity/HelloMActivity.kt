package com.perol.asdpl.pixivez.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.navigation.NavigationView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ColorfulAdapter
import com.perol.asdpl.pixivez.adapters.HelloMViewPagerAdapter
import com.perol.asdpl.pixivez.databinding.ActivityHelloMBinding
import com.perol.asdpl.pixivez.dialog.RegisterDialog
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.UserEntity
import com.perol.asdpl.pixivez.viewmodel.HelloMViewModel
import io.multimoon.colorful.Colorful
import io.multimoon.colorful.CustomThemeColor
import io.multimoon.colorful.ThemeColor
import io.multimoon.colorful.ThemeColorInterface
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hello_m.*
import kotlinx.android.synthetic.main.app_bar_hello_m.*
import kotlinx.android.synthetic.main.content_hello_m.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.io.File
import java.util.*


class HelloMActivity : RinkActivity(), NavigationView.OnNavigationItemSelectedListener {
    var activityHelloMBinding: ActivityHelloMBinding? = null
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    lateinit var appDatabase: AppDatabase
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            3000 -> {
                val length = grantResults.size
                var re_request = false
                for (i in 0 until length) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        re_request = true
                    }
                }
                if (re_request) {
                    Toast.makeText(this, "未获得授权，请自行到系统设置进行授权", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        appDatabase = AppDatabase.getInstance(this)
        val islogin = sharedPreferencesServices.getBoolean("islogin")
        if (!islogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }


        ThemeUtil.Themeinit(this)
        activityHelloMBinding = DataBindingUtil.setContentView(this, R.layout.activity_hello_m)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_logo)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setToolbarNavigationClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        checkAndRequestPermissions(permissionList)
        initView()
        initData()
        initBind()
        viewpager_hellom.currentItem = sharedPreferencesServices.getInt("firstpage")
        if (!sharedPreferencesServices.getBoolean("isreadpaper")) {
            val normalDialog = AlertDialog.Builder(this, R.style.AlertDialogCustom)
            normalDialog.setMessage("1.在图片详情页长按可以保存选定图片，长按头像快速关注作者，请给应用权限\n2.浏览动图时，点击下方播放按钮等待一定时间合成动图，完毕即可播放,合成过程内存开销相当之大，完成之前离开可能会导致问题\n3.卡片上和图片详情页显示为中图，点进手势页为大图，下载的才为原图\n4.搜索页先选择用户ID或者插画ID选项才能搜ID，并且应用支持从链接打开\n5.限制总开关在官网里，遇到无权限访问的插画，自行去网页开启，开发者不提供帮助服务，开发者并不是老好人"
            )
            normalDialog.setTitle("引导&提示")
            normalDialog.setPositiveButton("不再提示"
            ) { dialog, which ->
                sharedPreferencesServices.setBoolean("isreadpaper", true)
            }
            normalDialog.show()
        }

    }

    private fun checkAndRequestPermissions(permissionList: ArrayList<String>) {
        val list = ArrayList(permissionList)
        val it = list.iterator()

        while (it.hasNext()) {
            val permission = it.next()
            val hasPermission = ContextCompat.checkSelfPermission(this, permission)
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                it.remove()
            }

        }
        if (list.size == 0) {
            return
        }
        val permissions = list.toTypedArray()
        ActivityCompat.requestPermissions(this, permissions, 3000)


    }

    private fun initView() {
        viewpager_hellom.adapter = HelloMViewPagerAdapter(supportFragmentManager)
        viewpager_hellom.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

        })
        tablayout_hellom.setupWithViewPager(viewpager_hellom)
        for (i in 0..2) {
            val tabitem = tablayout_hellom.getTabAt(i)!!

            when (i) {
                0 -> {
                    tabitem.icon = ContextCompat.getDrawable(this, R.drawable.ic_action_home_white)
                }
                1 -> {
                    tabitem.icon = ContextCompat.getDrawable(this, R.drawable.ic_action_ranking_white)
                }
                2 -> {
                    tabitem.icon = ContextCompat.getDrawable(this, R.drawable.ic_action_my_white)
                }
            }

        }
    }


    private fun initData() {
        val viewmodel = ViewModelProviders.of(this).get(HelloMViewModel::class.java)
        viewmodel.userBean.observe(this, androidx.lifecycle.Observer {
            userBean(it)
        })
        viewmodel.first()
    }

    private fun userBean(it: UserEntity?) {
        if (it != null) {
            val header = nav_view.getHeaderView(0)
            GlideApp.with(this).load(it.userimage).circleCrop().into(header.findViewById<ImageView>(R.id.imageView).apply {
                setOnClickListener {
                    val mainimage = header!!.findViewById<ImageView>(R.id.imageView)
                    val optionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@HelloMActivity, mainimage, "userimage");
                    val intent = Intent(applicationContext, UserMActivity::class.java)
                    val userid = sharedPreferencesServices.getString("userid").toLong()
                    intent.putExtra("data", userid)
                    startActivity(intent, optionsCompat.toBundle())
                }
            })
            header.findViewById<TextView>(R.id.headtext).text = it.username
            header.findViewById<TextView>(R.id.textView).text = it.useremail
        } else {
            toast(resources.getString(R.string.conflict))
        }

    }

    private fun initBind() {
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.hello_m, menu)

        if (sharedPreferencesServices.getBoolean("isnone")) {
            val menuItem = nav_view.menu.findItem(R.id.nav_regist)
            menuItem.isVisible = true

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SearchMActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_gallery -> {
                startActivity(Intent(applicationContext, SaucenaoActivity::class.java))
            }
            R.id.nav_slideshow -> {
                clearn()
            }
            R.id.nav_manage -> {
                val intent = Intent(applicationContext, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_theme -> {
                val list = ArrayList<ThemeColorInterface>().also {
                    val myCustomColor1 = CustomThemeColor(
                            this,
                            R.style.bili_primary_color,
                            R.style.bili_primary_dark_color,
                            R.color.pink, // <= use the color you defined in my_custom_primary_color
                            R.color.pink // <= use the color you defined in my_custom_primary_dark_color
                    )
                    val myCustomColor2 = CustomThemeColor(
                            this,
                            R.style.blue_primary_color,
                            R.style.blue_primary_dark_color,
                            R.color.blue, // <= use the color you defined in my_custom_primary_color
                            R.color.blue // <= use the color you defined in my_custom_primary_dark_color
                    )
                    it+=ThemeColor.BLUE
                    it+=ThemeColor.AMBER
                    it+=ThemeColor.GREEN
                    it+=ThemeColor.PINK
                    it+=ThemeColor.PURPLE
                    it+=ThemeColor.BLUE_GREY
                    it+=ThemeColor.ORANGE
                    it+=ThemeColor.RED
                    it+=ThemeColor.TEAL
                    it+=ThemeColor.LIGHT_BLUE
                    it+=ThemeColor.LIGHT_GREEN
                    it += myCustomColor1
                    it += myCustomColor2
                }
                alert {
                    title = "选择主题"
                    customView {
                        verticalLayout {
                            recyclerView {
                                layoutManager = GridLayoutManager(this@HelloMActivity,4)
                                adapter = ColorfulAdapter(R.layout.view_colorfulitem, list).apply {
                                    onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                                        sharedPreferencesServices.setInt("colornum", position)
                                        Colorful().edit().setPrimaryColor(list[position]).apply(applicationContext) {
                                            recreate()
                                        }
                                    }
                                }
                            }.lparams(height = dip(0), width = matchParent) {
                                topMargin = dip(20)
                                weight=5f
                            }
                            switch {
                                hint = "Dark"
                                isChecked = Colorful().getDarkTheme()
                               setOnCheckedChangeListener { compoundButton, b ->
                                   Colorful().edit().setDarkTheme(isChecked).apply(this@HelloMActivity.applicationContext).apply {
                                       recreate()
                                   }
                               }
                            }.lparams(height = dip(0), width = matchParent){
                                weight=1f
                                marginStart=dip(8)
                                marginEnd=dip(8)
                            }
                        }
                    }

                }.show()
            }
            R.id.nav_history -> {
                val intent = Intent(applicationContext, HistoryMActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                val url = "https://github.com/Notsfsssf"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.nav_send -> {
                sharedPreferencesServices.setBoolean("islogin", false)
                Observable.just(1).observeOn(Schedulers.io()).subscribe({
                    appDatabase.userDao().deletehistory()
                }, {}, {})
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.nav_regist -> {
                val registerDialog = RegisterDialog()
                registerDialog.show(supportFragmentManager)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun clearn() {
        val normalDialog = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        normalDialog.setMessage("这将清理全部的缓存")
        normalDialog.setPositiveButton("确定"
        ) { dialog, which ->
            //...To-do
            Thread(Runnable {
                GlideApp.get(applicationContext).clearDiskCache()
                deleteDir(applicationContext.cacheDir)
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    deleteDir(applicationContext.externalCacheDir)
                }
            }).start()
        }


        normalDialog.show()
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

}