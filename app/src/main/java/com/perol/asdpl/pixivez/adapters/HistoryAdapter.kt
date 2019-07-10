package com.perol.asdpl.pixivez.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.ybq.android.spinkit.SpinKitView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.sql.IllustBeanEntity

class HistoryAdapter(layoutResId: Int) : BaseQuickAdapter<IllustBeanEntity, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: IllustBeanEntity) {
        helper.addOnClickListener(R.id.item_img).setVisible(R.id.constraintLayout_num,false)
        val imageView=  helper.getView<ImageView>(R.id.item_img)
        GlideApp.with(imageView.context).load(item.imageurl).placeholder(R.color.white).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {

                return false
            }
        }).into(imageView)
    }


}
