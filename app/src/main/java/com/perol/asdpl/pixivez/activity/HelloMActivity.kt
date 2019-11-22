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
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.HelloMViewPagerAdapter
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.sql.UserEntity
import kotlinx.android.synthetic.main.app_bar_hello_m.*
import kotlinx.android.synthetic.main.content_hello_m.*
import kotlinx.coroutines.runBlocking
import java.io.File


class HelloMActivity : RinkActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 789) {
                recreate()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            3000 -> {
                val length = grantResults.size
                var reRequest = false
                for (i in 0 until length) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        reRequest = true
                    }
                }
                if (reRequest) {
                    Toast.makeText(this, "未获得授权，请自行到系统设置进行授权", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_gallery -> {
                startActivity(Intent(applicationContext, SaucenaoActivity::class.java))
            }
            R.id.nav_slideshow -> {
                clean()
            }
            R.id.nav_manage -> {
                val intent = Intent(applicationContext, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_theme -> {
                val intent = Intent(applicationContext, ThemeActivity::class.java)
                startActivityForResult(intent, 789)
            }
            R.id.nav_history -> {
                val intent = Intent(applicationContext, HistoryMActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_progress -> {
                startActivity(Intent(this@HelloMActivity, ProgressActivity::class.java))
            }
            R.id.nav_account -> {
                startActivity(Intent(this, AccountActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    var exitTime = 0L
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(
                applicationContext,
                getString(R.string.again_to_exit),
                Toast.LENGTH_SHORT
            ).show();
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var allUser = ArrayList<UserEntity>()
        runBlocking {
            allUser = ArrayList(AppDataRepository.getAllUser())

        }
        if (allUser.isEmpty()) {
            startActivity(Intent(this@HelloMActivity, LoginActivity::class.java))
            finish()
            return
        }
        ThemeUtil.themeInit(this)
        setContentView(R.layout.app_bar_hello_m)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_logo)
        toggle.isDrawerIndicatorEnabled = true
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
        viewpager_hellom.currentItem =
            PreferenceManager.getDefaultSharedPreferences(this).getString("firstpage", "0")?.toInt()
                ?: 0


        val view = nav_view.inflateHeaderView(R.layout.nav_header_hello_m)
        val imageview = view.findViewById<ImageView>(R.id.imageView)
        val headtext = view.findViewById<TextView>(R.id.headtext)
        val textView = view.findViewById<TextView>(R.id.textView)
        var nowNum = PreferenceManager.getDefaultSharedPreferences(this).getInt("usernum", 0)
        if (nowNum >= allUser.size) {
            nowNum = 0
        }
        GlideApp.with(imageview.context)
            .load(allUser[nowNum].userimage)
            .circleCrop().into(imageview)
        imageview.setOnClickListener {
            runBlocking {
                val intent = Intent(this@HelloMActivity, UserMActivity::class.java)
                intent.putExtra("data", AppDataRepository.getUser().userid)
                startActivity(intent)
            }
        }

        headtext.text = allUser[nowNum].username
        textView.text = allUser[nowNum].useremail

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
                    tabitem.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_action_ranking_white)
                }
                2 -> {
                    tabitem.icon = ContextCompat.getDrawable(this, R.drawable.ic_action_my_white)
                }
            }

        }
    }

//    private fun initData() {
//        val viewModel = ViewModelProviders.of(this).get(HelloMViewModel::class.java)
//    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.hello_m, menu)
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


    private fun clean() {
        val normalDialog = MaterialAlertDialogBuilder(this)
        normalDialog.setMessage("这将清理全部的缓存")
        normalDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
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