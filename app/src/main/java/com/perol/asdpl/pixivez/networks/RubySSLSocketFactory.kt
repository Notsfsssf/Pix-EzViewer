/*MIT License

Copyright (c) 2019 Perol_Notsfsssf

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

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