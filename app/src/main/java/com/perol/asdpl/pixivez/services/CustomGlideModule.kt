package com.perol.asdpl.pixivez.services


import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.perol.asdpl.pixivez.networks.HeaderLoaderFactory


import java.io.InputStream
import com.perol.asdpl.pixivez.networks.ImageHttpDns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


/**
 * Created by asdpl on 2018/2/23.
 */

@GlideModule
class CustomGlideModule : AppGlideModule() {


    override fun registerComponents(context: Context, glide: Glide,
                                    registry: Registry) {
//        val client= OkHttpClient.Builder().addInterceptor(object : Interceptor {
//
//            @Throws(IOException::class)
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val original = chain.request()
//                val requestBuilder = original.newBuilder()
//                        .addHeader("User-Agent", "PixivIOSApp/5.8.0")
//                        .addHeader("referer", "https://app-api.pixiv.net/")
//                val request = requestBuilder.build()
//                return chain.proceed(request)
//            }
//        }).dns(ImageHttpDns()).build()
//        registry.replace(GlideUrl::class.java, InputStream::class.java, com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader.Factory(client))
      registry.prepend(String::class.java, InputStream::class.java, HeaderLoaderFactory())

    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}
