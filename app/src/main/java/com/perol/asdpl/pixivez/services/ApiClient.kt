package com.perol.asdpl.pixivez.services

import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.networks.RubyHttpDns
import com.perol.asdpl.pixivez.networks.RubySSLSocketFactory
import com.perol.asdpl.pixivez.networks.RubyX509TrustManager
import com.perol.asdpl.pixivez.repository.AppDataRepository
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.HttpStatusException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


object ApiClient {
    private const val HashSalt =
        "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c"
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

    private fun encode(text: String): String {
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

    private val okHttpClient by lazy { OkHttpClient() }

    private val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = okHttpClient.newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("GlideInterceptor", message)
                }
            }).apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        val builder1 = OkHttpClient.Builder()
        builder1.addInterceptor { chain ->
            var Authorization = ""
            runBlocking {
                try {
                    Authorization = AppDataRepository.getUser().Authorization
                } catch (e: Exception) {
                }
            }
            val ISO8601DATETIMEFORMAT =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", local)
            val isoDate = ISO8601DATETIMEFORMAT.format(Date())
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .removeHeader("User-Agent")
                .addHeader(
                    "User-Agent",
                    "PixivAndroidApp/5.0.155 (Android ${android.os.Build.VERSION.RELEASE}; ${android.os.Build.MODEL})"
                )
                .addHeader("Authorization", Authorization)
                .addHeader("Accept-Language", "${local.language}_${local.country}")
                .addHeader("App-OS", "Android")
                .addHeader("App-OS-Version", "${android.os.Build.VERSION.RELEASE}")
                .header("App-Version", "5.0.166")
                .addHeader("X-Client-Time", isoDate)
                .addHeader("X-Client-Hash", encode("$isoDate$HashSalt"))

            val request = requestBuilder.build()
            val response = chain.proceed(request)

            response
        }
            .addInterceptor {

                val request = it.request()
                val response = it.proceed(request)
                if (response.code == 400 && response.body != null && response.body!!.string().contains(
                        "token"
                    )
                ) {

                }
                response
            }
            .addInterceptor(httpLoggingInterceptor)
        if (!PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance).getBoolean(
                "disableproxy",
                false
            )
        ) {
            builder1.sslSocketFactory(RubySSLSocketFactory(), RubyX509TrustManager())
                .hostnameVerifier(HostnameVerifier { p0, p1 -> true })
            builder1.dns(RubyHttpDns())
        }
        builder.client(client).build()
    }

    fun <T> createService(tClass: Class<T>): T {
        return retrofit.create(tClass)
    }
}