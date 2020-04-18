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
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.services.GlideApp


class UserShowAdapter(layoutResId: Int) :
    BaseQuickAdapter<SearchUserResponse.UserPreviewsBean, BaseViewHolder>(layoutResId),
    LoadMoreModule {


    init {
        setOnItemClickListener { adapter, view, position ->
            val intent = Intent(context, UserMActivity::class.java)
            intent.putExtra("data", this.data[position].user.id)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(intent)
        }
    }

    override fun convert(helper: BaseViewHolder, item: SearchUserResponse.UserPreviewsBean) {
        val linearLayoutManager = LinearLayoutManager(helper.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val userSearchillustAdapter = UserSearchIllustAdapter(R.layout.view_usersearchillust_item, item.illusts)
        //        helper.addOnClickListener(R.id.cardview_recommand).addOnClickListener(R.id.imageview_usershow).addOnClickListener(R.id.textview_usershowname);
        val recyclerView = helper.getView<RecyclerView>(R.id.recyclerview_usershow)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = userSearchillustAdapter
        val name = item.user.name
        helper.setText(R.id.textview_usershowname, name)
        val imageView = helper.getView<ImageView>(R.id.imageview_usershow)
        GlideApp.with(imageView.context).load(item.user.profile_image_urls.medium).circleCrop().transition(withCrossFade()).into(imageView)

    }
}
