/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.adapters

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.objects.Spotlight
import com.perol.asdpl.pixivez.services.GlideApp

class SpotlightAdapter(layoutResId: Int, data: List<Spotlight>?) :
    BaseQuickAdapter<Spotlight, BaseViewHolder>(layoutResId, data?.toMutableList()) {



    override fun convert(helper: BaseViewHolder, item: Spotlight) {
        val constraintLayout = helper.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_num)
        constraintLayout.visibility = View.GONE
        val userImage = helper.getView<ImageView>(R.id.imageview_user)
        val mainImage = helper.getView<ImageView>(R.id.item_img)
        helper.setText(R.id.textview_context, item.username)
                .setText(R.id.textview_title, item.title)
        GlideApp.with(mainImage.context).load(item.pictureurl).error(R.drawable.ai).transition(withCrossFade()).into(mainImage)
        GlideApp.with(userImage.context).load(item.userpic).transition(withCrossFade()).circleCrop().into(userImage)
        mainImage.setOnClickListener {
            val bundle = Bundle()
            val arrayList = LongArray(1)
            arrayList[0] = item.illustrateid.toLong()
            bundle.putLongArray("illustidlist", arrayList)
            bundle.putLong("illustid", item.illustrateid.toLong())
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        userImage.setOnClickListener {
            val intent = Intent(context, UserMActivity::class.java)
            val userid = item.userid.toLong()

            intent.putExtra("data", userid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
