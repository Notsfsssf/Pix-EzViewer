package com.perol.asdpl.pixivez.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.dialog.IconDialog
import com.perol.asdpl.pixivez.dialog.ThanksDialog
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.*

class AboutActivity : RinkActivity(), IconDialog.Callback, TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (tab!!.position) {
            0 -> {

            }
            1 -> {
                val thanksDialog = ThanksDialog()
                thanksDialog.show(supportFragmentManager)
            }
            2 -> {
                alert {
                    title = "如果你能够正常进入图片详情页,就不用打开这个设置"
                    customView {
                        relativeLayout {
                            switch {
                                hint = "addflag"
                                isChecked = PxEZApp.isaddflag
                                setOnCheckedChangeListener { buttonView, isChecked ->
                                    sharedPreferencesServices!!.setBoolean("isaddflag", isChecked)
                                    PxEZApp.isaddflag = isChecked
                                }

                            }.lparams() {
                                margin = dip(16)
                            }
                        }
                    }
                }.show()
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab!!.position) {
            0 -> {

            }
            1 -> {
                val thanksDialog = ThanksDialog()
                thanksDialog.show(supportFragmentManager)
            }
            2 -> {
                alert {
                    title = "如果你能够正常进入图片详情页,就不用打开这个设置"
                    customView {
                        relativeLayout {
                            switch {
                                hint = "addflag"
                                isChecked = PxEZApp.isaddflag
                                setOnCheckedChangeListener { buttonView, isChecked ->
                                    sharedPreferencesServices!!.setBoolean("isaddflag", isChecked)
                                    PxEZApp.isaddflag = isChecked
                                }

                            }.lparams() {
                                margin = dip(16)
                            }
                        }
                    }
                }.show()
            }
        }

    }

    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var versionName: String? = null
    private var defaultComponent: ComponentName? = null
    private var testComponent: ComponentName? = null
    private var mdComponent: ComponentName? = null
    private var path = PxEZApp.storepath
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 887) {
                path = data!!.getStringExtra("path")
                PxEZApp.storepath = path
                textView_storepath.text = path
                sharedPreferencesServices!!.setString("storepath", path)
                Toasty.success(applicationContext, path, Toast.LENGTH_SHORT).show()

            }
            if (requestCode == 888) {
                path = data!!.getStringExtra("path")
                textView_storepath2.text = path
                sharedPreferencesServices!!.setString("storepath1", path)
                Toasty.success(applicationContext, path, Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun enableComponent(componentName: ComponentName) {
        Log.d("compon", componentName.packageName)
        val state = packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return
        }
        packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun disableComponent(componentName: ComponentName) {
        val state = packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return
        }
        packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_language -> {
                val languages = listOf("zh", "en", "zh-rHK")
                selector("language", languages) { dialogInterface, i ->
                    if (sharedPreferencesServices != null)
                        sharedPreferencesServices!!.setInt("language", i);
                    toast("Please restart app")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        setContentView(R.layout.activity_about)



        defaultComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.normal")  //拿到默认的组件
        testComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.triangle")
        mdComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.md")
        GlideApp.with(this).load(R.drawable.er).circleCrop().into(pic_image)
        checkforupdate.setOnClickListener { v -> checkforupdate(v) }
        sharedPreferencesServices = SharedPreferencesServices.getInstance()

        switch_r18.isChecked = sharedPreferencesServices!!.getBoolean("r18on")
        switch_proxy.isChecked = sharedPreferencesServices!!.getBoolean("disableproxy")

        textView_storepath.text = PxEZApp.storepath
        textView_storepath2.text = if (sharedPreferencesServices!!.getString("storepath1") == null) {
            "未指定"
        } else {
            sharedPreferencesServices!!.getString("storepath1")
        }
        switch_liuhai.isChecked = sharedPreferencesServices!!.getBoolean("needstatusbar")
        switch_liuhai.setOnCheckedChangeListener({ compoundButton, b -> sharedPreferencesServices!!.setBoolean("needstatusbar", b) })

        textView_storepath2.setOnClickListener {
            val builder = AlertDialog.Builder(this@AboutActivity, R.style.AlertDialogCustom)
            builder.setTitle(this.getString(R.string.savepath))
                    .setMessage("当前：" + sharedPreferencesServices!!.getString("storepath1") + "\n${resources.getString(R.string.onlysupportinternalstorage)}")
                    .setNegativeButton(resources.getString(R.string.internal)) { dialog, id ->
                        startActivityForResult(Intent(this, PathProviderActivity::class.java), 888)
                    }.show()
        }
        textView_storepath.setOnClickListener {
            val builder = AlertDialog.Builder(this@AboutActivity, R.style.AlertDialogCustom)
            builder.setTitle(this.getString(R.string.savepath))
                    .setMessage("当前：" + PxEZApp.storepath + "\n${resources.getString(R.string.onlysupportinternalstorage)}")
                    .setNegativeButton(this.getString(R.string.internal)) { dialog, id ->
                        startActivityForResult(Intent(this, PathProviderActivity::class.java), 887)
                    }


            val alertDialog = builder.create()
            alertDialog.show()
        }
        changeicon.setOnClickListener {
            val iconDialog = IconDialog()
            iconDialog.callback = this@AboutActivity
            iconDialog.show(supportFragmentManager)
        }
        switch_r18.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sharedPreferencesServices!!.setBoolean("r18on", true)
                Toast.makeText(applicationContext, "H是可以的！ԅ(¯﹃¯ԅ) ", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferencesServices!!.setBoolean("r18on", false)
                Toast.makeText(applicationContext, "H是不行的！ (￣^￣) ", Toast.LENGTH_SHORT).show()
            }
        }
        switch_proxy.setOnCheckedChangeListener { compoundButton, b ->
            sharedPreferencesServices!!.setBoolean("disableproxy", b)
            Toast.makeText(applicationContext, "重启应用后生效", Toast.LENGTH_SHORT).show()
        }
        setSupportActionBar(toobar_about)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val pkName = "com.perol.asdpl.play.pixivez"
        try {
            versionName = this.packageManager.getPackageInfo(
                    pkName, 0).versionName
            versionname.text = versionName
        } catch (e: PackageManager.NameNotFoundException) {
            versionname.text = "Beta"
        }
        tablayout_top.addOnTabSelectedListener(this)
        textview_support.movementMethod = LinkMovementMethod.getInstance()

        spinner_chushi.setSelection(sharedPreferencesServices!!.getInt("firstpage"), true)
        spinner_chushi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                sharedPreferencesServices!!.setInt("firstpage", i)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
        spinner_format.setSelection(sharedPreferencesServices!!.getInt("saveformat"))
        spinner_format.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferencesServices!!.setInt("saveformat", position)
                PxEZApp.saveformat = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        donation_botton.setOnClickListener {
            val uri = Uri.parse("https://github.com/Notsfsssf/Pix-EzViewer/blob/master/donation/README.md");
            startActivity(Intent(Intent.ACTION_VIEW, uri));
        }
        quality.setSelection(sharedPreferencesServices!!.getInt("quality"))
        quality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPreferencesServices!!.setInt("quality", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        textview_weixin.setOnClickListener {
            alert {
                customView {
                    imageView {
                        imageResource = R.drawable.weixinqr
                    }
                    noButton { }
                }

            }.show()
        }
        bottompic.setOnClickListener {
            val uri = Uri.parse("https://music.163.com/#/song?id=28377223&userid=32973424");
            val intent = Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    fun openapp() {
        val builder = AlertDialog.Builder(this@AboutActivity, R.style.AlertDialogCustom)
        builder.setTitle("选择储存位置")
                .setNegativeButton("取消") { dialog, id ->

                }
                .setPositiveButton("确认") { dialog, id ->

                }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun checkforupdate(v: View) {

   Beta.checkUpgrade();

    }

    fun jumpto(v: View) {

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//
//        menuInflater.inflate(R.menu.about, menu)
//
//        return true
//    }


    override fun onClick(position: Int) {

        Toast.makeText(applicationContext, "正在尝试更换，等待启动器刷新", Toast.LENGTH_SHORT).show()
        when (position) {
            0 -> {
                enableComponent(defaultComponent!!)
                disableComponent(testComponent!!)
                disableComponent(mdComponent!!)
            }
            1 -> {
                enableComponent(testComponent!!)
                disableComponent(defaultComponent!!)
                disableComponent(mdComponent!!)
            }
            2 -> {
                enableComponent(mdComponent!!)
                disableComponent(defaultComponent!!)
                disableComponent(testComponent!!)
            }
        }


    }
}
