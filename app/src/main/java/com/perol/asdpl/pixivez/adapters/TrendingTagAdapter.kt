package com.perol.asdpl.pixivez.adapters

import android.widget.ImageView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.TrendingtagResponse
import com.perol.asdpl.pixivez.services.GlideApp


/**
 * Created by asdpl on 2018/2/20.
 */

class TrendingTagAdapter(layoutResId: Int, data: List<TrendingtagResponse.TrendTagsBean>?) : BaseQuickAdapter<TrendingtagResponse.TrendTagsBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: TrendingtagResponse.TrendTagsBean) {
        helper.setText(R.id.textview_tag, item.tag)
        val imageView = helper.itemView.findViewById<ImageView>(R.id.imageview_trendingtag)
        GlideApp.with(imageView.context).load(item.illust.image_urls.square_medium).placeholder(R.mipmap.ic_noimage).skipMemoryCache(true).into(imageView)
    }


}
