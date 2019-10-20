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

package com.perol.asdpl.pixivez.databindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.GlideApp

@BindingAdapter("userUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null)
        if (url.contentEquals("https://source.pixiv.net/common/images/no_profile.png")) {
            GlideApp.with(imageView.context).load(R.mipmap.ic_noimage_round).circleCrop().transition(withCrossFade()).into(imageView)
        } else
            GlideApp.with(imageView.context)
                    .load(url)
                    .placeholder(R.mipmap.ic_noimage)
                    .circleCrop()
                    .error(R.mipmap.ic_noimage)
                    .into(imageView)
}

@BindingAdapter("url")
fun GlideLoadImage(imageView: ImageView, url: String?) {
    if (url != null)
        GlideApp.with(imageView.context).load(url).transition(withCrossFade()).error(R.drawable.chobi01).placeholder(R.drawable.chobi01).into(imageView)
    else {
        GlideApp.with(imageView).load(R.drawable.chobi01).into(imageView)
    }
}







