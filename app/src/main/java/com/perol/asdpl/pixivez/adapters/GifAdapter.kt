package com.perol.asdpl.pixivez.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.snackbar.Snackbar
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.IllustBean
import com.perol.asdpl.pixivez.responses.UgoiraMetadataResponse
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.waynejo.androidndkgif.GifEncoder

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.Comparator

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking

/**
 * Created by Notsfsssf on 2018/3/27.
 */

class GifAdapter(layoutResId: Int, data: List<File>?, private val data1: UgoiraMetadataResponse, large: IllustBean) : BaseQuickAdapter<File, BaseViewHolder>(layoutResId, data) {

    private var file: File? = null
    private val path: String = PxEZApp.instance.cacheDir.toString() + "/" + large.id + ".gif"
    private val path2: String = PxEZApp.storepath + "/" + large.id + ".gif"
    private val path3: String = PxEZApp.instance.cacheDir.toString() + "/" + large.id + ".ojbk"
    private var imready = false





    private fun createGif(delay: Int) {
        val file1 = File(path)
        if (!file1.exists()) {
            try {
                if (!file1.parentFile!!.exists()) {
                    file1.parentFile!!.mkdirs()
                }
                file1.createNewFile()

            } catch (e: Exception) {
            }

        }
        try {

            ndkGifGenerator(delay)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }

    }

    @Throws(FileNotFoundException::class, RuntimeException::class)
    private fun ndkGifGenerator(delay: Int) {
        val file1 = File(path3)
        if (file1.exists()) {
            return
        }
        val gifEncoder = GifEncoder()
        val fs = file!!.listFiles()
        val pics = ArrayList(Arrays.asList(*fs!!))
        if (pics.isEmpty()) {
            throw RuntimeException("unzip files not found")
        }
        if (pics.size < data1.ugoira_metadata.frames.size) {
            throw RuntimeException("something wrong in manga files")
        }
        pics.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
        for (i in pics.indices) {
            if (pics[i].isFile) {
                val bitmap = BitmapFactory.decodeFile(pics[i].absolutePath) ?: continue
                if (i == 0) {
                    gifEncoder.init(bitmap.width, bitmap.height, path, GifEncoder.EncodingType.ENCODING_TYPE_STABLE_HIGH_MEMORY)
                }
                  gifEncoder.encodeFrame(bitmap, delay)

            }

        }
        gifEncoder.close()
        file1.mkdirs()
        Log.d("d", "I'm ok???????????????????????????")


    }

    private fun bilibiliGenerator(delay: Int) {
        //        BurstLinker burstLinrker = new BurstLinker();
        //        try{
        ////            burstLinker.init(500,500,file);
        //        }
    }

    override fun convert(helper: BaseViewHolder, item: File) {
        helper.addOnLongClickListener(R.id.imageview_gifx)
        this.file = item
        val spinKitView = helper.getView<SpinKitView>(R.id.progressbar_loadingx)
        spinKitView.visibility = View.VISIBLE
        val imageView = helper.getView<ImageView>(R.id.imageview_gifx)
        imageView.setOnLongClickListener { v ->
            if (imready)
                Snackbar.make(v, mContext.getString(R.string.saveselectpic), Snackbar.LENGTH_LONG).setAction("确认") { v ->
                    Observable.create(ObservableOnSubscribe<Uri> { emitter ->
                        val file = File(path)
                        val file1 = File(path2)
                        if (!file1.parentFile!!.exists()) {
                            file1.parentFile!!.mkdir()
                        }
                        if (!file1.exists()) {
                            file.copyTo(file1,overwrite = true)
                            val uri = Uri.fromFile(file1)
                            emitter.onNext(uri)
                        } else {
                            emitter.onComplete()
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Uri> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onNext(uri: Uri) {
                                    Snackbar.make(v, "成功！", Snackbar.LENGTH_SHORT).show()
                                    PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                                }

                                override fun onError(e: Throwable) {
                                    Snackbar.make(v, "保存失败，请清除缓存或更改保存路径后重试" + e.message, Snackbar.LENGTH_SHORT).show()
                                }

                                override fun onComplete() {
                                    Snackbar.make(v, "动图已存在", Snackbar.LENGTH_SHORT).show()
                                }
                            })
                }.show()
           true
        }
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            createGif(data1.ugoira_metadata.frames[0].delay)
            emitter.onNext("d")
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(s: String) {
                        try {
                            imready = true
                            spinKitView.visibility = View.GONE
                            GlideApp.with(imageView.context).asGif().load(path).skipMemoryCache(true).into(imageView)
                        } catch (e: Exception) {

                        }

                    }

                    override fun onError(e: Throwable) {
                        Toasty.error(PxEZApp.instance, e.message + mContext.getString(R.string.errorgif)).show()
                    }

                    override fun onComplete() {

                    }
                })


    }


}
