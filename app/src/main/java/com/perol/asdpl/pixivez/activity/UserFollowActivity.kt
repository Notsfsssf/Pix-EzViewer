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

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.services.AppApiPixivService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_follow.*
import kotlinx.coroutines.runBlocking

class UserFollowActivity : RinkActivity() {

    private var userShowAdapter: UserShowAdapter? = null
    private var Next_url: String? = null
    private var recyclerviewusersearch: RecyclerView? = null
    private var restClient: RestClient? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var Authorization: String? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var appApiPixivService: AppApiPixivService? = null
    private val username: String? = null
    private var bundle: Bundle? = null
    private var userid: Long = 0
    private var restrict = "public"
    private var data: SearchUserResponse? = null
    private var pastdata: SearchUserResponse? = null
    internal var isfollower: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_user_follow)
        bundle = this.intent.extras
        userid = bundle!!.getLong("user")
        isfollower = bundle!!.getBoolean("isfollower", false)
        setSupportActionBar(toolbar_userfollow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("关注")
        initdata()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initdata() {

        restClient = RestClient()
        sharedPreferencesServices = SharedPreferencesServices(applicationContext)
        runBlocking {
            Authorization = AppDataRepository.getUser().Authorization
        }
        spinner.setVisibility(View.GONE)

        linearLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        appApiPixivService = restClient!!.retrofit_AppAPI
                .create(AppApiPixivService::class.java)

        recyclerviewusersearch = findViewById(R.id.recyclerview_usersearch)
        recyclerviewusersearch!!.layoutManager = linearLayoutManager
        //        appApiPixivService.getUserFollower(Authorization,userid)
        Observable.just(1).flatMap {
            if (isfollower!!) {
                appApiPixivService!!.getUserFollower(Authorization!!, userid)

            } else
                appApiPixivService!!.getUserFollowing(Authorization!!, userid, restrict)
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<SearchUserResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(searchUserResponse: SearchUserResponse) {
                        pastdata = searchUserResponse
                        data = searchUserResponse
                        Next_url = searchUserResponse.next_url
                        userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)
                        userShowAdapter!!.setNewData(searchUserResponse.user_previews)
                        recyclerviewusersearch!!.adapter = userShowAdapter
                        runBlocking {
                            val user = AppDataRepository.getUser()
                            if (userid == user.userid && !isfollower!!) {
                                spinner.visibility = View.VISIBLE
                                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                        when (position) {
                                            0 -> {
                                                restrict = "public"
                                                againrefresh()
                                            }
                                            1 -> {
                                                restrict = "private"
                                                againrefresh()
                                            }
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>) {

                                    }
                                }

                            } else {
                                spinner.setVisibility(View.GONE)
                            }
                        }

                        userShowAdapter!!.setOnLoadMoreListener({
                            if (Next_url != null) {
                                appApiPixivService!!.getNextUser(Authorization!!, Next_url!!).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                                        .subscribe(object : Observer<SearchUserResponse> {
                                            override fun onSubscribe(d: Disposable) {

                                            }

                                            override fun onNext(searchUserResponse: SearchUserResponse) {
                                                Next_url = searchUserResponse.next_url
                                                userShowAdapter!!.addData(searchUserResponse.user_previews)
                                            }

                                            override fun onError(e: Throwable) {
                                                userShowAdapter!!.loadMoreFail()
                                            }

                                            override fun onComplete() {
                                                userShowAdapter!!.loadMoreComplete()
                                            }
                                        })
                            } else {
                                userShowAdapter!!.loadMoreEnd()
                            }
                        }, recyclerviewusersearch)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }

    private fun againrefresh() {
        appApiPixivService!!.getUserFollowing(Authorization!!, userid, restrict).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<SearchUserResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(searchUserResponse: SearchUserResponse) {
                        data = searchUserResponse
                        Next_url = searchUserResponse.next_url
                        userShowAdapter!!.setNewData(data!!.user_previews)


                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }
}
