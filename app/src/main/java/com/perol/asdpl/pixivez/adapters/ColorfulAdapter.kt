package com.perol.asdpl.pixivez.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import io.multimoon.colorful.ThemeColorInterface
import java.util.*

class ColorfulAdapter(layoutResId: Int, data: ArrayList<ThemeColorInterface>) : BaseQuickAdapter<ThemeColorInterface, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ThemeColorInterface) {
            val imageView = helper.getView<ImageView>(R.id.imagevew_colorful)
            imageView.setBackgroundColor(item.getColorPack().normal().asInt())
    }
}