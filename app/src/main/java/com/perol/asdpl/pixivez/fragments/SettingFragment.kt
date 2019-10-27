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

package com.perol.asdpl.pixivez.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PathProviderActivity
import com.perol.asdpl.pixivez.dialog.IconDialog
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.PxEZApp
import okhttp3.*
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*


class SettingFragment : PreferenceFragmentCompat(), IconDialog.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.normal")  //拿到默认的组件
        testComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.triangle")
        mdComponent = ComponentName("com.perol.asdpl.play.pixivez", "com.perol.asdpl.pixivez.md")

    }

    private var defaultComponent: ComponentName? = null
    private var testComponent: ComponentName? = null
    private var mdComponent: ComponentName? = null
    private fun enableComponent(componentName: ComponentName) {
        Log.d("compon", componentName.packageName)
        val state = activity?.packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return
        }
        activity?.packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun disableComponent(componentName: ComponentName) {
        val state = activity?.packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return
        }
        activity?.packageManager!!.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun getCrashReportFiles(): Array<String>? {
        val filesDir = activity?.filesDir
        val filter = FilenameFilter { dir, name -> name.endsWith(".cr") }
        return filesDir?.list(filter)
    }

    override fun onClick(position: Int) {
        Toast.makeText(PxEZApp.instance, "正在尝试更换，等待启动器刷新", Toast.LENGTH_SHORT).show()
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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)
        findPreference<SwitchPreference>("disableproxy")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("storepath1")!!.apply {
            setDefaultValue(PxEZApp.storepath)
            summary = PxEZApp.storepath
        }
        findPreference<Preference>("version")!!.apply {
            try {
                // ---get the package info---
                val pm = context.packageManager
                val pi = pm.getPackageInfo(context.packageName, 0)
                summary = pi.versionName
            } catch (e: Exception) {
                Log.e("VersionInfo", "Exception", e)
            }
        }
        findPreference<ListPreference>("language")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<SwitchPreference>("r18on")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference<SwitchPreference>("animation")!!.setOnPreferenceChangeListener { preference, newValue ->
            PxEZApp.animationEnable = newValue as Boolean
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 887) {
                val path = data!!.getStringExtra("path")
                PxEZApp.storepath = path
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("storepath1", PxEZApp.storepath).apply()
                findPreference<Preference>("storepath1")!!.apply {
                    summary = path
                }

            }


        }

    }

    private fun checkUpdate() {
        val checkurl = "https://raw.githubusercontent.com/Notsfsssf/Pix-EzViewer/master/gradle.properties";
        val okHttpClient = OkHttpClient.Builder().build()
        val requests = Request.Builder()
                .url(checkurl).get().build();
        okHttpClient.newCall(request = requests).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toasty.info(PxEZApp.instance, "检查更新出错，前往项目地址更新", Toast.LENGTH_LONG).show()
                    try {
                        val uri = Uri.parse("https://github.com/Notsfsssf/Pix-EzViewer")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    val props = Properties()
                    props.load(response.body?.byteStream())
                    val versioncode = props.getProperty("VERSIONCODE")
                    Log.d("CODE", versioncode)
                    if (BuildConfig.VERSION_CODE >= versioncode.toInt()) {
                        Toasty.error(PxEZApp.instance, "未发现新版本,可以前往项目地址确认").show()
                    } else {
                        Toasty.info(PxEZApp.instance, "发现新版本,前往项目地址更新", Toast.LENGTH_LONG).show()
                        try {
                            val uri = Uri.parse("https://github.com/Notsfsssf/Pix-EzViewer")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        })


    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "me" -> {
                try {
                    val uri = Uri.parse("https://music.163.com/song?id=1335514068&userid=32973424")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                }
            }
            "check" -> {
                if (BuildConfig.ISGOOGLEPLAY) {
                    try {
                        val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.perol.asdpl.play.pixivez")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                    }
                } else
                    checkUpdate()
            }
            "storepath1" -> {
                startActivityForResult(Intent(activity, PathProviderActivity::class.java), 887)
            }
            "version" -> {
                try {
                    val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.perol.asdpl.play.pixivez")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                }
            }
            "icons" -> {
                val iconDialog = IconDialog()
                iconDialog.callback = this
                iconDialog.show(childFragmentManager)
            }
            "viewreport" -> {
                val list = getCrashReportFiles()
                var string = ""
                for (a in list!!.indices) {
                    if (a > 10) {
                        continue
                    }
                    val cr = File(activity?.filesDir, list[a])
                    string += cr.readText()
                }
                val dialogBuild = MaterialAlertDialogBuilder(activity!!)
                dialogBuild.setMessage(string).setTitle("这是崩溃报告，如果遇到个别功能闪退，请将此报告反馈给开发者")
                        .setPositiveButton(android.R.string.ok) { _, _ ->

                        }
                        .create().show()

            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}