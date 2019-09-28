package com.perol.asdpl.pixivez.services


import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.perol.asdpl.pixivez.networks.HeaderLoaderFactory
import com.perol.asdpl.pixivez.networks.ImageHttpDns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by asdpl on 2018/2/23.
 */

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
//                        .addHeader("User-Agent", "PixivAndroidApp/5.0.155 (Android 6.0.1; Pixel C)")
//                        .addHeader("referer", "https://app-api.pixiv.net/")
//                        .addHeader("Accept-Encoding", "gzip");
//                val request = requestBuilder.build()
//                return chain.proceed(request)
//            }
//        }).dns(ImageHttpDns())
//        val factory = OkHttpUrlLoader.Factory(builder.build())
        registry.replace(String::class.java, InputStream::class.java,HeaderLoaderFactory())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}
