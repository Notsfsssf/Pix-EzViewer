package com.perol.asdpl.pixivez.adapters

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.services.GlideApp

/**
 * Created by asdpl on 2018/2/27.
 */

class UserShowAdapter(layoutResId: Int) : BaseQuickAdapter<SearchUserResponse.UserPreviewsBean, BaseViewHolder>(layoutResId), BaseQuickAdapter.OnItemClickListener {
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val intent = Intent(mContext, UserMActivity::class.java)
        intent.putExtra("data",this.data[position].user.id)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        mContext.startActivity(intent)
    }

    init {
    this.onItemClickListener=this
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
