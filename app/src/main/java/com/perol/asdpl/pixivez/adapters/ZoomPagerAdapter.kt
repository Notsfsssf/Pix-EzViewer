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

package com.perol.asdpl.pixivez.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.view.*
import androidx.viewpager.widget.PagerAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.Works
import java.io.File
import java.util.*


class ZoomPagerAdapter(
    val context: Context,
    private val arrayList: ArrayList<String>,
    val illust: Illust?
) : PagerAdapter() {

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_pager_zoom, container, false)
        val photoView = view.findViewById<SubsamplingScaleImageView>(R.id.photoview_zoom)
        photoView.isEnabled = true
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if ((e2.rawY - e1.rawY) > dm.heightPixels / 4) {
                        (context as Activity).finish()
                    }
                    return false
                }
            })
        photoView.setOnTouchListener { v, event ->
            return@setOnTouchListener gestureDetector.onTouchEvent(event);
        }
        GlideApp.with(context).asFile().load(arrayList[position])
            .into(object : CustomTarget<File>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    photoView.setImage(ImageSource.uri(Uri.fromFile(resource)))
                    if (illust != null) {
                        photoView.setOnLongClickListener {
                            MaterialDialog(context).show {
                                title(R.string.saveselectpic1)
                                positiveButton(android.R.string.ok) {
                                    if (!illust.meta_pages.isNullOrEmpty()) {
                                        Works.imageDownloadWithFile(illust, resource, position)
                                    } else Works.imageDownloadWithFile(illust, resource, null)

                                }
                                negativeButton(android.R.string.cancel)
                            }

                            true
                        }

                    }
                }
            })
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
