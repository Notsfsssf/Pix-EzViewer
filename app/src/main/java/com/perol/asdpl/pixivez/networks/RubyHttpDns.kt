package com.perol.asdpl.pixivez.networks

import android.util.Log
import com.perol.asdpl.pixivez.services.OneZeroService
import okhttp3.Dns
import java.net.InetAddress

import java.io.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RubyHttpDns : Dns {
    var retrofit = Retrofit.Builder()
            .baseUrl("https://1.0.0.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    var oneZeroService: OneZeroService
    val list = ArrayList<InetAddress>()

    init {

        oneZeroService = retrofit.create(OneZeroService::class.java)
    }

    override fun lookup(hostname: String): List<InetAddress> {
        if (list.isNotEmpty()) {
            return list
        }
        try {
            val response = oneZeroService.getItem("application/dns-json", hostname, "A", "false", "false").execute()
            val oneZeroResponse = response.body()
            if (oneZeroResponse != null) {
                for (i in oneZeroResponse.answer) {
                    list.addAll(InetAddress.getAllByName(i.data))
                }

            }
            return list
        } catch (e: Exception) {

        }
        list.add(InetAddress.getByName("210.140.131.222"))
        return list
    }

}