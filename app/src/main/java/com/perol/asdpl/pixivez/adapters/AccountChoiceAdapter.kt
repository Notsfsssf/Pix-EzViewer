package com.perol.asdpl.pixivez.adapters

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.sql.UserEntity
import kotlinx.android.synthetic.main.view_account_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking

class AccountChoiceAdapter(layoutResId: Int, data: List<UserEntity>,val numForNow:Int) : BaseQuickAdapter<UserEntity, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: UserEntity) {
        val userImage = helper.getView<ImageView>(R.id.imageView4)
        Glide.with(mContext).load(item.userimage).circleCrop().into(userImage)
        helper.setImageResource(R.id.imageview_delete, R.drawable.ic_action_lajitong)
                .setText(R.id.textView4, item.username)
                .setText(R.id.textview_email, item.useremail)
        val delete = helper.getView<ImageView>(R.id.imageview_delete)
        if (helper.layoutPosition==numForNow){
            helper.setVisible(R.id.imageview_delete,false)
        }
        delete.colorFilter=LightingColorFilter(Color.BLACK,Color.BLACK)
        delete.setOnClickListener {
            runBlocking {
                AppDataRepository.deleteUser(item)
                data -= item
                this@AccountChoiceAdapter.setNewData(data)
            }
        }
    }
}