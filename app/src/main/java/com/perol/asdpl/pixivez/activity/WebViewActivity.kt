/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlinx.android.synthetic.main.activity_web_view.*
import java.io.ByteArrayInputStream
import java.util.*


class WebViewActivity : RinkActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_web_view)
        val local = when (PxEZApp.language) {
            1 -> {
                "en"
            }
            2 -> {
                "zh"
            }
            3 -> {
                "ja"
            }
            else -> {
                "zh"
            }
        }

//        val additionalHttpHeaders = hashMapOf<String,String>("Accept-Language" to local.displayLanguage)
//        "Accept-Language": "zh-CN"
        webview.loadUrl(intent.getStringExtra("url")!!.replace("ja", local))
        webview.settings.blockNetworkImage = false
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                if (request.url.host!!.equals("d.pixiv.org") || request.url.host!!.equals("connect.facebook.net") || request.url.host!!.equals(
                        "platform.twitter.com"
                    ) || request.url.host!!.equals("www.google-analytics.com")
                ) {
                    val webResourceResponse =
                        WebResourceResponse(
                            "application/javascript",
                            "UTF-8",
                            ByteArrayInputStream("".toByteArray())
                        );
                    return webResourceResponse;
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val segment = request.url.pathSegments
                if (segment.size == 2) {
                    if (segment[0] == "artworks") {
                        val id = segment[1].toLong()
                        val bundle = Bundle()
                        val arrayList = LongArray(1)
                        arrayList[0] = id
                        bundle.putLongArray("illustlist", arrayList)
                        bundle.putLong("illustid", id)
                        val intent = Intent(this@WebViewActivity, PictureActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        return true
                    }

                }
                if (segment.size == 1 && request.url.toString().contains("/member.php?id=")) {
                    val userId = request.url.getQueryParameter("id")
                    val intent = Intent(this@WebViewActivity, UserMActivity::class.java)
                    intent.putExtra("data", userId?.toLong())
                    startActivity(intent)
                    return true
                }
                return false

            }
        }
        fab.setOnClickListener {
            finish()
        }
    }
}
