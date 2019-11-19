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

package com.perol.asdpl.pixivez.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.responses.IllustDetailResponse
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.services.UnzipUtil
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.IllustBeanEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class PictureXViewModel : BaseViewModel() {
    val newRec = arrayListOf<Illust>()
    val illustDetailResponse = MutableLiveData<IllustDetailResponse?>()
    val retrofitRespository: RetrofitRespository = RetrofitRespository.getInstance()
    val aboutPics = MutableLiveData<ArrayList<Illust>>()
    val likeIllust = MutableLiveData<Boolean>()
    val followUser = MutableLiveData<Boolean>()
    var tags = MutableLiveData<BookMarkDetailResponse.BookmarkDetailBean>()
    val progress = MutableLiveData<ProgressInfo>()
    val downloadGifSuccess = MutableLiveData<Boolean>()
    val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    @Throws(Exception::class)
    fun downloadzip(medium: String) {

        val zippath = "${PxEZApp.instance.cacheDir.path}/${illustDetailResponse.value!!.illust.id}.zip"
        val file = File(zippath)
        if (file.exists()) {
            Observable.create<Int> { ob ->
                UnzipUtil.UnZipFolder(file, PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id)
                ob.onNext(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                downloadGifSuccess.value = true
            }, {
                File(PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id).deleteRecursively()
                file.delete()
                reDownLoadGif(medium)

            }, {})
        } else {
            reDownLoadGif(medium)
        }

    }

    fun loadgif(id: Long) = retrofitRespository.getUgoiraMetadata(id)

    @SuppressLint("CheckResult")
    fun reDownLoadGif(medium: String) {
        val zippath = "${PxEZApp.instance.cacheDir}/${illustDetailResponse.value!!.illust.id}.zip"
        val file = File(zippath)
        progress.value = ProgressInfo(0, 0)
        retrofitRespository.getGIFFile(medium).subscribe { response ->
            val inputstream = response.byteStream()
            Observable.create<Int> { ob ->

                val output = file.outputStream()
                println("----------")
                progress.value!!.all = response.contentLength()
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var bytes = inputstream.read(buffer)
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = inputstream.read(buffer)
                    progress.value!!.now = bytesCopied
                    Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        progress.value = progress.value!!
                    }
                }
                inputstream.close()
                output.close()
                println("+++++++++")
                UnzipUtil.UnZipFolder(file, PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id)
                ob.onNext(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                downloadGifSuccess.value = true
                println("wwwwwwwwwwwwwwwwwwwwww")
            }, {
                it.printStackTrace()
            })

        }
    }

    fun firstGet(toLong: Long, illust: Illust?) {
        if (illust == null)
            disposables.add(retrofitRespository.getIllust(toLong).subscribe({ it ->
                illustDetailResponse.value = it
                likeIllust.value = it!!.illust.is_bookmarked
                Observable.just(1).observeOn(Schedulers.io()).subscribe { ot ->
                    val ee = appDatabase.illusthistoryDao().getHistoryOne(it.illust.id)
                    if (ee.isNotEmpty()) {
                        appDatabase.illusthistoryDao().deleteOne(ee[0])
                        appDatabase.illusthistoryDao().insert(IllustBeanEntity(null, it.illust.image_urls.square_medium, it.illust.id))
                    } else
                        appDatabase.illusthistoryDao().insert(IllustBeanEntity(null, it.illust.image_urls.square_medium, it.illust.id))
                }
            }, {}, {}))
        else {
            illustDetailResponse.value = IllustDetailResponse().also {
                it.illust = illust
            }
            likeIllust.value = illust.is_bookmarked
        }

    }

    fun getRelative(long: Long) {
        disposables.add(retrofitRespository.getIllustRecommended(long).subscribe({

//            aboutPics.value = it.illusts as ArrayList<Illust>?
            newRec.addAll(it.illusts as ArrayList<Illust>)
            disposables.add(retrofitRespository.getIllustRecommendedNext(long,30).subscribe({ nextIt ->
                newRec.addAll(nextIt.illusts as ArrayList<Illust>)
                disposables.add(retrofitRespository.getIllustRecommendedNext(long,60).subscribe({ nextIt2 ->
                    newRec.addAll(nextIt2.illusts as ArrayList<Illust>)
                    aboutPics.value = newRec
                }, {}, {}))

            }, {}, {}))

        }, {}, {}))
    }



    @SuppressLint("CheckResult")
    fun FabClick() {
        val id = illustDetailResponse.value!!.illust.id
        val postUnlikeIllust = retrofitRespository.postUnlikeIllust(id)
        val postLikeIllust = retrofitRespository.postLikeIllust(id)
        if (illustDetailResponse.value!!.illust.is_bookmarked!!) {
            postUnlikeIllust!!.subscribe({
                likeIllust.value = false
                illustDetailResponse.value!!.illust.is_bookmarked = false
            }, {

            }, {}, {})
        } else {
            postLikeIllust!!.subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.is_bookmarked = true
            }, {}, {})
        }
    }

    fun fabOnLongClick() {
        val c = retrofitRespository.getBookmarkDetail(illustDetailResponse.value!!.illust.id.toLong())
        c!!.subscribe(
                {

                    tags.value = it.bookmark_detail

                }
                , {}, {})
    }

    fun onDialogClick(arrayList: ArrayList<String>, boolean: Boolean, toLong: Long) {
        if (!illustDetailResponse.value!!.illust.is_bookmarked) {
            var string = "public"
            if (!boolean) {
                string = "public"
            } else {
                string = "private"
            }
            val postbookmark = retrofitRespository.postLikeIllustWithTags(toLong, string, arrayList)
            postbookmark!!.subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.is_bookmarked = true
            }, {}, {})

        } else {
            val postbookmark = retrofitRespository.postUnlikeIllust(toLong.toLong())
            postbookmark!!.subscribe({
                likeIllust.value = false
                illustDetailResponse.value!!.illust.is_bookmarked = false
            }, {}, {})
        }
    }

    fun likeUser() {
        val id = illustDetailResponse.value!!.illust.user.id
        if (!illustDetailResponse.value!!.illust.user.is_followed) {
            retrofitRespository.postfollowUser(id, "public").subscribe({
                followUser.value = true
                illustDetailResponse.value!!.illust.user.is_followed = true
            }, {}, {})
        } else {
            retrofitRespository.postunfollowUser(id).subscribe({
                followUser.value = false
                illustDetailResponse.value!!.illust.user.is_followed = false
            }, {}, {}
            )
        }
    }

}