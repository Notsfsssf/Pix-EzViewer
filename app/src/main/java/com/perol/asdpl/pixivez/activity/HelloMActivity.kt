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


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.perol.asdpl.pixivez.adapters.HelloMViewPagerAdapter
import com.perol.asdpl.pixivez.databinding.ActivityHelloMBinding
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.sql.UserEntity
import com.perol.asdpl.pixivez.viewmodel.HelloMViewModel
import kotlinx.android.synthetic.main.app_bar_hello_m.*
import kotlinx.android.synthetic.main.content_hello_m.*
import kotlinx.coroutines.runBlocking
import java.io.File


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

    var exitTime = 0L
    override fun onBackPressed() {
        if (drawer.isDrawerOpen) {
            drawer.onBackPressed()
            return
        }

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(applicationContext, getString(R.string.again_to_exit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish()
        }
    }

    private lateinit var header: AccountHeader
    private lateinit var drawer: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var allUser = ArrayList<UserEntity>()
        runBlocking {
            allUser = ArrayList<UserEntity>(AppDataRepository.getAllUser())

        }
        if (allUser.isEmpty() || allUser[0].username == "olduser") {
            startActivity(Intent(this@HelloMActivity, LoginActivity::class.java))
            finish()
            return
        }
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        ThemeUtil.themeInit(this)
        activityHelloMBinding = DataBindingUtil.setContentView(this, R.layout.app_bar_hello_m)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                val url = "https://" + uri.host + uri.path
                println(url)
                GlideApp.with(imageView).load(url).error(R.drawable.ai).optionalCenterCrop().into(imageView)
            }
        })
        val typedValue = TypedValue();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        header = AccountHeaderBuilder()
                .withActivity(this)
                .withOnAccountHeaderListener(this)
                .build()
        header.headerBackgroundView.setBackgroundColor(typedValue.data)
        drawer = DrawerBuilder().withActivity(this)
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
                                .withIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_download))
                                .withName(R.string.download_progress)
                                .withSelectable(false)
                                .withIconTintingEnabled(true)
                                .withIdentifier(888L)
                )
                .withSelectedItem(-1)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar).build()
        val toggle = ActionBarDrawerToggle(
                this, drawer.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_logo)
//        toggle.isDrawerIndicatorEnabled = false
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
                        val intent = Intent(applicationContext, ThemeActivity::class.java)
                        startActivity(intent)
//                        val option = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//                            AppCompatDelegate.MODE_NIGHT_NO
//                        } else {
//                            AppCompatDelegate.MODE_NIGHT_YES
//                        }
//                        AppCompatDelegate.setDefaultNightMode(option)
                    }
                    6 -> {
                        val intent = Intent(applicationContext, SettingActivity::class.java)
                        startActivity(intent)
                    }


                }
                if (drawerItem.identifier == 888L) {
                    startActivity(Intent(this@HelloMActivity, ProgressActivity::class.java))
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
        viewpager_hellom.currentItem = PreferenceManager.getDefaultSharedPreferences(this).getString("firstpage", "0")?.toInt()
                ?: 0
//AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Works.checkUpdate(this)
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
                for (i in it.indices) {
                    header.addProfile(
                            ProfileDrawerItem()
                                    .withName(it[i].username)
                                    .withIcon(it[i].userimage)
                                    .withEmail(it[i].useremail)
                                    .withIdentifier(i.toLong()), i)
                }
                header.setActiveProfile(sharedPreferencesServices.getInt("usernum").toLong())
            }
        } else {
            Toasty.error(this, resources.getString(R.string.conflict)).show()
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
        val normalDialog = MaterialAlertDialogBuilder(this)
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