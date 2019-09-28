package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
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


    private var restClient: RestClient? = null
    private var appApiPixivService: AppApiPixivService? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
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

        restClient = RestClient()
        appApiPixivService = restClient!!.pixiviSion_AppAPI.create(AppApiPixivService::class.java)
        Observable.just(1).flatMap(object : Function<Int, ObservableSource<SpotlightResponse>> {
            override fun apply(t: Int): ObservableSource<SpotlightResponse> {
                runBlocking {
                    Authorization = AppDataRepository.getUser().Authorization
                }
                return appApiPixivService!!.getPixivisionArticles(Authorization!!, "all")
            }


        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        pixiviSionAdapter.setOnLoadMoreListener({
                            Observable.just(1).flatMap {
                                runBlocking {
                                    Authorization = AppDataRepository.getUser().Authorization
                                }
                                appApiPixivService!!.getNextPixivisionArticles(Authorization!!, Nexturl!!)
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
                                            pixiviSionAdapter.loadMoreFail()
                                        }

                                        override fun onComplete() {
                                            pixiviSionAdapter.loadMoreComplete()
                                        }
                                    })
                        },recyclerview_pixivision)
                        pixiviSionAdapter.setOnItemChildClickListener { adapter, view, position ->
                            val intent = Intent(applicationContext, WebViewActivity::class.java)
                            intent.putExtra("url", data!!.spotlight_articles[position].article_url.replace("ja", if(PxEZApp.locale=="zh"){"zh"}else{"en"}))
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
