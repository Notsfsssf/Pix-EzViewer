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
import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.SpotlightResponse.SpotlightArticlesBean
import com.perol.asdpl.pixivez.services.GlideApp

class PixiVisionAdapter(
    layoutResId: Int,
    data: MutableList<SpotlightArticlesBean>?,
    context: Context?
) : BaseQuickAdapter<SpotlightArticlesBean, BaseViewHolder>(layoutResId, data), LoadMoreModule {
    override fun convert(
        helper: BaseViewHolder,
        item: SpotlightArticlesBean
    ) {
        helper.setText(R.id.textView__pixivision_title, item.title)
        val imageView =
            helper.getView<ImageView>(R.id.imageView_pixivision)
        GlideApp.with(context).load(item.thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.halftrans)
            .transition(DrawableTransitionOptions.withCrossFade()).into(imageView)
    }
    @SuppressLint("InflateParams")
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        animationEnable = true
        setAnimationWithDefault(AnimationType.ScaleIn)
    }
}