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

package com.perol.asdpl.pixivez.objects

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.OAuthSecureService
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.UserEntity
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class ReFreshFunction : io.reactivex.functions.Function<Observable<Throwable>, ObservableSource<*>> {
    private var client_id: String? = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
    private var client_secret: String? = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    private var oAuthSecureService: OAuthSecureService? = null
    private var i = 0
    private val maxRetries = 3
    private var retryCount = 0

    constructor(context: Context) : super() {
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        this.oAuthSecureService = RestClient().getretrofit_OAuthSecure().create(OAuthSecureService::class.java)
    }

    private constructor() {
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        this.oAuthSecureService = RestClient().getretrofit_OAuthSecure().create(OAuthSecureService::class.java)
    }

    @Throws(Exception::class)
    override fun apply(throwableObservable: Observable<Throwable>): ObservableSource<*> {
        return throwableObservable.flatMap(Function<Throwable, ObservableSource<*>> { throwable ->
            if (throwable is TimeoutException || throwable is SocketTimeoutException
                    || throwable is ConnectException) {
                return@Function Observable.error<Any>(throwable)
            } else if (throwable is HttpException) {
                if (throwable.response()!!.code() == 400) {
                    if (++retryCount <= maxRetries) {
                        var userEntitys: UserEntity? = null
                        TToast.retoken(PxEZApp.instance)
                        runBlocking {
                            userEntitys = AppDataRepository.getUser()
                        }
                        return@Function refreshtoken(userEntitys!!)
                    } else
                        return@Function Observable.error<Any>(throwable)


                } else if (throwable.response()!!.code() == 404) {
                    if (i == 0) {
                        Log.d("d", throwable.response()!!.message())
                        Toasty.warning(PxEZApp.instance, "查找的id不存在" + throwable.response()!!.message(), Toast.LENGTH_SHORT).show()
                        i++
                    }
                    return@Function Observable.error<Any>(throwable)
                }
            }
            return@Function Observable.error<Any>(throwable)
        })

    }

    private fun refreshtoken(it: UserEntity): ObservableSource<*> {
        return oAuthSecureService!!.postRefreshAuthToken(client_id, client_secret, "refresh_token", it.Refresh_token,
                it.Device_token, true)
                .subscribeOn(Schedulers.io()).doOnNext { pixivOAuthResponse ->
                    sharedPreferencesServices!!.setBoolean("islogin", true)
                    val user = pixivOAuthResponse.response.user
                    val userEntity = UserEntity(user.profile_image_urls.px_170x170, java.lang.Long.parseLong(user.id), user.name, user.mail_address, user.isIs_premium,
                            pixivOAuthResponse.response.device_token, pixivOAuthResponse.response.refresh_token, "Bearer " + pixivOAuthResponse.response.access_token)
                    userEntity.Id = it.Id
                    runBlocking {
                        AppDataRepository.UpdateUser(userEntity)
                    }
                }

    }

    companion object {
        @Volatile
        private var instance: ReFreshFunction? = null

        fun getInstance(): ReFreshFunction =
                instance ?: synchronized(this) {
                    instance ?: ReFreshFunction().also { instance = it }
                }

    }


}

