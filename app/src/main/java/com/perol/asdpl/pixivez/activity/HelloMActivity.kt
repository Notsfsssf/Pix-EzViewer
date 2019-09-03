package com.perol.asdpl.pixivez.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ColorfulAdapter
import com.perol.asdpl.pixivez.adapters.HelloMViewPagerAdapter
import com.perol.asdpl.pixivez.databinding.ActivityHelloMBinding
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.sql.UserEntity
import com.perol.asdpl.pixivez.viewmodel.HelloMViewModel
import io.multimoon.colorful.Colorful
import io.multimoon.colorful.CustomThemeColor
import io.multimoon.colorful.ThemeColor
import io.multimoon.colorful.ThemeColorInterface
import kotlinx.android.synthetic.main.app_bar_hello_m.*
import kotlinx.android.synthetic.main.content_hello_m.*
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.io.File
import java.lang.Runnable
import kotlin.collections.ArrayList


class HelloMActivity : RinkActivity(), Drawer.OnDrawerNavigationListener, AccountHeader.OnAccountHeaderListener {
    override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
        when (profile.identifier) {
            -100L -> {
                startActivity(Intent(this, AccountActivity::class.java))
            }
            else -> {
                sharedPreferencesServices.setInt("usernum", profile.identifier.toInt())
                recreate()
            }
        }
        return true
    }

    override fun onNavigationClickListener(clickedView: View): Boolean {
        return false
    }

    private var activityHelloMBinding: ActivityHelloMBinding? = null
    lateinit var sharedPreferencesServices: SharedPreferencesServices
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

    private lateinit var header: AccountHeader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var allUser = ArrayList<UserEntity>()
        runBlocking {
            allUser = AppDataRepository.getAllUser() as ArrayList<UserEntity>

        }
        if (allUser.isEmpty()||allUser[0].username == "olduser") {
            startActivity(Intent(this@HelloMActivity, LoginActivity::class.java))
            finish()
            return
        }

        ThemeUtil.themeInit(this)
        activityHelloMBinding = DataBindingUtil.setContentView(this, R.layout.app_bar_hello_m)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {


            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                val url="https://"+uri.host+uri.path
                println(url)
                GlideApp.with(imageView).load(url).error(R.drawable.ai).optionalCenterCrop().into(imageView)
            }
        })
        header = AccountHeaderBuilder()
                .withActivity(this)
                .withOnAccountHeaderListener(this)
                .build()
        AppCompatDrawableManager.get().getDrawable(this, R.drawable.ic_action_my_white)
        val drawer = DrawerBuilder().withActivity(this)
                .withAccountHeader(header)
                .addDrawerItems(
                        PrimaryDrawerItem()
                                .withIdentifier(6)
                                .withName(R.string.mypage)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_my_white))
                                .withIconTintingEnabled(true),
                        PrimaryDrawerItem()
                                .withIdentifier(0)
                                .withName(R.string.searchsource)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_logo))
                                .withIconTintingEnabled(true),
                        PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName(R.string.cleancatche)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_lajitong))
                                .withIconTintingEnabled(true),
                        PrimaryDrawerItem()
                                .withIdentifier(3)
                                .withName(R.string.historyrecord)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_shijian))
                                .withIconTintingEnabled(true),
                        PrimaryDrawerItem()
                                .withIdentifier(4)
                                .withName(R.string.themecoloful)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_theme))
                                .withIconTintingEnabled(true),
                        PrimaryDrawerItem()
                                .withIdentifier(5)
                                .withName(R.string.appsetting)
                                .withSelectable(false)
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_share_setting))
                                .withIconTintingEnabled(true)
                )
                .addStickyDrawerItems(
                        PrimaryDrawerItem()
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_share))
                                .withName("About")
                                .withSelectable(false)
                                .withIconTintingEnabled(true)
                                .withIdentifier(888L)
                )
                .withSelectedItem(-1)
                .withTranslucentStatusBar(false)
                .withTranslucentNavigationBar(false)
                .withToolbar(toolbar).build()
        val toggle = ActionBarDrawerToggle(
                this, drawer.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_logo)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setToolbarNavigationClickListener {
            drawer.drawerLayout.openDrawer(GravityCompat.START)
        }
        drawer.drawerLayout.addDrawerListener(toggle)
        drawer.onDrawerNavigationListener = this
        drawer.onDrawerItemClickListener = object : Drawer.OnDrawerItemClickListener {
            override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                Log.d("position", position.toString())

                when (position) {
                    1 -> {
                        runBlocking {
                            val intent = Intent(this@HelloMActivity, UserMActivity::class.java)
                            intent.putExtra("data", AppDataRepository.getUser().userid)
                            startActivity(intent)

                        }
                        return true
                    }
                    2 -> {
                        startActivity(Intent(applicationContext, SaucenaoActivity::class.java))
                    }
                    3 -> {
                        clearn()
                    }
                    4 -> {
                        val intent = Intent(applicationContext, HistoryMActivity::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        val list = ArrayList<ThemeColorInterface>().also {
                            val myCustomColor1 = CustomThemeColor(
                                    this@HelloMActivity,
                                    R.style.bili_primary_color,
                                    R.style.bili_primary_dark_color,
                                    R.color.pink, // <= use the color you defined in my_custom_primary_color
                                    R.color.pink // <= use the color you defined in my_custom_primary_dark_color
                            )
                            val myCustomColor2 = CustomThemeColor(
                                    this@HelloMActivity,
                                    R.style.blue_primary_color,
                                    R.style.blue_primary_dark_color,
                                    R.color.blue, // <= use the color you defined in my_custom_primary_color
                                    R.color.blue // <= use the color you defined in my_custom_primary_dark_color
                            )
                            it += ThemeColor.BLUE
                            it += ThemeColor.AMBER
                            it += ThemeColor.GREEN
                            it += ThemeColor.PINK
                            it += ThemeColor.PURPLE
                            it += ThemeColor.BLUE_GREY
                            it += ThemeColor.ORANGE
                            it += ThemeColor.RED
                            it += ThemeColor.TEAL
                            it += ThemeColor.LIGHT_BLUE
                            it += ThemeColor.LIGHT_GREEN
                            it += myCustomColor1
                            it += myCustomColor2
                        }
                        alert {
                            title = "选择主题"
                            customView {
                                verticalLayout {
                                    recyclerView {
                                        layoutManager = GridLayoutManager(this@HelloMActivity, 4)
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
                                        weight = 5f
                                    }
                                    switch {
                                        hint = "Dark"
                                        isChecked = Colorful().getDarkTheme()
                                        setOnCheckedChangeListener { compoundButton, b ->
                                            Colorful().edit().setDarkTheme(isChecked).apply(this@HelloMActivity.applicationContext).apply {
                                                recreate()
                                            }
                                        }
                                    }.lparams(height = dip(0), width = matchParent) {
                                        weight = 1f
                                        marginStart = dip(8)
                                        marginEnd = dip(8)
                                    }
                                }
                            }

                        }.show()
                    }
                    6 -> {
                        val intent = Intent(applicationContext, AboutActivity::class.java)
                        startActivity(intent)
                    }


                }
                if (drawerItem.identifier == 888L) {
               startActivity(Intent(this@HelloMActivity,AboutXActivity::class.java))
                }
                return true
            }

        }
        toggle.syncState()
        val permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        checkAndRequestPermissions(permissionList)
        initView()
        initData()
        viewpager_hellom.currentItem = sharedPreferencesServices.getInt("firstpage")


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

    var longList = ArrayList<Long>()
    private fun userBean(it: List<UserEntity>) {
        if (it.isNotEmpty()) {
            val profileSettingDrawerItem = ProfileSettingDrawerItem()
                    .withName(R.string.setting)
                    .withIdentifier(-100L)
                    .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_share_setting))
                    .withIconTinted(true)

            header.clear()
            header.addProfile(profileSettingDrawerItem, header.profiles?.size ?: 0)
            Log.d("profile:", sharedPreferencesServices.getInt("usernum").toLong().toString())


            runBlocking {
                it.forEachWithIndex { index, it ->

                    header.addProfile(
                            ProfileDrawerItem()
                                    .withName(it.username)
                                    .withIcon(it.userimage)
                                    .withEmail(it.useremail)
                                    .withIdentifier(index.toLong()), index
                    )
                }
                header.setActiveProfile(sharedPreferencesServices.getInt("usernum").toLong())
            }
        } else {
            toast(resources.getString(R.string.conflict))
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.hello_m, menu)

//        if (sharedPreferencesServices.getBoolean("isnone")) {
//            val menuItem = nav_view.menu.findItem(R.id.nav_regist)
//            menuItem.isVisible = true
//
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SearchRActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun clearn() {
        val normalDialog = AlertDialog.Builder(this)
        normalDialog.setMessage("这将清理全部的缓存")
        normalDialog.setPositiveButton("确定"
        ) { dialog, which ->
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