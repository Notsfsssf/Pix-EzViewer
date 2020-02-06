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

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Pair
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.services.Works


class RankingAdapter(layoutResId: Int, data: List<Illust>?, private val R18on: Boolean) : BaseQuickAdapter<Illust, BaseViewHolder>(layoutResId, data), BaseQuickAdapter.OnItemClickListener {


    val retrofit = RetrofitRespository.getInstance()

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bundle = Bundle()
        bundle.putLong("illustid", data[position].id)
        val illustlist = LongArray(this.data.count())
        for (i in this.data.indices) {
            illustlist[i] = this.data[i].id
        }
        bundle.putLongArray("illustlist", illustlist)
      //  bundle.putParcelable(this.data[position].id.toString(), this.data[position])
        val intent = Intent(mContext, PictureActivity::class.java)
        intent.putExtras(bundle)
        if (PxEZApp.animationEnable) {
            val mainimage = view!!.findViewById<View>(R.id.item_img)
            val title = view.findViewById<View>(R.id.textview_title)
            val userImage = view.findViewById<View>(R.id.imageview_user)

            val options = ActivityOptions.makeSceneTransitionAnimation(
                mContext as Activity,
                Pair.create(
                    mainimage,
                    "mainimage"
                ),
                Pair.create(title, "title"),
                Pair.create(userImage, "userimage")
            )
            ContextCompat.startActivity(mContext, intent, options.toBundle())
        } else ContextCompat.startActivity(mContext, intent, null)
    }


    init {

        this.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        this.onItemClickListener = this

    }


    override fun convert(helper: BaseViewHolder, item: Illust) {
        val constraintLayout = helper.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_num)
        val typedValue = TypedValue()
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        val colorPrimary = typedValue.resourceId;
        when (item.type) {
            "illust" -> if (item.meta_pages.isEmpty()) {
                constraintLayout.visibility = View.INVISIBLE
            } else if (!item.meta_pages.isEmpty()) {
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
        val imageView = helper.getView<ImageView>(R.id.item_img)
        imageView.setTag(R.id.tag_first, item.image_urls.medium)
        val imageViewuser = helper.getView<ImageView>(R.id.imageview_user)
        imageViewuser.setOnClickListener {
            val intent = Intent(mContext, UserMActivity::class.java)
            intent.putExtra("data", item.user.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)

        }
        imageViewuser.setTag(R.id.tag_first, item.user.profile_image_urls.medium)
        helper.setText(R.id.textview_title, item.title).setTextColor(R.id.textview_context, ContextCompat.getColor(mContext, colorPrimary))
        helper.setText(R.id.textview_context, item.user.name)
        helper.setTextColor(R.id.like, if (item.is_bookmarked) {
            Color.YELLOW
        } else {
            ContextCompat.getColor(mContext, colorPrimary)
        })
                .setOnClickListener(R.id.save) {
                    val illust = item
                    Works.imageDownloadAll(illust)
                }
                .setOnLongClickListener(R.id.save) {
                    Works.imageDownloadAll(item)
                    true
                }
                .setOnClickListener(R.id.like) { v ->
                    val textView = v as TextView
                    if (item.is_bookmarked) {
                        retrofit.postUnlikeIllust(item.id).subscribe({
                            textView.setTextColor(ContextCompat.getColor(mContext, colorPrimary))
                            item.is_bookmarked = false
                        }, {}, {})
                    } else {
                        retrofit.postLikeIllust(item.id)!!.subscribe({

                            textView.setTextColor(Color.YELLOW)
                            item.is_bookmarked = true
                        }, {}, {})
                    }
                }


        GlideApp.with(imageViewuser.context).load(item.user.profile_image_urls.medium).circleCrop().into(object : ImageViewTarget<Drawable>(imageViewuser) {
            override fun setResource(resource: Drawable?) {
                imageViewuser.setImageDrawable(resource)
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                if (item.user.profile_image_urls.medium === imageViewuser.getTag(R.id.tag_first)) {
                    super.onResourceReady(resource, transition)

                }
            }
        })


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

                GlideApp.with(imageView.context).load(loadurl).transition(withCrossFade())
                    .placeholder(android.R.color.white)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : ImageViewTarget<Drawable>(imageView) {
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
        } else {
            GlideApp.with(imageView.context).load(loadurl).transition(withCrossFade())
                .placeholder(android.R.color.white)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(ContextCompat.getDrawable(imageView.context, R.drawable.ai))
                .into(object : ImageViewTarget<Drawable>(imageView) {
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
