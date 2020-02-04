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


import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.perol.asdpl.pixivez.networks.HeaderLoaderFactory
import java.io.InputStream


@GlideModule
class CustomGlideModule : AppGlideModule() {


    override fun registerComponents(context: Context, glide: Glide,
                                    registry: Registry) {
//        val builder = OkHttpClient.Builder()
//        builder.addInterceptor(object : Interceptor {
//            @Throws(IOException::class)
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val requestBuilder = chain.request().newBuilder()
//                        .removeHeader("User-Agent")
//                        .removeHeader("Accept-Encoding")
//                        .addHeader("User-Agent", "PixivAndroidApp/5.0.155 (Android 6.0.1; ${android.os.Build.MODEL})")
//                        .addHeader("referer", "https://app-api.pixiv.net/")
//                        .addHeader("Accept-Encoding", "gzip");
//                val request = requestBuilder.build()
//                return chain.proceed(request)
//            }
//        }).dns(ImageHttpDns())
//        val factory = OkHttpUrlLoader.Factory(builder.build())
        registry.replace(String::class.java, InputStream::class.java, HeaderLoaderFactory())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}
