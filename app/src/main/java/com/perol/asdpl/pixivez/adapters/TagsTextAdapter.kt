package com.perol.asdpl.pixivez.adapters

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.Tags

class TagsTextAdapter(layoutResId: Int) : BaseQuickAdapter<Tags, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: Tags) {
        helper.setText(R.id.name,item.name).setText(R.id.translated_name,item.translated_name)


    }
}