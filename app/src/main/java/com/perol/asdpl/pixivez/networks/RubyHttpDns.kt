package com.perol.asdpl.pixivez.networks

import com.perol.asdpl.pixivez.services.OneZeroService
import okhttp3.Dns
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

/**
 * Created by Perol_Notsf on 2019/1/27.
 */
class RubyHttpDns : Dns {

    val list = ArrayList<InetAddress>()



    override fun lookup(hostname: String): List<InetAddress> {
        if (list.isNotEmpty()) {
            return list
        }
        try {
            val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://1.0.0.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val response = retrofit.create(OneZeroService::class.java).getItem("application/dns-json", hostname, "A", "false", "false").execute()
            val oneZeroResponse = response.body()
            if (oneZeroResponse != null) {
                if (oneZeroResponse.answer != null) {
                    if (oneZeroResponse.answer.isNotEmpty())
                        for (i in oneZeroResponse.answer) {
                            list.addAll(InetAddress.getAllByName(i.data))
                        }
                } else {
                    list.add(InetAddress.getByName("210.140.131.222"))
                    list.add(InetAddress.getByName("210.140.131.219"))
                    list.add(InetAddress.getByName("210.140.131.224"))
                    return list
                }

            }
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        list.add(InetAddress.getByName("210.140.131.222"))
        list.add(InetAddress.getByName("210.140.131.219"))
        list.add(InetAddress.getByName("210.140.131.224"))
        return list
    }

}