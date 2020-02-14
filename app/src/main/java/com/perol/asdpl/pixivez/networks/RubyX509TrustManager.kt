package com.perol.asdpl.pixivez.networks

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class RubyX509TrustManager : X509TrustManager {
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
    }

    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}