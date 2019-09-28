package com.perol.asdpl.pixivez.networks

import android.content.Context
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader

import java.io.InputStream

import okhttp3.OkHttpClient
import com.bumptech.glide.load.model.LazyHeaderFactory
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache


/**
 * Created by asdpl on 2018/2/23.
 */
class HeaderLoaderFactory : ModelLoaderFactory<String, InputStream> {
    private val modelCache = ModelCache<String, GlideUrl>(250)
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
        return OkHttpUrlLoader(multiFactory.build(GlideUrl::class.java, InputStream::class.java), modelCache)
    }


    override fun teardown() { /* nothing to free */
    }
}

class OkHttpUrlLoader(concreteLoader: ModelLoader<GlideUrl, InputStream>, modelCache: ModelCache<String, GlideUrl>) : BaseGlideUrlLoader<String>(concreteLoader, modelCache) {
    override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
        return model
    }

    override fun handles(model: String): Boolean {
        return true
    }

    override fun getHeaders(model: String?, width: Int, height: Int, options: Options?): Headers? {
        return LazyHeaders.Builder()
                .addHeader("User-Agent", "PixivIOSApp/5.8.0")
                .addHeader("referer", "https://app-api.pixiv.net/")
                .build()
    }


}
