package com.perol.asdpl.pixivez.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
import com.chad.library.adapter.base.BaseViewHolder
import com.github.ybq.android.spinkit.SpinKitView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.services.Works


/**
 * Created by asdpl on 2018/2/11.
 */


class RecommendAdapter(layoutResId: Int, data: List<Illust>?, private val R18on: Boolean) : BaseQuickAdapter<Illust, BaseViewHolder>(layoutResId, data) {
    val retrofit = RetrofitRespository.getInstance()

    init {

        this.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        this.onItemClickListener = OnItemClickListener { adapter, view, position ->
            val bundle = Bundle()
            bundle.putLong("illustid", this@RecommendAdapter.data[position].id)
            val illustlist = LongArray(this.data.count())
            for (i in this.data.indices) {
                illustlist[i] = this.data[i].id
            }
            bundle.putLongArray("illustlist", illustlist)
            val intent = Intent(mContext, PictureActivity::class.java)
            intent.putExtras(bundle)
         //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (PxEZApp.animationEnable) {
                val mainimage = view!!.findViewById<View>(R.id.item_img)
                val optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext as Activity, mainimage, "mainimage");
                startActivity(mContext, intent, optionsCompat.toBundle())
            } else
                startActivity(mContext,intent,null)
        }


    }


    override fun convert(helper: BaseViewHolder, item: Illust) {
        val typedValue = TypedValue();
        mContext.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        val colorPrimary = typedValue.resourceId;

        helper.setText(R.id.title, item.title).setText(R.id.detail, item.caption).setTextColor(R.id.like, if (item.is_bookmarked) {
            ContextCompat.getColor(mContext, R.color.yellow)
        } else {
            ContextCompat.getColor(mContext, colorPrimary)
        })
                .setOnClickListener(R.id.save) {
                  Works.ImageDownloadAll(item)
                }
                .setOnClickListener(R.id.like) { v ->
                    val textView = v as Button
                    if (item.is_bookmarked) {
                        retrofit.postUnlikeIllust(item.id.toLong())!!.subscribe({
                            textView.setTextColor(ContextCompat.getColor(mContext, colorPrimary))
                            item.is_bookmarked = false
                        }, {}, {})
                    } else {
                        retrofit.postLikeIllust(item.id)!!.subscribe({

                            textView.setTextColor(ContextCompat.getColor(mContext, R.color.yellow))
                            item.is_bookmarked = true
                        }, {}, {})
                    }
                }

        val constraintLayout = helper.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_num)
        val imageView = helper.getView<ImageView>(R.id.item_img)
        when (item.type) {
            "illust" -> if (item.meta_pages.isEmpty()) {
                constraintLayout.visibility = View.INVISIBLE
            } else if (item.meta_pages.isNotEmpty()) {
                constraintLayout.visibility = View.VISIBLE
                helper.setText(R.id.textview_num, item.meta_pages.size.toString())
            }
            "ugoira" -> {
                constraintLayout.visibility = View.VISIBLE
                helper.setText(R.id.textview_num, "Gif")
            }
            else -> {
                constraintLayout.visibility = View.VISIBLE
                helper.setText(R.id.textview_num, "CoM")
            }
        }
        imageView.setTag(R.id.tag_first, item.image_urls.medium)

        val needsmall = item.height > 1500 || item.height > 1500
        val loadurl = if (needsmall) {
            item.image_urls.square_medium
        } else {
            item.image_urls.medium
        }
        if (!R18on) {
            var isr18 = false
            for (i in item.tags) {
                if (i.name == "R-18" || i.name == "R-18G") {
                    isr18 = true
                    break
                }
            }
            if (isr18) {
                GlideApp.with(imageView.context).load(ContextCompat.getDrawable(mContext, R.drawable.h)).placeholder(R.drawable.h).into(imageView)
            } else {
                GlideApp.with(imageView.context).load(loadurl)
                        .transition(withCrossFade()).placeholder(R.color.white)
                        .into(object : ImageViewTarget<Drawable>(imageView) {

                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

                                if (imageView.getTag(R.id.tag_first) === item.image_urls.medium) {
                                    super.onResourceReady(resource, transition)
                                }


                            }

                            override fun setResource(resource: Drawable?) {

                                imageView.setImageDrawable(resource)
                            }
                        }
                        )

            }
        } else {

            GlideApp.with(imageView.context).load(loadurl).transition(withCrossFade()).placeholder(R.color.white).error(ContextCompat.getDrawable(imageView.context, R.drawable.ai)).into(object : ImageViewTarget<Drawable>(imageView) {
                override fun setResource(resource: Drawable?) {
                    imageView.setImageDrawable(resource)
                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    if (imageView.getTag(R.id.tag_first) === item.image_urls.medium) {
                        super.onResourceReady(resource, transition)
                    }

                }
            })
        }

    }


}



