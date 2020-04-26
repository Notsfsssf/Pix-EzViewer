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

import android.media.MediaScannerConnection
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import java.io.File

fun String.toLegal(): String {
    return this.replace("/", "").replace("\\", "").replace(":", "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class IllustD(
    var id: Long = 0,
    var preview: String? = null,
    var userId: Long = 0,
    var userName: String? = null,
    var userAvatar: String? = null,
    var title: String? = null,
    var url: String
)

object Works {


    fun imageDownloadWithFile(illust: Illust, file: File, part: Int?) {
        var url = ""
        val title = illust.title.toLegal()
        val userName = illust.user.name.toLegal()
        val user = illust.user.id
        val name = illust.id
        val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance);
        val format = pre.getString(
            "saveformat",
            "0"
        )?.toInt()
            ?: 0
        val needCreateFold = pre.getBoolean("needcreatefold", false)
        var type = ".png"
        var filename = "${name}_p$part$type"
        if (part != null && illust.meta_pages.isNotEmpty()) {
            url = illust.meta_pages[part].image_urls.original
            type = if (url.contains("png")) {
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
            url = illust.meta_single_page.original_image_url!!
            type = if (url.contains("png")) {
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
        val path = if (needCreateFold) {
            "${PxEZApp.storepath}/${userName}_${user}"
        } else PxEZApp.storepath
        val targetFile = File(path, filename)
        try {
            file.copyTo(targetFile, overwrite = true)
            file.delete()
            Toasty.success(
                PxEZApp.instance,
                PxEZApp.instance.resources.getString(R.string.savesuccess)+"!",
                Toast.LENGTH_SHORT
            ).show()
            MediaScannerConnection.scanFile(
                PxEZApp.instance,
                arrayOf(targetFile.path),
                arrayOf(
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(targetFile.extension)
                )
            ) { _, _ ->

            }
        } catch (e: Exception) {

        }
    }

    fun imageDownloadAll(illust: Illust) {
        TToast.startDownload(PxEZApp.instance)
        if (illust.meta_pages.isEmpty()) {
            imgD(illust, null)
        } else {
            for (i in illust.meta_pages.indices) {
                imgD(illust, i)
            }
        }
    }

    val option by lazy {
        HttpOption()
            .apply {
                addHeader(
                    "User-Agent",
                    "PixivAndroidApp/5.0.155 (Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL})"
                ).addHeader("referer", "https://app-api.pixiv.net/")
            }
    }
    val mapper by lazy {
        ObjectMapper().registerKotlinModule()
    }

    fun imgD(illust: Illust, part: Int?) {
        var url = ""
        val title = illust.title.toLegal()
        val userName = illust.user.name.toLegal()
        val user = illust.user.id
        val name = illust.id
        val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance)
        val format = pre.getString(
            "saveformat",
            "0"
        )?.toInt()
            ?: 0

        var type = ".png"
        var filename = "${name}_p$part$type"
        if (part != null && illust.meta_pages.isNotEmpty()) {
            url = illust.meta_pages[part].image_urls.original
            type = if (url.contains("png")) {
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
            url = illust.meta_single_page.original_image_url!!
            type = if (url.contains("png")) {
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
        val needCreateFold = pre.getBoolean("needcreatefold", false)
        val path = if (needCreateFold) {
            "${PxEZApp.storepath}/${userName}_${illust.user.id}"
        } else PxEZApp.storepath
        val targetFile = File(path, filename)
        if (targetFile.exists()) {
            Toasty.normal(
                PxEZApp.instance,
                PxEZApp.instance.getString(R.string.alreadysaved),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val illustD = IllustD(
            id = illust.id,
            preview = illust.image_urls.square_medium,
            userName = illust.user.name,
            userId = illust.user.id,
            userAvatar = illust.user.profile_image_urls.medium,
            title = illust.title,
            url = url
        )
        val targetPath = "${PxEZApp.instance.cacheDir}${File.separator}${filename}"
        Aria.download(PxEZApp.instance)
            .load(url) //读取下载地址
            .setFilePath(targetPath) //设置文件保存的完整路径
            .ignoreFilePathOccupy()
            .setExtendField(mapper.writeValueAsString(illustD))
            .option(option)
            .create()


    }


    @Deprecated("imgD")
    fun imageDownloadOne(illust: Illust, part: Int?) {
        var url = ""
        val title = illust.title.toLegal()
        val userName = illust.user.name.toLegal()
        val user = illust.user.id
        val name = illust.id
        val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance);
        val format = pre.getString(
            "saveformat",
            "0"
        )?.toInt()
            ?: 0
        val needCreateFold = pre.getBoolean("needcreatefold", false)
        var type = ".png"
        var filename = "${name}_p$part$type"
        if (part != null && illust.meta_pages.isNotEmpty()) {
            url = illust.meta_pages[part].image_urls.original
            type = if (url.contains("png")) {
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
            url = illust.meta_single_page.original_image_url!!
            type = if (url.contains("png")) {
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


    }


}
