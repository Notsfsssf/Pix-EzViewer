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

package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.SpotlightAdapter
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.Spotlight
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.IllustDetailResponse
import com.perol.asdpl.pixivez.services.AppApiPixivService
import com.perol.asdpl.pixivez.services.PxEZApp
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_spotlight.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class SpotlightActivity : RinkActivity() {


    private val reurls = HashSet<Int>()
    private var appApiPixivService: AppApiPixivService? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var spotlightAdapter: SpotlightAdapter? = null
    private val list = ArrayList<Spotlight>()
    private var url = ""
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_spotlight)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        getData()
    }

    private fun getData() {
        val intent = intent
        url = intent.getStringExtra("url")
        textView_test.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(url)
            intent.data = content_url
            startActivity(intent)
        })
        val restClient = RestClient()
        appApiPixivService = restClient.retrofitAppApi.create(AppApiPixivService::class.java)
        val local = when (PxEZApp.language) {
            1 -> {
                Locale.ENGLISH
            }
            2 -> {
                Locale.TRADITIONAL_CHINESE
            }
            3 -> {
                Locale.JAPANESE
            }
            else -> {
                Locale.SIMPLIFIED_CHINESE
            }
        }
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            val builder = OkHttpClient.Builder()
            val okHttpClient = builder.build()
            val request = Request.Builder()
                    .url(url)
                .addHeader("Accept-Language", "${local.language}_${local.country}")
                    .build()
            val response = okHttpClient.newCall(request).execute()
            val result = response.body!!.string()
            emitter.onNext(result)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(s: String) {
                        val urls = getImgStra(s)

                        val doc = Jsoup.parse(s)

                        val elements1 = doc.select("div[class=am__description _medium-editor-text]")
                        val articletitle = ""
                        val stringBuilder = StringBuilder(articletitle)


                        for (element in elements1) {
                            stringBuilder.append(element.text())

                        }
                        textView_test.setText(stringBuilder)
                        for (string in urls) {
                            if (!string.contains("svg") && string.contains("https://www.pixiv.net/member_illust.php?")) {
                                reurls.add(Integer.valueOf(string.replace("https://www.pixiv.net/member_illust.php?mode=medium&illust_id=", "")))
                            }
                        }
                        getspolight()

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }

    private fun getspolight() {

        Observable.create(ObservableOnSubscribe<IllustDetailResponse> { emitter ->
            num = 0
            for (id in reurls) {
                num += 1
                var Authorization: String = ""
                runBlocking {
                    Authorization = AppDataRepository.getUser().Authorization
                }

                appApiPixivService!!.getIllust(Authorization, id.toLong()).subscribeOn(Schedulers.io())
                        .subscribe(object : Observer<IllustDetailResponse> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(illustDetailResponse: IllustDetailResponse) {
                                emitter.onNext(illustDetailResponse)


                            }

                            override fun onError(e: Throwable) {

                            }

                            override fun onComplete() {

                            }
                        })

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<IllustDetailResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(illustDetailResponse: IllustDetailResponse) {

                        val name = illustDetailResponse.illust.user.name
                        val title = illustDetailResponse.illust.title
                        list.add(Spotlight(title, name, illustDetailResponse.illust.user.profile_image_urls.medium, illustDetailResponse.illust.image_urls.large, illustDetailResponse.illust.id.toString(), illustDetailResponse.illust.user.id))
                        if (num == reurls.size) {
                            onComplete()

                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        spotlightAdapter = SpotlightAdapter(R.layout.view_ranking_item1, list, this@SpotlightActivity)
                        recyclerview_spotlight.setLayoutManager(LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false))

                        recyclerview_spotlight.setAdapter(spotlightAdapter)
                        recyclerview_spotlight.setNestedScrollingEnabled(false)
                    }
                })


    }

    companion object {

        fun getImgStr(htmlStr: String): Set<String> {
            val pics = HashSet<String>()
            var img = ""
            val p_image: Pattern
            val m_image: Matcher
            //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
            val regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>"
            p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE)
            m_image = p_image.matcher(htmlStr)
            while (m_image.find()) {
                img = m_image.group()
                val m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img)
                while (m.find()) {
                    pics.add(m.group(1))
                }
            }
            return pics
        }

        fun getImgStra(htmlStr: String): Set<String> {
            val pics = HashSet<String>()
            val doc = Jsoup.parse(htmlStr)
            val elements = doc.body().allElements
            for (element in elements) {
                pics.add(element.select("a").attr("href"))
            }
            return pics
        }
    }
}
