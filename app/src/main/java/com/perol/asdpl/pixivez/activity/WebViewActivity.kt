package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : RinkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_web_view)
        webview.loadUrl(intent.getStringExtra("url"))
        webview.settings.blockNetworkImage=false
        webview.settings.javaScriptEnabled=true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
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
                if (segment.size==1 && request.url.toString().contains("/member.php?id=")) {
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
