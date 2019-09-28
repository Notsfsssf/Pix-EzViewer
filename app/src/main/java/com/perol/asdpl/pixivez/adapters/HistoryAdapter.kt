package com.perol.asdpl.pixivez.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.sql.IllustBeanEntity

class HistoryAdapter(layoutResId: Int) : BaseQuickAdapter<IllustBeanEntity, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: IllustBeanEntity) {
        val imageView=  helper.getView<ImageView>(R.id.item_img)
        GlideApp.with(imageView.context).load(item.imageurl).placeholder(R.color.white).into(imageView)
    }
}
