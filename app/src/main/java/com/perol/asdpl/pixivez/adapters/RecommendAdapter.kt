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
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.services.Works
import org.jetbrains.anko.layoutInflater


/**
 * Created by asdpl on 2018/2/11.
 */


class RecommendAdapter(layoutResId: Int, data: List<IllustsBean>?, private val R18on: Boolean) : BaseQuickAdapter<IllustsBean, BaseViewHolder>(layoutResId, data) {
    val retrofit = RetrofitRespository.getInstance()

    init {

        this.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        this.onItemClickListener = OnItemClickListener { adapter, view, position ->
            val bundle = Bundle()
            bundle.putLong("illustid", this@RecommendAdapter.data[position].id)
            val illustlist = LongArray(this.data.count())
            for(i in this.data.indices){
                illustlist[i]=this.data[i].id
            }
            bundle.putLongArray("illustlist", illustlist)
            val intent = Intent(mContext, PictureActivity::class.java)
            intent.putExtras(bundle)
            if (!PxEZApp.isaddflag)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val mainimage = view!!.findViewById<View>(R.id.item_img)
            val pictitle = view!!.findViewById<View>(R.id.textview_title)
            val optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(mContext as Activity, mainimage, "mainimage");
            startActivity(mContext, intent, optionsCompat.toBundle())
        }


    }


    override fun convert(helper: BaseViewHolder, item: IllustsBean) {
//        helper.addOnClickListener(R.id.content)
//        helper.addOnLongClickListener(R.id.content)
        val typedValue = TypedValue();
        mContext.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        val colorPrimary = typedValue.resourceId;

        helper
                .setText(R.id.title, item.title).setText(R.id.detail, item.caption).setTextColor(R.id.like, if (item.isIs_bookmarked) {
                    ContextCompat.getColor(mContext, R.color.yellow)
                } else {
                    ContextCompat.getColor(mContext, colorPrimary)
                })
                .setOnClickListener(R.id.save, object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val illust = item
                        if (illust.meta_pages.isEmpty()) {
                            Works.ImageDownload(illust.meta_single_page.original_image_url, illust.user.id.toString(), illust.id.toString(), null, if (illust.meta_single_page.original_image_url.contains("png")) {
                                ".png"
                            } else ".jpg")
                        } else {
                            for (i in illust.meta_pages.indices) {
                                Works.ImageDownload(illust.meta_pages[i].image_urlsX.original, illust.user.id.toString(), illust.id.toString(), i.toString(), if (illust.meta_pages[i].image_urlsX.original.contains("png")) {
                                    ".png"
                                } else ".jpg")
                            }
                        }
                    }

                })
                .setOnLongClickListener(R.id.save, object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        val illust = item
                        if (illust.meta_pages.isEmpty()) {
                            Works.ImageDownload(illust.meta_single_page.original_image_url, illust.user.id.toString(), illust.id.toString(), null, if (illust.meta_single_page.original_image_url.contains("png")) {
                                ".png"
                            } else ".jpg")
                        } else {
                            for (i in illust.meta_pages.indices) {
                                Works.ImageDownload(illust.meta_pages[i].image_urlsX.original, illust.user.id.toString(), illust.id.toString(), i.toString(), if (illust.meta_pages[i].image_urlsX.original.contains("png")) {
                                    ".png"
                                } else ".jpg")
                            }
                        }
                        return true
                    }

                })
                .setOnClickListener(R.id.like, object : View.OnClickListener {
                    override fun onClick(v: View?) {

                        val textView = v as Button
                        if (item.isIs_bookmarked) {
                            retrofit.postUnlikeIllust(item.id.toLong())!!.subscribe({
                                textView.setTextColor(ContextCompat.getColor(mContext, colorPrimary))
                                item.isIs_bookmarked = false
                            }, {}, {})
                        } else {
                            retrofit.postLikeIllust(item.id)!!.subscribe({

                                textView.setTextColor(ContextCompat.getColor(mContext, R.color.yellow))
                                item.isIs_bookmarked = true
                            }, {}, {})
                        }
                    }

                })

        val constraintLayout = helper.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout_num)
        val imageView = helper.getView<ImageView>(R.id.item_img)
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
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.h))
            } else {
                GlideApp.with(imageView.context).load(loadurl)
                        .transition(withCrossFade()).placeholder(R.color.grayx)
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

            GlideApp.with(imageView.context).load(loadurl).transition(withCrossFade()).placeholder(R.color.grayx).error(ContextCompat.getDrawable(imageView.context, R.drawable.ai)).skipMemoryCache(true).into(object : ImageViewTarget<Drawable>(imageView) {
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



