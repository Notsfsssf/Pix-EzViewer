package com.perol.asdpl.pixivez.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.objects.Spotlight
import com.perol.asdpl.pixivez.services.GlideApp
import java.util.*

class SpotlightAdapter(layoutResId: Int, data: List<Spotlight>?, private val context: Context) : BaseQuickAdapter<Spotlight, BaseViewHolder>(layoutResId, data) {

    init {
        this.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    }

    override fun convert(helper: BaseViewHolder, item: Spotlight) {
        val constraintLayout = helper.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_num)
        constraintLayout.visibility = View.GONE
        val imageView = helper.getView<ImageView>(R.id.imageview_user)
        val imageView1 = helper.getView<ImageView>(R.id.item_img)
        helper.addOnClickListener(R.id.item_img).setText(R.id.textview_context, item.username)
                .setText(R.id.textview_title, item.title)
        GlideApp.with(imageView1.context).load(item.pictureurl).error(R.drawable.ai).transition(withCrossFade()).into(imageView1)
        GlideApp.with(imageView.context).load(item.userpic).transition(withCrossFade()).circleCrop().into(imageView)
        imageView1.setOnClickListener {
            val bundle = Bundle()
            val arrayList = LongArray(1)
            arrayList[0]=item.illustrateid.toLong()
            bundle.putLongArray("illustlist", arrayList)
            bundle.putLong("illustid", item.illustrateid.toLong())
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        imageView.setOnClickListener {
            val intent = Intent(context, UserMActivity::class.java)
            val userid = item.userid.toLong()

            intent.putExtra("data", userid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
