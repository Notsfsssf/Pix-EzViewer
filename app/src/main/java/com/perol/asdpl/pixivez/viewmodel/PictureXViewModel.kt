package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.MutableLiveData
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse
import com.perol.asdpl.pixivez.responses.IllustDetailResponse
import com.perol.asdpl.pixivez.responses.IllustsBean
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
    val aboutPics = MutableLiveData<ArrayList<IllustsBean>>()
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
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe ({
                downloadGifSuccess.value = true
                println("wwwwwwwwwwwwwwwwwwwwww")
            },{
it.printStackTrace()
            })

        }
    }

    fun firstGet(toLong: Long) {
        retrofitRespository.getIllust(toLong)!!.subscribe({ it ->
            illustDetailResponse.value = it
            likeIllust.value = it!!.illust.isIs_bookmarked
            Observable.just(1).observeOn(Schedulers.io()).subscribe { ot ->
                val ee = appDatabase.illusthistoryDao().getHistoryOne(it.illust.id)
                if (ee.isNotEmpty()) {
                    appDatabase.illusthistoryDao().deleteOne(ee[0])
                    appDatabase.illusthistoryDao().insert(IllustBeanEntity(null, it.illust.image_urls.square_medium, it.illust.id))
                } else
                    appDatabase.illusthistoryDao().insert(IllustBeanEntity(null, it.illust.image_urls.square_medium, it.illust.id))
            }
        }, {}, {})

    }

    fun getRelative(long: Long) {
        retrofitRespository.getIllustRecommended(long).subscribe({

            aboutPics.value = it.illusts as ArrayList<IllustsBean>?
        }, {}, {})
    }

    fun FabClick() {
        val id = illustDetailResponse.value!!.illust.id
        val postUnlikeIllust = retrofitRespository.postUnlikeIllust(id)
        val postLikeIllust = retrofitRespository.postLikeIllust(id)
        if (illustDetailResponse.value!!.illust.isIs_bookmarked!!) {
            postUnlikeIllust!!.subscribe({
                likeIllust.value = false
                illustDetailResponse.value!!.illust.isIs_bookmarked = false
            }, {

            }, {}, {})
        } else {
            postLikeIllust!!.subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.isIs_bookmarked = true
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
        if (!illustDetailResponse.value!!.illust.isIs_bookmarked) {
            var string = "public"
            if (!boolean) {
                string = "public"
            } else {
                string = "private"
            }
            val postbookmark = retrofitRespository.postLikeIllustWithTags(toLong, string, arrayList)
            postbookmark!!.subscribe({
                likeIllust.value = true
                illustDetailResponse.value!!.illust.isIs_bookmarked = true
            }, {}, {})

        } else {
            val postbookmark = retrofitRespository.postUnlikeIllust(toLong.toLong())
            postbookmark!!.subscribe({
                likeIllust.value = false
                illustDetailResponse.value!!.illust.isIs_bookmarked = false
            }, {}, {})
        }
    }

    fun likeUser() {
        val id = illustDetailResponse.value!!.illust.user.id
        if (!illustDetailResponse.value!!.illust.user.isIs_followed) {
            retrofitRespository.postfollowUser(id, "public").subscribe({
                followUser.value = true
                illustDetailResponse.value!!.illust.user.isIs_followed = true
            }, {}, {})
        } else {
            retrofitRespository.postunfollowUser(id).subscribe({
                followUser.value = false
                illustDetailResponse.value!!.illust.user.isIs_followed = false
            }, {}, {}
            )
        }
    }

}