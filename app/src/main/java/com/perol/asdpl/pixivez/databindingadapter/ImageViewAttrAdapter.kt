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
    if (url!=null)
    GlideApp.with(imageView.context).load(url).transition(withCrossFade()).error(R.drawable.chobi01).placeholder(R.drawable.chobi01).into(imageView)
    else
    {
        GlideApp.with(imageView).load(R.drawable.chobi01).into(imageView)
    }
}







