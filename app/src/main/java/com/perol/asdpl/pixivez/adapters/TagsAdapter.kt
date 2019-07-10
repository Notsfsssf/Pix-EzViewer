package com.perol.asdpl.pixivez.adapters

import android.widget.CheckBox
import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import java.util.*
import kotlin.collections.HashMap

class TagsAdapter(layoutResId: Int, data: List<String>?, var checkStatus: HashMap<Int, Boolean>) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.textview_tag1, item)
                .setOnCheckedChangeListener(R.id.checkBox, null)
        helper.setChecked(R.id.checkBox, checkStatus[helper.adapterPosition]!!)
                .setOnCheckedChangeListener(R.id.checkBox) { buttonView, isChecked -> checkStatus.put(helper.adapterPosition, isChecked) }

    }
}
