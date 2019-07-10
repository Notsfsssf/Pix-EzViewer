package com.perol.asdpl.pixivez.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R

class TagsShowAdapter(layoutResId: Int, data: List<String>?,var counts: ArrayList<Int>) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.textview_tagname, item).setText(R.id.textView24, counts[helper.adapterPosition].toString())
    }
}
