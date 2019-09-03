package com.perol.asdpl.pixivez.networks

import android.util.Log
import com.perol.asdpl.pixivez.services.OneZeroService
import okhttp3.Dns
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

class ImageHttpDns:Dns{
    var retrofit: Retrofit = Retrofit.Builder()
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
                if (oneZeroResponse.answer != null) {
                    if (oneZeroResponse.answer.isNotEmpty())
                        for (i in oneZeroResponse.answer) {
                            Log.d("dns",i.data)
                            list.addAll(InetAddress.getAllByName(i.data))
                        }
                } else {
                    list.addAll(Dns.SYSTEM.lookup(hostname))
                    if (list.isEmpty()){
                        list.add(InetAddress.getByName("210.140.92.136"))
                        list.add(InetAddress.getByName("210.140.92.139"))
                        list.add(InetAddress.getByName("210.140.92.144"))
                    }
                    return list
                }

            }
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        list.addAll(Dns.SYSTEM.lookup(hostname))
        if (list.isEmpty()){
            list.add(InetAddress.getByName("210.140.92.136"))
            list.add(InetAddress.getByName("210.140.92.139"))
            list.add(InetAddress.getByName("210.140.92.144"))
        }
        return list
    }

}