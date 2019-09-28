package com.perol.asdpl.pixivez.services

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class Works {
    companion object {
        fun ImageDownloadAll(illust: Illust) {
            TToast.startDownload(PxEZApp.instance)
            if (illust.meta_pages.isEmpty()) {
                ImageDownloadOne(illust, null)
            } else {
                for (i in illust.meta_pages.indices) {
                    ImageDownloadOne(illust, i)
                }
            }
        }

        fun ImageDownloadOne(illust: Illust, part: Int?) {
            var url = ""
            val title = illust.title
            title.replace("/","")
            title.replace("\\","")
            title.replace(":","")
            val user = illust.user.id
            val name = illust.id
            TToast.startDownload(PxEZApp.instance)
            val format = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getString("saveformat", "0")?.toInt()
                    ?: 0
            var type = ".png"
            var filename = "${name}_p$part$type"
            if (part != null) {
                url = illust.meta_pages[part].image_urls.original
                type = if (illust.meta_pages[part].image_urls.original.contains("png")) {
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
                type = if (illust.meta_single_page.original_image_url.contains("png")) {
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


    }
}
