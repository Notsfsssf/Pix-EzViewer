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
            if (segment.size == 2) {
                if (segment[segment.size - 2] == "u") {
                    val id = segment[segment.size - 1].toLong()
                    try {
                        val intent1 = Intent(this, UserMActivity::class.java)
                        intent1.putExtra("data", id)
                        startActivity(intent1)
                        finish()
                        return
                    } catch (e: Exception) {
                        Toasty.error(this, "wrong id")
                    }
                }
                if (segment[segment.size - 2] == "i") {
                    val id = segment[segment.size - 1].toLong()
                    val bundle = Bundle()
                    val arrayList = LongArray(1)
                    try {
                        arrayList[0] = id
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
                }
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
            if (uri.encodedSchemeSpecificPart.contains("/fanbox/creator/")) {
                id = uri.pathSegments[uri.pathSegments.size - 1]
            }
            if (id != null) {

                try {
                    val intent1 = Intent(this, UserMActivity::class.java)
                    intent1.putExtra("data", id.toLong())
                    startActivity(intent1)
                    finish()
                } catch (e: Exception) {
                    Toasty.error(this, "wrong id")
                }

            }

        }


    }


}
