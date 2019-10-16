package com.perol.asdpl.pixivez.services

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.SettingActivity
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
            TToast.startDownload(PxEZApp.instance)
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
            Observable.create<File> {
                val storePath = PxEZApp.storepath
                val appDir = File(storePath)
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }
                val file = File(appDir, filename)
                if (file.exists()) {
                    it.onComplete()
                    return@create
                }
                val futurefile = GlideApp.with(PxEZApp.instance).asFile().load(url).submit()
                val finalfile = futurefile.get()
                finalfile.copyTo(file)
                GlideApp.with(PxEZApp.instance).clear(futurefile)
                it.onNext(file)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it)))
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.savesuccess), Toast.LENGTH_SHORT).show()

            }, {
                it.printStackTrace()
            }, {
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show()
            }, {})
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
