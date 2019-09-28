package com.perol.asdpl.pixivez.networks

import android.util.Log
import androidx.preference.PreferenceManager

import com.google.gson.GsonBuilder
import com.perol.asdpl.pixivez.services.PxEZApp
import okhttp3.*


import java.io.IOException

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
import okhttp3.OkHttpClient
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


/**
 * Created by asdpl on 2018/2/10.
 */

class RestClient {
    private val httpsDns = RubyHttpDns()
    private val local = when (PxEZApp.language) {
        1 -> {
            Locale.ENGLISH
        }
        2 -> {
            Locale.TRADITIONAL_CHINESE
        }
        else -> {
            Locale.SIMPLIFIED_CHINESE
        }
    }
    private val pixivOkHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                        .addHeader("Accept-Language", local.language)
                        .addHeader("User-Agent", "PixivIOSApp/5.8.0")
                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        })
        if (!PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getBoolean("disableproxy", false)) {
            builder.sslSocketFactory(RubySSLSocketFactory(), object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                }

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }).hostnameVerifier(HostnameVerifier { p0, p1 -> true })
            builder.dns(httpsDns)
        }
        val okHttpClient = builder.build()

        return@lazy okHttpClient
    }

    private val imageHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                            .addHeader("User-Agent", "PixivIOSApp/5.8.0")
                            .addHeader("referer", "https://app-api.pixiv.net/")
                    val request = requestBuilder.build()
                    return chain.proceed(request)
                }
            }).dns(ImageHttpDns())
            return builder.build()
        }


    private val gson = GsonBuilder()
            .create()
    val retrofit_AppAPI: Retrofit
        get() {
            val retrofit_OAuthSecure = Retrofit.Builder()
                    .baseUrl("https://app-api.pixiv.net")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit_OAuthSecure
        }

    val pixiviSion_AppAPI: Retrofit
        get() {
            val retrofit_AppAPI = Retrofit.Builder()
                    .baseUrl("https://app-api.pixiv.net/")
                    .client(pixivOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit_AppAPI
        }

    val retrofit_Account: Retrofit
        get() {
            val retrofit_AppAPI = Retrofit.Builder()
                    .baseUrl("https://accounts.pixiv.net/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit_AppAPI
        }
    private val HashSalt =
            "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c"

    fun encode(text: String): String {
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            val digest: ByteArray = instance.digest(text.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                val i: Int = b.toInt() and 0xff
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    private val okHttpClient: OkHttpClient
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("aaa", "message====$message")
                }
            })

            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()

            builder.addInterceptor(object : Interceptor {

                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {

                    val ISO8601DATETIMEFORMAT = SimpleDateFormat("yyyy/MM/dd 'T'HH:mmZ", local)
                    val isoDate = ISO8601DATETIMEFORMAT.format(Date())
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                            .removeHeader("User-Agent")
                            .addHeader("User-Agent", "PixivIOSApp/5.8.0")
                            .addHeader("Accept-Language", local.language)
                            .addHeader("X-Client-Time", isoDate)
                            .addHeader("X-Client-Hash", encode("$isoDate$HashSalt"))
                    val request = requestBuilder.build()
                    return chain.proceed(request)
                }
            })
                    .addInterceptor(httpLoggingInterceptor)
            if (!PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getBoolean("disableproxy", false)) {
                builder.sslSocketFactory(RubySSLSocketFactory(), object : X509TrustManager {
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                    }

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }).hostnameVerifier(HostnameVerifier { p0, p1 -> true })
                builder.dns(RubyHttpDns())
            }
            return builder
                    .build()
        }


    fun getretrofit_GIF(): Retrofit {

        val retrofit_OAuthSecure = Retrofit.Builder()
                .baseUrl("https://oauth.secure.pixiv.net/")
                .client(imageHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit_OAuthSecure
    }

    fun getretrofit_OAuthSecure(): Retrofit {


        val retrofit_OAuthSecure = Retrofit.Builder()
                .baseUrl("https://oauth.secure.pixiv.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit_OAuthSecure
    }


}
