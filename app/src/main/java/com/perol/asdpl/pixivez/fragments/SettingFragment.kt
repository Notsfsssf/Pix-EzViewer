package com.perol.asdpl.pixivez.fragments

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.bumptech.glide.Glide
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PathProviderActivity
import com.perol.asdpl.pixivez.dialog.IconDialog
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlinx.android.synthetic.main.preference_author.*
import java.io.File
import java.io.FilenameFilter


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
                val dialogBuild = AlertDialog.Builder(activity!!)
                dialogBuild.setMessage(string).setTitle("这是崩溃报告，如果遇到个别功能闪退，请将此报告反馈给开发者")
                        .setPositiveButton(android.R.string.ok) { _, _ ->

                        }
                        .create().show()

            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}