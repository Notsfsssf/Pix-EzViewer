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

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.databinding.DialogMeBinding
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.PxEZApp
import com.tencent.bugly.beta.Beta
import java.io.File
import java.io.FilenameFilter

class SettingFragment : PreferenceFragmentCompat() {
    private val storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        defaultComponent =
            ComponentName(requireContext().packageName, "com.perol.asdpl.pixivez.normal")
        testComponent =
            ComponentName(requireContext().packageName, "com.perol.asdpl.pixivez.triangle")
        mdComponent = ComponentName(requireContext().packageName, "com.perol.asdpl.pixivez.md")
    }

    lateinit var defaultComponent: ComponentName
    lateinit var testComponent: ComponentName
    lateinit var mdComponent: ComponentName
    private fun enableComponent(componentName: ComponentName) {
        Log.d("compon", componentName.packageName)
        val state = activity?.packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return
        }
        activity?.packageManager!!.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun disableComponent(componentName: ComponentName) {
        val state = activity?.packageManager!!.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return
        }
        activity?.packageManager!!.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun getCrashReportFiles(): Array<String>? {
        val filesDir = activity?.filesDir
        val filter = FilenameFilter { dir, name -> name.endsWith(".cr") }
        return filesDir?.list(filter)
    }

    private fun onClick(position: Int) {
        Toast.makeText(PxEZApp.instance, "正在尝试更换，等待启动器刷新", Toast.LENGTH_SHORT).show()
        when (position) {
            0 -> {
                enableComponent(defaultComponent)
                disableComponent(testComponent)
                disableComponent(mdComponent)
            }
            1 -> {
                enableComponent(testComponent)
                disableComponent(defaultComponent)
                disableComponent(mdComponent)
            }
            2 -> {
                enableComponent(mdComponent)
                disableComponent(defaultComponent)
                disableComponent(testComponent)
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)
        findPreference<SwitchPreference>("disableproxy")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT)
                .show()
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
            Snackbar.make(requireView(), getString(R.string.needtorestart), Snackbar.LENGTH_SHORT)
                .setAction(R.string.restart_now) {
                    PxEZApp.language = newValue.toString().toInt()
                    PxEZApp.ActivityCollector.recreate()
                }
                .show()
            true
        }
        findPreference<SwitchPreference>("show_user_img_main")!!.setOnPreferenceChangeListener { preference, newValue ->
            Snackbar.make(requireView(), getString(R.string.needtorestart), Snackbar.LENGTH_SHORT)
                .setAction(R.string.restart_now) {
                    PxEZApp.ActivityCollector.recreate()
                }
                .show()
            true
        }
        findPreference<SwitchPreference>("use_new_banner")!!.setOnPreferenceChangeListener { preference, newValue ->
            Snackbar.make(requireView(), getString(R.string.needtorestart), Snackbar.LENGTH_SHORT)
                .setAction(R.string.restart_now) {
                    PxEZApp.ActivityCollector.recreate()
                }
                .show()
            true
        }

        findPreference<SwitchPreference>("r18on")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT)
                .show()
            true
        }
        findPreference<SwitchPreference>("resume_unfinished_task")!!.setOnPreferenceChangeListener { preference, newValue ->
            Toasty.normal(PxEZApp.instance, getString(R.string.needtorestart), Toast.LENGTH_SHORT)
                .show()
            true
        }
        findPreference<SwitchPreference>("animation")!!.setOnPreferenceChangeListener { preference, newValue ->
            PxEZApp.animationEnable = newValue as Boolean
            true
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//
//            if (requestCode == 887) {
//                val path = data!!.getStringExtra("path")
//                PxEZApp.storepath = path
//                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("storepath1", PxEZApp.storepath).apply()
//                findPreference<Preference>("storepath1")!!.apply {
//                    summary = path
//                }
//
//            }
//
//
//        }
//
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (requireContext().allPermissionsGranted(storagePermissions)) {
                showDirectorySelectionDialog()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "me" -> {
                try {

                    val binding = DialogMeBinding.inflate(layoutInflater)
                    val dialog = MaterialDialog(requireContext(), BottomSheet()).show {
                        cornerRadius(16f)
                        customView(view = binding.root)

                    }
                    binding.bg.setOnClickListener {
                        val url = if (BuildConfig.ISGOOGLEPLAY) {
                            "https://youtu.be/Wu4fVGsEn8s"
                        } else {
                            "https://www.bilibili.com/video/BV1E741137mf"
                        }
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }


                } catch (e: Exception) {

                }
            }
            "me0" -> {
                        val url = "https://github.com/ultranity"
                        val uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
            }
            "check" -> {
                if (BuildConfig.ISGOOGLEPLAY) {
                    try {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=com.perol.asdpl.play.pixivez")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Beta.checkUpgrade()
            }
            "storepath1" -> {
//                startActivityForResult(Intent(activity, PathProviderActivity::class.java), 887)

                if (requireContext().allPermissionsGranted(storagePermissions)) {
                    showDirectorySelectionDialog()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        storagePermissions,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }
            "saveformat" -> {

            }
            "version" -> {
                if (BuildConfig.ISGOOGLEPLAY) {
                    try {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=com.perol.asdpl.play.pixivez")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    val url = "https://github.com/ultranity/Pix-EzViewer"
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            "icons" -> {
                showApplicationIconReplacementDialog()
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
                val dialogBuild = MaterialAlertDialogBuilder(requireActivity())
                dialogBuild.setMessage(string).setTitle("这是崩溃报告，如果遇到个别功能闪退，请将此报告反馈给开发者")
                    .setPositiveButton(android.R.string.ok) { _, _ ->

                    }
                    .create().show()

            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun showDirectorySelectionDialog() {
        MaterialDialog(requireContext()).show {
            title(R.string.title_save_path)
            folderChooser(allowFolderCreation = true) { _, folder ->
                with(folder.absolutePath) {
                    PxEZApp.storepath = this
                    PreferenceManager.getDefaultSharedPreferences(activity).apply {
                        putString("storepath1", PxEZApp.storepath)
                    }
                    findPreference<Preference>("storepath1")!!.apply {
                        summary = this@with
                    }
                }
            }
            cornerRadius(2.0F)
            negativeButton(android.R.string.cancel)
            positiveButton(R.string.action_select)
            lifecycleOwner(this@SettingFragment)
        }
    }

    private fun showApplicationIconReplacementDialog() {
        val items = listOf(
            BasicGridItem(R.mipmap.ic_launcher, "MD"),
            BasicGridItem(R.mipmap.ic_launcherep, "Triangle"),
            BasicGridItem(R.mipmap.ic_launchermd, "Probe")
        )//my bad

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.title_change_icon)
            gridItems(items) { _, index, _ ->
                onClick(index)
            }
            cornerRadius(16.0F)
            negativeButton(android.R.string.cancel)
            positiveButton(R.string.action_change)
            lifecycleOwner(this@SettingFragment)
        }
    }

    private fun Context.allPermissionsGranted(permissions: Array<String>): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

    private inline fun SharedPreferences.apply(modifier: SharedPreferences.Editor.() -> Unit) {
        edit().apply { modifier() }.run { apply() }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1
    }
}
