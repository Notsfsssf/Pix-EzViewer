package com.perol.asdpl.pixivez.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.services.Works
import java.lang.Exception
import java.lang.RuntimeException

class DownLoadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val nextUrl = inputData.getString("nexturl")
        var outNextUrl: String? = null
        val retrofit = RetrofitRespository.getInstance()
        retrofit.getNext(nextUrl!!).blockingSubscribe({
            outNextUrl = it.next_url
            it.illusts.forEach { illust ->
                if (illust.meta_pages.isNotEmpty()) {
                    for (i in illust.meta_pages.indices) {
                        val imageurl = illust.meta_pages[i].image_urlsX.original
                        Works.ImageDownloadBack(imageurl, illust.user.id.toString(), illust.id.toString(), i.toString(),
                                if (imageurl.contains("png")) {
                                    ".png"
                                } else {
                                    ".jpg"
                                })
                    }
                } else {
                    Works.ImageDownloadBack(illust.meta_single_page.original_image_url, illust.user.id.toString(), illust.id.toString(), null, if (illust.meta_single_page.original_image_url.contains("png")) {
                        ".png"
                    } else {
                        ".jpg"
                    })
                }
            }
        }, {}, {

        })
        val outputData = Data.Builder()
                .putString("nexturl", outNextUrl)
                .build()

        return Result.success(outputData)
    }
}