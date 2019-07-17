package com.perol.asdpl.pixivez

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.InstrumentationRegistry
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.OneZeroService
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
runBlocking {
  val result=  AppDataRepository.getAllUser()
    println("x:${result.size}")
}

    }
    @Test
    fun testRetrofit(){
        val okHttpClient=OkHttpClient.Builder().addInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder().build()
                println(request.url)
                return chain.proceed(request)
            }
        }).build()
        var retrofit = Retrofit.Builder()
                .baseUrl("https://1.0.0.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        var oneZeroService: OneZeroService= retrofit.create(OneZeroService::class.java)
        val response = oneZeroService.getItem("application/dns-json","oauth.secure.pixiv.net", "A", "false", "false").execute()
        val oneZeroResponse = response.body()
    }
}
