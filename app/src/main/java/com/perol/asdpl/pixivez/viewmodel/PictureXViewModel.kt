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
    val illustDetailResponse = MutableLiveData<IllustDetailResponse?>()
    val retrofitRespository: RetrofitRespository = RetrofitRespository.getInstance()
    val aboutPics = MutableLiveData<ArrayList<Illust>>()
    val likeIllust = MutableLiveData<Boolean>()
    val followUser = MutableLiveData<Boolean>()
    var tags = MutableLiveData<BookMarkDetailResponse.BookmarkDetailBean>()
    val progress = MutableLiveData<ProgressInfo>()
    val downloadGifSuccess = MutableLiveData<Boolean>()
    val startPostPone = MutableLiveData<Boolean>()
    private val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    @Throws(Exception::class)
    fun downloadZip(medium: String) {
        val zipPath =
            "${PxEZApp.instance.cacheDir.path}/${illustDetailResponse.value!!.illust.id}.zip"
        val file = File(zipPath)
        if (file.exists()) {
            Observable.create<Int> { ob ->
                UnzipUtil.UnZipFolder(
                    file,
                    PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id
                )
                ob.onNext(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                downloadGifSuccess.value = true
            }, {
                File(PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id).deleteRecursively()
                file.delete()
                reDownLoadGif(medium)

            }, {}).add()
        } else {
            reDownLoadGif(medium)
        }

    }

    fun loadGif(id: Long) = retrofitRespository.getUgoiraMetadata(id)

    private fun reDownLoadGif(medium: String) {
        val zipPath = "${PxEZApp.instance.cacheDir}/${illustDetailResponse.value!!.illust.id}.zip"
        val file = File(zipPath)
        progress.value = ProgressInfo(0, 0)
        retrofitRespository.getGIFFile(medium).subscribe { response ->
            val inputStream = response.byteStream()
            Observable.create<Int> { ob ->
                val output = file.outputStream()
                println("----------")
                progress.value!!.all = response.contentLength()
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var bytes = inputStream.read(buffer)
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = inputStream.read(buffer)
                    progress.value!!.now = bytesCopied
                    Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        progress.value = progress.value!!
                    }
                }
                inputStream.close()
                output.close()
                println("+++++++++")
                UnzipUtil.UnZipFolder(
                    file,
                    PxEZApp.instance.cacheDir.path + "/" + illustDetailResponse.value!!.illust.id
                )
                ob.onNext(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                downloadGifSuccess.value = true
                println("wwwwwwwwwwwwwwwwwwwwww")
            }, {
                it.printStackTrace()
            })

        }.add()
    }

    fun firstGet(toLong: Long, illust: Illust?) {
        if (illust == null) {
            startPostPone.value = true
            disposables.add(retrofitRespository.getIllust(toLong).subscribe({ it ->
                illustDetailResponse.value = it
                likeIllust.value = it!!.illust.is_bookmarked
                Observable.just(1).observeOn(Schedulers.io()).subscribe { ot ->
                    val ee = appDatabase.illusthistoryDao().getHistoryOne(it.illust.id)
                    if (ee.isNotEmpty()) {
                        appDatabase.illusthistoryDao().deleteOne(ee[0])
                        appDatabase.illusthistoryDao().insert(
                            IllustBeanEntity(
                                null,
                                it.illust.image_urls.square_medium,
                                it.illust.id
                            )
                        )
                    } else
                        appDatabase.illusthistoryDao().insert(
                            IllustBeanEntity(
                                null,
                                it.illust.image_urls.square_medium,
                                it.illust.id
                            )
                        )
                }
            }, {}, {}))
        } else {

            illustDetailResponse.value = IllustDetailResponse().also {
                it.illust = illust
            }
            likeIllust.value = illust.is_bookmarked
            Observable.just(1).observeOn(Schedulers.io()).subscribe { ot ->
                val ee = appDatabase.illusthistoryDao().getHistoryOne(illust.id)
                if (ee.isNotEmpty()) {
                    appDatabase.illusthistoryDao().deleteOne(ee[0])
                    appDatabase.illusthistoryDao()
                        .insert(IllustBeanEntity(null, illust.image_urls.square_medium, illust.id))
                } else
                    appDatabase.illusthistoryDao().insert(
                        IllustBeanEntity(
                            null,
                            illust.image_urls.square_medium,
                            illust.id
                        )
                    )
            }.add()
        }

    }

    fun getRelative(long: Long) {
        disposables.add(retrofitRespository.getIllustRecommended(long).subscribe({

            aboutPics.value = it.illusts as ArrayList<Illust>?
        }, {}, {}))
    }

    fun FabClick() {
        val id = illustDetailResponse.value!!.illust.id
        val postUnlikeIllust = retrofitRespository.postUnlikeIllust(id)
        val postLikeIllust = retrofitRespository.postLikeIllust(id)
        if (illustDetailResponse.value!!.illust.is_bookmarked) {
            postUnlikeIllust.subscribe({
                likeIllust.value = false
                illustDetailResponse.value!!.illust.is_bookmarked = false
            }, {

            }, {}, {}).add()
        } else {
            postLikeIllust!!.subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.is_bookmarked = true
            }, {}, {}).add()
        }
    }

    fun fabOnLongClick() {
        if (illustDetailResponse.value != null)
            retrofitRespository
                .getBookmarkDetail(illustDetailResponse.value!!.illust.id)
                .subscribe(
                    { tags.value = it.bookmark_detail }
                    , {}, {}).add()
        else {
            val a = illustDetailResponse.value
            print(a)
        }
    }

    fun onDialogClick(boolean: Boolean) {
        val toLong = illustDetailResponse.value!!.illust.id
        val arrayList = ArrayList<String>()
        for (i in tags.value!!.tags) {
            if (i.isIs_registered) {
                arrayList.add(i.name)
            }
        }
        if (!illustDetailResponse.value!!.illust.is_bookmarked) {
            var string = "public"
            if (!boolean) {
                string = "public"
            } else {
                string = "private"
            }
            val postbookmark =
                retrofitRespository.postLikeIllustWithTags(toLong, string, arrayList).subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.is_bookmarked = true
                }, {}, {}).add()

        } else {
            retrofitRespository.postUnlikeIllust(toLong.toLong())
                .subscribe({
                    likeIllust.value = false
                    illustDetailResponse.value!!.illust.is_bookmarked = false
                }, {}, {}).add()
        }
    }

    fun likeUser() {
        val id = illustDetailResponse.value!!.illust.user.id
        if (!illustDetailResponse.value!!.illust.user.is_followed) {
            retrofitRespository.postfollowUser(id, "public").subscribe({
                followUser.value = true
                illustDetailResponse.value!!.illust.user.is_followed = true
            }, {}, {}).add()
        } else {
            retrofitRespository.postunfollowUser(id).subscribe({
                followUser.value = false
                illustDetailResponse.value!!.illust.user.is_followed = false
            }, {}, {}
            ).add()
        }
    }

}

data class ProgressInfo(var now: Long, var all: Long)
