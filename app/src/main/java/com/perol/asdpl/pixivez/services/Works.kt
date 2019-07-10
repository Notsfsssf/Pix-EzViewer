package com.perol.asdpl.pixivez.services

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception

class Works {
    companion object {
        fun ImageDownload(url: String, user: String, name: String, part: String?, type: String) {
            TToast.startDownload(PxEZApp.instance)
            var filename = "${name}_p$part$type"
            if (part != null) {
                when (PxEZApp.saveformat) {
                    0 -> {
                        filename = "${name}_$part$type"
                    }
                    1 -> {
                        filename = "${name}_p$part$type"
                    }
                    2 -> {
                        filename = "${user}_${name}_$part$type"
                    }
                }
            } else {
                when (PxEZApp.saveformat) {
                    0 -> {
                        filename = "$name$type"
                    }
                    1 -> {
                        filename = "$name$type"
                    }
                    2 -> {
                        filename = "${user}_$name$type"
                    }
                }
            }
            Observable.create<File> {
                val storePath = PxEZApp.storepath
                val appDir = File(storePath)
                if (!appDir.exists()) {
                    appDir.mkdir()
                }
                val file = File(appDir, filename)
                if (file.exists()) {
                    it.onComplete()
                }
                val futurefile = GlideApp.with(PxEZApp.instance).asFile().load(url).submit()
                try {
                    val finalfile = futurefile.get()
                    finalfile.copyTo(file, overwrite = true)
                    GlideApp.with(PxEZApp.instance).clear(futurefile)
                    it.onNext(file)
                } catch (e: Exception) {
                    it.onError(e)
                }

            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

                PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it)))
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.savesuccess), Toast.LENGTH_SHORT).show()

            }, {
            }, {
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show()
            }, {})


        }

        fun ImageDownloadBack(url: String, user: String, name: String, part: String?, type: String) {
            TToast.startDownload(PxEZApp.instance)
            var filename = "${name}_p$part$type"
            if (part != null) {
                when (PxEZApp.saveformat) {
                    0 -> {
                        filename = "${name}_$part$type"
                    }
                    1 -> {
                        filename = "${name}_p$part$type"
                    }
                    2 -> {
                        filename = "${user}_${name}_$part$type"
                    }
                }
            } else {
                when (PxEZApp.saveformat) {
                    0 -> {
                        filename = "$name$type"
                    }
                    1 -> {
                        filename = "$name$type"
                    }
                    2 -> {
                        filename = "${user}_$name$type"
                    }
                }
            }
            Observable.create<File> {
                val storePath = PxEZApp.storepath
                val appDir = File(storePath)
                if (!appDir.exists()) {
                    appDir.mkdir()
                }
                val file = File(appDir, filename)
                if (file.exists()) {
                    it.onComplete()
                }
                val futurefile = GlideApp.with(PxEZApp.instance).asFile().load(url).submit()
                try {
                    val finalfile = futurefile.get()
                    finalfile.copyTo(file, overwrite = true)
                    GlideApp.with(PxEZApp.instance).clear(futurefile)
                    it.onNext(file)
                } catch (e: Exception) {
                    it.onError(e)
                }

            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({

                PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(it)))
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.savesuccess), Toast.LENGTH_SHORT).show()

            }, {
            }, {
                Toasty.success(PxEZApp.instance, PxEZApp.instance.resources.getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show()
            }, {})


        }
    }
}
