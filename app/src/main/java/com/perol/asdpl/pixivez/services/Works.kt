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

package com.perol.asdpl.pixivez.services

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.work.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.SettingActivity
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.works.ImgDownLoadWorker
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*

class Works {
    companion object {
        private fun String.toLegal(): String {
            return this.replace("/", "").replace("\\", "").replace(":", "")
        }

        fun imageDownloadWithFile(illust: Illust, file: File, part: Int?) {
            var url = ""
            val title = illust.title.toLegal()
            val userName = illust.user.name.toLegal()
            val user = illust.user.id
            val name = illust.id
            val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance);
            val format = pre.getString(
                "saveformat",
                "0"
            )?.toInt()
                ?: 0
            val needCreateFold = pre.getBoolean("needcreatefold", false)
            var type = ".png"
            var filename = "${name}_p$part$type"
            if (part != null && illust.meta_pages.isNotEmpty()) {
                url = illust.meta_pages[part].image_urls.original
                type = if (url.contains("png")) {
                    ".png"
                } else ".jpg"
                when (format) {
                    0 -> {
                        filename = "${name}_$part$type"
                    }
                    1 -> {
                        filename = "${name}_p$part$type"
                    }
                    2 -> {
                        filename = "${user}_${name}_$part$type"
                    }
                    3 -> {
                        filename = "${name}_${title}_$part$type"
                    }
                }
            } else {
                url = illust.meta_single_page.original_image_url!!
                type = if (url.contains("png")) {
                    ".png"
                } else ".jpg"
                when (format) {
                    0 -> {
                        filename = "$name$type"
                    }
                    1 -> {
                        filename = "$name$type"
                    }
                    2 -> {
                        filename = "${user}_$name$type"
                    }
                    3 -> {
                        filename = "${name}_${title}$type"
                    }
                }
            }
            val path = if (needCreateFold) {
                "${PxEZApp.storepath}/$userName"
            } else PxEZApp.storepath
            val targetFile = File(path, filename)
            try {
                file.copyTo(targetFile, overwrite = true)
                Toasty.success(
                    PxEZApp.instance,
                    PxEZApp.instance.resources.getString(R.string.savesuccess),
                    Toast.LENGTH_SHORT
                ).show()
                MediaScannerConnection.scanFile(
                    PxEZApp.instance,
                    arrayOf(targetFile.path),
                    arrayOf(MimeTypeMap.getSingleton().getMimeTypeFromExtension(targetFile.extension))
                ) { _, _ ->

                }
            } catch (e: Exception) {

            }

        }

        fun imageDownloadAll(illust: Illust) {
            TToast.startDownload(PxEZApp.instance)
            if (illust.meta_pages.isEmpty()) {
                imageDownloadOne(illust, null)
            } else {
                for (i in illust.meta_pages.indices) {
                    imageDownloadOne(illust, i)
                }
            }
        }

        fun imageDownloadOne(illust: Illust, part: Int?) {
            var url = ""
            val title = illust.title.toLegal()
            val userName = illust.user.name.toLegal()
            val user = illust.user.id
            val name = illust.id
            val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance);
            val format = pre.getString(
                "saveformat",
                "0"
            )?.toInt()
                ?: 0
            val needCreateFold = pre.getBoolean("needcreatefold", false)
            var type = ".png"
            var filename = "${name}_p$part$type"
            if (part != null && illust.meta_pages.isNotEmpty()) {
                url = illust.meta_pages[part].image_urls.original
                type = if (url.contains("png")) {
                    ".png"
                } else ".jpg"
                when (format) {
                    0 -> {
                        filename = "${name}_$part$type"
                    }
                    1 -> {
                        filename = "${name}_p$part$type"
                    }
                    2 -> {
                        filename = "${user}_${name}_$part$type"
                    }
                    3 -> {
                        filename = "${name}_${title}_$part$type"
                    }
                }
            } else {
                url = illust.meta_single_page.original_image_url!!
                type = if (url.contains("png")) {
                    ".png"
                } else ".jpg"
                when (format) {
                    0 -> {
                        filename = "$name$type"
                    }
                    1 -> {
                        filename = "$name$type"
                    }
                    2 -> {
                        filename = "${user}_$name$type"
                    }
                    3 -> {
                        filename = "${name}_${title}$type"
                    }
                }
            }

            val inputData = workDataOf(
                "file" to filename,
                "url" to url,
                "title" to illust.title,
                "id" to illust.id,
                "username" to userName,
                "needcreatefold" to needCreateFold
            )
            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ImgDownLoadWorker>()
                .setInputData(inputData)
                .addTag("image")
                .build()
            WorkManager.getInstance(PxEZApp.instance)
                .enqueueUniqueWork(url, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)


            WorkManager.getInstance(PxEZApp.instance).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                .observeForever(object : Observer<WorkInfo> {
                    override fun onChanged(workInfo: WorkInfo?) {
                        if (workInfo == null) {
                            return
                        }
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            if (workInfo.outputData.getBoolean("exist", false)) {
                                Toasty.success(
                                    PxEZApp.instance,
                                    PxEZApp.instance.resources.getString(R.string.alreadysaved),
                                    Toast.LENGTH_SHORT
                                ).show()
                                WorkManager.getInstance(PxEZApp.instance)
                                    .getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                                    .removeObserver(this)
                                return
                            }
                            Toasty.success(
                                PxEZApp.instance,
                                PxEZApp.instance.resources.getString(R.string.savesuccess),
                                Toast.LENGTH_SHORT
                            ).show()
                            val path = workInfo.outputData.getString("path")
                            if (path != null)
                                MediaScannerConnection.scanFile(
                                    PxEZApp.instance,
                                    arrayOf(path),
                                    arrayOf(
                                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                            File(path).extension
                                        )
                                    )
                                ) { _, _ ->

                                }
                        } else if (workInfo.state == WorkInfo.State.FAILED) {

                        } else if (workInfo.state == WorkInfo.State.CANCELLED) {

                            val file = File(PxEZApp.storepath, filename)
                            file.deleteOnExit()
                        }
                        WorkManager.getInstance(PxEZApp.instance)
                            .getWorkInfoByIdLiveData(oneTimeWorkRequest.id).removeObserver(this)
                    }
                })
        }

        fun checkUpdate(activty: Activity) {
            val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activty)
            if (com.perol.asdpl.pixivez.BuildConfig.ISGOOGLEPLAY || PxEZApp.autochecked || !defaultSharedPreferences.getBoolean(
                    "autocheck",
                    true
                )
            ) {
                return
            }
            PxEZApp.autochecked = true
            val checkurl =
                "https://raw.githubusercontent.com/Notsfsssf/Pix-EzViewer/master/gradle.properties";
            val okHttpClient = OkHttpClient.Builder().build()
            val requests = Request.Builder()
                .url(checkurl).get().build();
            okHttpClient.newCall(request = requests).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val props = Properties()
                        props.load(response.body?.byteStream())
                        val versioncode = props.getProperty("VERSIONCODE")
                        val versionName = props.getProperty("VERSIONNAME")

                        val pm = activty.packageManager;
                        val packageInfo = pm.getPackageInfo(activty.packageName, 0);
                        val versioncodeP = packageInfo.versionCode
                        Log.d("CODE", versioncode + versioncodeP)
                        if (versioncodeP >= versioncode.toInt() || defaultSharedPreferences.getString(
                                "ignoreversion",
                                ""
                            ) == versioncode
                        ) {

                        } else {
                            activty.runOnUiThread {
                                try {
                                    val dialogs =
                                        MaterialAlertDialogBuilder(activty).setTitle("发现新版本")
                                            .setMessage(versionName)
                                            .setPositiveButton("前往更新") { i, j ->
                                                try {
                                                    val uri =
                                                        Uri.parse("https://github.com/Notsfsssf/Pix-EzViewer")
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    activty.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toasty.info(
                                                        PxEZApp.instance,
                                                        "no browser found",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }.setNegativeButton("提醒设置") { i, j ->
                                                val intent =
                                                    Intent(activty, SettingActivity::class.java)
                                                activty.startActivity(intent)
                                            }.setNeutralButton("此版不提示") { i, j ->
                                                defaultSharedPreferences.edit()
                                                    .putString("ignoreversion", versioncode).apply()
                                            }
                                    dialogs.show()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                        }
                    } catch (e: Exception) {

                    }
                }
            })
        }

    }
}
