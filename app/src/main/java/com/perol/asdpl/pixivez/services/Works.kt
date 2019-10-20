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
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.work.*
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
            var title = illust.title
            title = title.replace("/", "")
            title = title.replace("\\", "")
            title = title.replace(":", "")
            val user = illust.user.id
            val name = illust.id
            val format = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getString("saveformat", "0")?.toInt()
                    ?: 0
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
                url = illust.meta_single_page.original_image_url
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

//            Observable.create<File> {
//                val storePath = PxEZApp.storepath
//                val appDir = File(storePath)
//                if (!appDir.exists()) {
//                    appDir.mkdirs()
//                }
//                val file = File(appDir, filename)
//                if (file.exists()) {
//                    it.onComplete()
//                    return@create
//                }
//                val futurefile = GlideApp.with(PxEZApp.instance).asFile().load(url).submit()
//                val finalfile = futurefile.get()
//                finalfile.copyTo(file)
//                GlideApp.with(PxEZApp.instance).clear(futurefile)
//                it.onNext(file)
//            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
//                PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it)))
//                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.savesuccess), Toast.LENGTH_SHORT).show()
//
//            }, {
//                it.printStackTrace()
//            }, {
//                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show()
//            }, {})

            val inputData = workDataOf("file" to filename, "url" to url, "title" to illust.title, "id" to illust.id)
            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ImgDownLoadWorker>()
                    .setInputData(inputData)
                    .addTag("image")
                    .build()
            WorkManager.getInstance(PxEZApp.instance).enqueueUniqueWork(url, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
            WorkManager.getInstance(PxEZApp.instance).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                    .observeForever { workInfo ->
                        if (workInfo == null) {
                            return@observeForever
                        }
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            if (workInfo.outputData.getBoolean("exist", false)) {
                                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show()
                                return@observeForever
                            }
                            Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.savesuccess), Toast.LENGTH_SHORT).show()
                            val uri = workInfo.outputData.getString("path")
                            if (uri != null)
                                PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(uri))))
                        } else if (workInfo.state == WorkInfo.State.FAILED) {

                        } else if (workInfo.state == WorkInfo.State.CANCELLED) {

                            val file = File(PxEZApp.storepath, filename)
                            file.deleteOnExit()
                        }
                    }
        }

        fun checkUpdate(activty: Activity) {
            val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activty)
            if (com.perol.asdpl.pixivez.BuildConfig.ISGOOGLEPLAY || PxEZApp.autochecked || !defaultSharedPreferences.getBoolean("autocheck", true)) {
                return
            }
            PxEZApp.autochecked = true
            val checkurl = "https://raw.githubusercontent.com/Notsfsssf/Pix-EzViewer/master/gradle.properties";
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
                        if (versioncodeP >= versioncode.toInt() || defaultSharedPreferences.getString("ignoreversion", "") == versioncode) {

                        } else {
                            activty.runOnUiThread {
                                try {
                                    val dialogs = AlertDialog.Builder(activty).setTitle("发现新版本").setMessage(versionName).setPositiveButton("前往更新") { i, j ->
                                        try {
                                            val uri = Uri.parse("https://github.com/Notsfsssf/Pix-EzViewer")
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            activty.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toasty.info(PxEZApp.instance, "no browser found", Toast.LENGTH_SHORT).show()
                                        }
                                    }.setNegativeButton("提醒设置") { i, j ->
                                        val intent = Intent(activty, SettingActivity::class.java)
                                        activty.startActivity(intent)
                                    }.setNeutralButton("此版不提示") { i, j ->
                                        defaultSharedPreferences.edit().putString("ignoreversion", versioncode).apply()
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
