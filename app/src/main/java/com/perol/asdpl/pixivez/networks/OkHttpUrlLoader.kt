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

package com.perol.asdpl.pixivez.networks

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream


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
            .addHeader(
                "User-Agent",
                "PixivAndroidApp/5.0.155 (Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL})"
            )
                .addHeader("referer", "https://app-api.pixiv.net/")
                .build()
    }


}
