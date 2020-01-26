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
import android.view.View
import android.widget.Toast
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.responses.PixivAccountsResponse
import com.perol.asdpl.pixivez.responses.PixivOAuthResponse
import com.perol.asdpl.pixivez.services.AccountPixivService
import com.perol.asdpl.pixivez.services.OAuthSecureService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_user.*
import java.util.*

class NewUserActivity : RinkActivity() {
    private val Authorization = "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8"
    private var accountPixivService: AccountPixivService? = null
    private var restClient: RestClient? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var oAuthSecureService: OAuthSecureService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        restClient = RestClient()
        accountPixivService = restClient!!.retrofitAccount.create(AccountPixivService::class.java)
        oAuthSecureService =
            restClient!!.getRetrofitOauthSecure().create(OAuthSecureService::class.java)
        sharedPreferencesServices = SharedPreferencesServices(applicationContext)
        button_login.setOnClickListener(View.OnClickListener {
            if (edittext_username.text.toString().isNotBlank()) {
                accountPixivService!!.createProvisionalAccount(edittext_username.getText().toString(), "pixiv_android_app_provisional_account", Authorization)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(object : Observer<PixivAccountsResponse> {
                            override fun onSubscribe(d: Disposable) {}

                            override fun onNext(pixivAccountsResponse: PixivAccountsResponse) {
                                sharedPreferencesServices!!.setString("Device_token", pixivAccountsResponse.body.device_token)
                                sharedPreferencesServices!!.setString("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY")
                                sharedPreferencesServices!!.setString("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP")
                                sharedPreferencesServices!!.setString("username", pixivAccountsResponse.body.user_account)
                                sharedPreferencesServices!!.setString("password", pixivAccountsResponse.body.password)
                                sharedPreferencesServices!!.setBoolean("isnone", true)

                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                            }

                            override fun onComplete() {
                                val map = HashMap<String, Any>()
                                map["client_id"] = "KzEZED7aC0vird8jWyHM38mXjNTY"
                                map["client_secret"] = "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP"
                                map["grant_type"] = "password"
                                map["username"] = sharedPreferencesServices!!.getString("username")
                                map["password"] = sharedPreferencesServices!!.getString("password")
                                map["device_token"] = sharedPreferencesServices!!.getString("Device_token")
                                oAuthSecureService!!.postAuthToken(map).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<PixivOAuthResponse> {
                                            override fun onSubscribe(d: Disposable) {

                                            }

                                            override fun onNext(pixivOAuthResponse: PixivOAuthResponse) {
                                                sharedPreferencesServices!!.setString("Device_token", pixivOAuthResponse.response.device_token)
                                                sharedPreferencesServices!!.setString("Refresh_token", pixivOAuthResponse.response.refresh_token)
                                                sharedPreferencesServices!!.setString("Authorization", "Bearer " + pixivOAuthResponse.response.access_token)
                                                sharedPreferencesServices!!.setString("userid", pixivOAuthResponse.response.user.id.toString())
                                            }

                                            override fun onError(e: Throwable) {
                                                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                                            }

                                            override fun onComplete() {
                                                Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_LONG).show()
                                                val intent = Intent(applicationContext, HelloMActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        })
                            }
                        })
            }
        })
    }
}
