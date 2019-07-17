package com.perol.asdpl.pixivez.networks
import android.util.Log
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
class RubySSLSocketFactory : SSLSocketFactory() {
    var hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
    private val conn: HttpsURLConnection? = null
    override fun getDefaultCipherSuites(): Array<String> {
        return arrayOf<String>()
    }

    override fun createSocket(plainSocket: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        val address = plainSocket!!.inetAddress
        Log.d("address", address.hostAddress)
        if (autoClose) {
            plainSocket.close()
        }
        val sslSocketFactory = getDefault()
        val ssl = sslSocketFactory.createSocket(address, port) as SSLSocket
        ssl.enabledProtocols = ssl.supportedProtocols
        val session = ssl.session
        Log.i("!", "Protocol: " + session.protocol + " PeerHost: " + session.peerHost +
                " CipherSuite: " + session.cipherSuite);
        return ssl
    }

    override fun createSocket(p0: String?, p1: Int): Socket? {
        return null;
    }

    override fun createSocket(p0: String?, p1: Int, p2: InetAddress?, p3: Int): Socket? {
        return null;
    }

    override fun createSocket(p0: InetAddress?, p1: Int): Socket? {
        return null;
    }

    override fun createSocket(p0: InetAddress?, p1: Int, p2: InetAddress?, p3: Int): Socket? {
        return null;
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return arrayOf<String>()
    }}