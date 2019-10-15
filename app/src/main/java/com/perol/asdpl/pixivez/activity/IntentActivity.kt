package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import com.perol.asdpl.pixivez.objects.Toasty

class IntentActivity : RinkActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_intent);
        val uri = intent.data
        if (uri != null) {
            val segment = uri.pathSegments
            if (uri.path?.contains("artworks") == true) {
                val id = segment[segment.size - 1].toLong()
                val bundle = Bundle()
                val arrayList = LongArray(1)
                try {
                    arrayList[arrayList.size - 1] = id
                    bundle.putLongArray("illustlist", arrayList)
                    bundle.putLong("illustid", id)
                    val intent2 = Intent(this, PictureActivity::class.java)
                    intent2.putExtras(bundle)
                    startActivity(intent2)
                    finish()
                    return
                } catch (e: Exception) {
                    Toasty.error(this, "wrong id")
                }
                return
            }
            val test1 = uri.getQueryParameter("illust_id")

            if (test1 != null) {
                val bundle = Bundle()
                val arrayList = LongArray(1)
                try {
                    arrayList[arrayList.size - 1] = test1.toLong()
                    bundle.putLongArray("illustlist", arrayList)
                    bundle.putLong("illustid", test1.toLong())
                    val intent2 = Intent(this, PictureActivity::class.java)
                    intent2.putExtras(bundle)
                    startActivity(intent2)
                    finish()
                    return
                } catch (e: Exception) {
                    Toasty.error(this, "wrong id")
                }

            }
            var id = uri.getQueryParameter("id")
            if (uri.encodedSchemeSpecificPart.contains("//www.pixiv.net/fanbox/creator/")) {
                id = uri.encodedSchemeSpecificPart.replace("//www.pixiv.net/fanbox/creator/", "")
            }
            if (id != null) {

                try {
                    val intent1 = Intent(this, UserMActivity::class.java)
                    intent1.putExtra("data",id.toLong())
                    startActivity(intent1)
                    finish()
                } catch (e: Exception) {
                    Toasty.error(this, "wrong id")
                }

            }
        }


    }


}
