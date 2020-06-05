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

package com.perol.asdpl.pixivez.services

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.android.play.core.missingsplits.MissingSplitsManagerFactory
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.CrashHandler
import com.perol.asdpl.pixivez.objects.Toasty
import com.tencent.bugly.Bugly
import kotlinx.coroutines.delay
import okhttp3.internal.waitMillis
import java.io.File


class PxEZApp : Application() {
    private val objectMapper by lazy { ObjectMapper().registerKotlinModule() }
    lateinit var pre: SharedPreferences;

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask?) {
        task?.let {
            val extendField = it.extendField
            val illustD = objectMapper.readValue(extendField, IllustD::class.java)
            val title = illustD.title
            val sourceFile = File(it.filePath)

            val needCreateFold = pre.getBoolean("needcreatefold", false)
            val userName = illustD.userName?.toLegal()
            val path = if (needCreateFold) {
                "$storepath/${userName}_${illustD.userId}"
            } else storepath
            val targetFile = File(path, sourceFile.name)
            sourceFile.copyTo(targetFile, overwrite = true)
            MediaScannerConnection.scanFile(
                this,
                arrayOf(targetFile.path),
                arrayOf(
                    MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(targetFile.extension)
                )
            ) { _, _ ->

            }
            sourceFile.delete()
            Toasty.success(this, "${title}${getString(R.string.savesuccess)}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreate() {
        //https://developer.android.com/guide/app-bundle/sideload-check#missing_splits
        if (BuildConfig.ISGOOGLEPLAY)
            if (MissingSplitsManagerFactory.create(this).disableAppIfMissingRequiredSplits()) {
                // Skip app initialization.
                return
            }
        super.onCreate()
        pre = PreferenceManager.getDefaultSharedPreferences(this)
        Aria.init(this)
        Aria.download(this).register()

        Aria.get(this).apply {
            downloadConfig.apply {
//                queueMod=QueueMod.NOW.tag
                maxTaskNum = pre.getString("max_task_num", "2")!!.toInt()
            }
            appConfig.apply {
                isNotNetRetry = true
            }
        }

        Thread(Runnable {
            //Aria.download(this).removeAllTask(true)
            Aria.download(this).allCompleteTask?.forEach {
                Aria.download(this).load(it.id).cancel(true)
            }
            if( pre.getBoolean("resume_unfinished_task",true)
                //&& Aria.download(this).allNotCompleteTask?.isNotEmpty()
            )
            {
                //Toasty.normal(this, getString(R.string.unfinished_task_title), Toast.LENGTH_SHORT).show()
                Aria.download(this).allNotCompleteTask?.forEach {
                    Aria.download(this).load(it.id).cancel(true)
                    val illustD = objectMapper.readValue(it.str, IllustD::class.java)
                    Aria.download(this).load(it.url)
                        .setFilePath(it.filePath) //设置文件保存的完整路径
                        .ignoreFilePathOccupy()
                        .setExtendField(Works.mapper.writeValueAsString(illustD))
                        .option(Works.option)
                        .create()
                    Thread.sleep(500)
                }
            }
            /*
        if(Aria.download(this).allNotCompleteTask.isNotEmpty())
        {
            MaterialDialog(this).show {
                title(R.string.unfinished_task_title)
                message(R.string.unfinished_task_content)
                negativeButton(android.R.string.cancel)
                positiveButton(android.R.string.ok) {
                    Aria.download(this).allNotCompleteTask?.forEach {
                        //Aria.download(this).load(it.id).resume()
                        val illustD = objectMapper.readValue(it.str, IllustD::class.java)
                        val fileName = it.fileName
                        val targetPath =
                            "${PxEZApp.instance.cacheDir}${File.separator}${fileName}"
                        Aria.download(PxEZApp.instance)
                            .load(it.url) //读取下载地址
                            .setFilePath(targetPath) //设置文件保存的完整路径
                            .ignoreFilePathOccupy()
                            .setExtendField(Works.mapper.writeValueAsString(illustD))
                            .option(Works.option)
                            .create()
                    }
                }
            }
        }
        */
            /*Aria.download(this).taskList?.forEach {
            if(it.isComplete)
                Aria.download(this).load(it.id).cancel()
            else
            {
                Aria.download(this).load(it.id).cancel(true)
                val illustD = objectMapper.readValue(it.str, IllustD::class.java)
                Aria.download(PxEZApp.instance)
                    .load(it.url) //读取下载地址
                    .setFilePath(it.filePath) //设置文件保存的完整路径
                    .ignoreFilePathOccupy()
                    .setExtendField(Works.mapper.writeValueAsString(illustD))
                    .option(Works.option)
                    .create()
            }
        }*/
        }).start()
        instance = this
        AppCompatDelegate.setDefaultNightMode(
            pre.getString(
                "dark_mode",
                "-1"
            )!!.toInt()
        )
        animationEnable = pre.getBoolean("animation", true)
        language = pre.getString("language", "0")?.toInt() ?: 0
        storepath = pre.getString(
            "storepath1",
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        )
            ?: Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        if (pre.getBoolean("crashreport", true)) {
            CrashHandler.getInstance().init(this)
        }
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0).language
        } else {
            resources.configuration.locale.language
        }
        if (!BuildConfig.ISGOOGLEPLAY)
            Bugly.init(this, "5f21ff45b7", BuildConfig.DEBUG)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityCollector.collect(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityCollector.discard(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                //
            }
        })
    }

    private val Activity.simpleName get() = javaClass.simpleName

    object ActivityCollector {
        @JvmStatic
        private val activityList = mutableListOf<Activity>()

        @JvmStatic
        fun collect(activity: Activity) {
            activityList.add(activity)
        }

        @JvmStatic
        fun discard(activity: Activity) {
            activityList.remove(activity)
        }

        @JvmStatic
        fun recreate() {
            for (i in activityList.size - 1 downTo 0) {
                activityList[i].recreate()
            }
        }
    }

    companion object {
        @JvmStatic
        var storepath = ""

        @JvmStatic
        var locale = "zh"

        @JvmStatic
        var language: Int = 0

        @JvmStatic
        var animationEnable: Boolean = false
        lateinit var instance: PxEZApp
        var autochecked = false

        private const val TAG = "PxEZApp"
    }
}
