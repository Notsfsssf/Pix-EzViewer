package com.perol.asdpl.pixivez.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.Illust
import java.io.File

class  PathProviderAdapter (layoutResId: Int, data: List<File>) : BaseQuickAdapter<File, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder, item: File) {
        helper.setText(R.id.path_textview,item.name).setImageResource(R.id.imageView2,R.drawable.ic_action_folder)
    }

}