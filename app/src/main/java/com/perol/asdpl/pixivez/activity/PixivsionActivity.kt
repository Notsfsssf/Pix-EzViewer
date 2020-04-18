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
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PixiviSionAdapter
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ReFreshFunction
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.SpotlightResponse
import com.perol.asdpl.pixivez.services.AppApiPixivService
import com.perol.asdpl.pixivez.services.PxEZApp
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pixivision.*
import kotlinx.coroutines.runBlocking

class PixivsionActivity : RinkActivity() {


    lateinit var appApiPixivService: AppApiPixivService
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    private var Authorization: String? = null
    private var Nexturl: String? = null
    private var data: SpotlightResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_pixivision)
        setSupportActionBar(toobar_pixivision)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        runBlocking {
            Authorization = AppDataRepository.getUser().Authorization
        }
        initbind()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initbind() {

        val restClient = RestClient()
        appApiPixivService = restClient.pixivisionAppApi.create(AppApiPixivService::class.java)
        Observable.just(1).flatMap {
            runBlocking {
                Authorization = AppDataRepository.getUser().Authorization
            }
            appApiPixivService.getPixivisionArticles(Authorization!!, "all")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .retryWhen(ReFreshFunction(applicationContext))
                .subscribe(object : Observer<SpotlightResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(spotlightResponse: SpotlightResponse) {
                        data = spotlightResponse
                        Nexturl = spotlightResponse.next_url
                        val pixiviSionAdapter = PixiviSionAdapter(R.layout.view_pixivision_item, spotlightResponse.spotlight_articles, this@PixivsionActivity)
                        recyclerview_pixivision.layoutManager = LinearLayoutManager(applicationContext)
                        recyclerview_pixivision.adapter = pixiviSionAdapter
                        pixiviSionAdapter.loadMoreModule?.setOnLoadMoreListener {
                            Observable.just(1).flatMap {
                                runBlocking {
                                    Authorization = AppDataRepository.getUser().Authorization
                                }
                                appApiPixivService.getNextPixivisionArticles(
                                    Authorization!!,
                                    Nexturl!!
                                )
                            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .retryWhen(ReFreshFunction(applicationContext))
                                    .subscribe(object : Observer<SpotlightResponse> {
                                        override fun onSubscribe(d: Disposable) {

                                        }

                                        override fun onNext(spotlightResponse: SpotlightResponse) {
                                            Nexturl = spotlightResponse.next_url
                                            pixiviSionAdapter.addData(spotlightResponse.spotlight_articles)
                                        }

                                        override fun onError(e: Throwable) {
                                            pixiviSionAdapter.loadMoreModule?.loadMoreFail()
                                        }

                                        override fun onComplete() {
                                            pixiviSionAdapter.loadMoreModule?.loadMoreComplete()
                                        }
                                    })
                        }
                        pixiviSionAdapter.addChildClickViewIds(R.id.imageView_pixivision)
                        pixiviSionAdapter.setOnItemChildClickListener { adapter, view, position ->
                            val intent = Intent(this@PixivsionActivity, WebViewActivity::class.java)
                            intent.putExtra("url", data!!.spotlight_articles[position].article_url)
                            startActivity(intent)
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {

                    }
                })
    }
}
