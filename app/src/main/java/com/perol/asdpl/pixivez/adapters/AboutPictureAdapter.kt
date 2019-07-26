package com.perol.asdpl.pixivez.adapters

import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.GlideApp

class AboutPictureAdapter(layoutResId: Int) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: String) {
        val imageView = helper.getView<ImageView>(R.id.imageview_aboutpic)
        if (helper.layoutPosition%2!=0)
        GlideApp.with(imageView.context).load(item).placeholder(R.color.white).transition(withCrossFade()).centerInside().into(imageView)
        else
        GlideApp.with(imageView.context).load(item).placeholder(R.color.gray).transition(withCrossFade()).centerInside().into(imageView)
    }
}
