package com.perol.asdpl.pixivez.works

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.headersContentLength
import java.io.File

class ImgDownLoadWorker(var appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }

    override suspend fun doWork(): Result {

        val url = inputData.getString("url")!!
        val fileName = inputData.getString("file")!!
        val appDir = File(PxEZApp.storepath)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val file = File(appDir, fileName)
        if (file.exists()) {
            val outputData = workDataOf("exist" to true)
            return Result.success(outputData)
        }
        val builder = OkHttpClient.Builder();
        val client = builder.build()
        val request = Request.Builder()
                .addHeader("User-Agent", "PixivAndroidApp/5.0.155 (Android ${android.os.Build.VERSION.RELEASE}; Pixel C)")
                .addHeader("referer", "https://app-api.pixiv.net/")
                .get()
                .url(url)
                .build()
        val response = client.newCall(request).execute()
        try {
            withContext(Dispatchers.IO) {
                val lenght = response.headersContentLength()
                val inputstream = response.body?.byteStream()!!
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var bytes = inputstream.read(buffer)
                while (bytes >= 0) {
                    file.outputStream().write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = inputstream.read(buffer)
                    val lastUpdate = workDataOf("max" to lenght, "now" to bytesCopied, "url" to url, "file" to fileName)
                    setProgress(lastUpdate)
                }
            }
            val outputData = workDataOf("path" to file.absolutePath)
            return Result.success(outputData)
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.IO) {
                file.deleteOnExit()
            }
            return Result.failure()
        }
    }

}