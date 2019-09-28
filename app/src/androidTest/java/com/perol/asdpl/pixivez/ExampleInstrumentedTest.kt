package com.perol.asdpl.pixivez

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.InstrumentationRegistry
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.services.GlideApp
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
        val appContext = InstrumentationRegistry.getTargetContext()
 GlideApp.with(appContext).load("https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_170.jpg")
    }
}
