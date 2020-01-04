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

class ImgDownLoadWorker(var appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }

    override suspend fun doWork(): Result {

        val url = inputData.getString("url")!!
        val fileName = inputData.getString("file")!!
        val title = inputData.getString("title")!!
        val id = inputData.getLong("id", 0L)
        val userName =inputData.getString("username")
        val needCreateFold= inputData.getBoolean("needcreatefold",false)
        val path = if (needCreateFold) {
            "${PxEZApp.storepath}/$userName"
        } else PxEZApp.storepath
        val appDir = File(path)

        val lastUpdate = workDataOf(
            "max" to 100,
            "now" to 1,
            "url" to url,
            "file" to fileName,
            "title" to title,
            "id" to id,
            "username" to userName,
            "needcreatefold" to needCreateFold
        )
        setProgress(lastUpdate)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val file = File(path, fileName)
        if (file.exists()) {
            val outputData = workDataOf("exist" to true)
            return Result.success(outputData)
        }
        val builder = OkHttpClient.Builder();
        val client = builder.build()
        val request = Request.Builder()
            .addHeader(
                "User-Agent",
                "PixivAndroidApp/5.0.155 (Android ${android.os.Build.VERSION.RELEASE}; Pixel C)"
            )
            .addHeader("referer", "https://app-api.pixiv.net/")
            .get()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        try {
            withContext(Dispatchers.IO) {
                val cacheFile = File.createTempFile(fileName, null, PxEZApp.instance.cacheDir)
                val lenght = response.headersContentLength()
                val inputstream = response.body?.byteStream()!!
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var bytes = inputstream.read(buffer)
                val out = cacheFile.outputStream()
                while (bytes >= 0) {
                    out.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = inputstream.read(buffer)
                    val lastUpdate1 = workDataOf(
                        "max" to lenght,
                        "now" to bytesCopied,
                        "url" to url,
                        "file" to fileName,
                        "title" to title,
                        "id" to id,
                        "username" to userName,
                        "needcreatefold" to needCreateFold
                    )
                    setProgress(lastUpdate1)
                }
                cacheFile.inputStream().copyTo(file.outputStream())
                cacheFile.delete()
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