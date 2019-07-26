package com.perol.asdpl.pixivez.adapters

import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.ybq.android.spinkit.SpinKitView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.ZoomActivity
import com.perol.asdpl.pixivez.responses.IllustBean
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.Works
import android.Manifest.permission.SET_WALLPAPER
import androidx.core.app.ActivityCompat.startActivityForResult
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import com.perol.asdpl.pixivez.services.PxEZApp
import java.io.File


/**
 * Created by Notsfsssf on 2018/3/12.
 */

class PictureAdapter(layoutResId: Int, data: List<String>?, private val data1: IllustBean?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    var callback: Callback? = null

    interface Callback {
        fun onClick()
        fun onFirst()
    }

//    override fun onViewRecycled(holder: BaseViewHolder) {
//        super.onViewRecycled(holder)
//        val imageView = holder.getView<ImageView>(R.id.imagelarge1)
//
//        if (imageView == null) {
//
//        } else {
//            GlideApp.with(mContext).clear(imageView)
//
//        }
//    }


    override fun convert(helper: BaseViewHolder, item: String) {
//        if (callback != null && helper.adapterPosition == (0)) {
//            callback!!.onFirst()
//        }
        if (data1 == null) {
            return
        }
        Log.d("put", "1");
        val imageView = helper.itemView.findViewById<ImageView>(R.id.imagelarge1)
        val spinKitView = helper.itemView.findViewById<SpinKitView>(R.id.progressbar_loading)
        spinKitView.visibility = View.VISIBLE
        helper.addOnClickListener(R.id.imagelarge1)
        imageView.setOnClickListener {
            val intent = Intent(mContext, ZoomActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("num", helper.adapterPosition)
            val arrayList = ArrayList<String>()
            if (data1.meta_pages.isEmpty()) {
                arrayList.add(data1.meta_single_page.original_image_url)
            } else {
                data1.meta_pages.map {
                    arrayList.add(it.image_urlsX.original)
                }
            }
            bundle.putStringArrayList("url", arrayList)
            intent.putExtras(bundle)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)
        }
        GlideApp.with(imageView.context).load(item).placeholder(R.color.white).transition(withCrossFade()).error(R.drawable.buzhisuocuo).listener(object : RequestListener<Drawable> {

            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                spinKitView.visibility = View.GONE

                if (callback != null && helper.adapterPosition == (this@PictureAdapter.data.size - 1)) {
                    callback!!.onClick()
                }
                return false
            }
        }).into(imageView)
        if (data1.type != "ugoira")
            helper.setOnLongClickListener(R.id.imagelarge1) { it ->
                val builder = AlertDialog.Builder(mContext as Activity, R.style.AlertDialogCustom)

                builder.setTitle(mContext.resources.getString(R.string.saveselectpic1))
                var imagesrcurl: String? = null
                if (data1.meta_pages.size != 0) {
                    imagesrcurl = data1.meta_pages[helper.adapterPosition].image_urlsX.original
                } else {
                    imagesrcurl = data1.meta_single_page.original_image_url
                }
                builder.setMessage("描述: " + Html.fromHtml(data1.caption))


                val finalImagesrcurl = imagesrcurl
                builder.setPositiveButton(mContext.resources.getString(R.string.confirm)) { dialog, which ->
                    val position: String? =
                            if (data1.meta_pages.isEmpty()) {
                                null
                            } else
                                helper.adapterPosition.toString()
                    var type = ".jpg"
                    if (finalImagesrcurl.contains("png")) {
                        type = ".png"
                    }
                    Works.ImageDownload(finalImagesrcurl, data1.user.id.toString(), data1.id.toString(), position, type)
                }
                builder.setNegativeButton(mContext.resources.getString(R.string.cancel)) { dialog, which ->

                }
//                builder.setNeutralButton("设置为壁纸"){a,b->
//
//                    val position: String? =
//                            if (data1.meta_pages.isEmpty()) {
//                                null
//                            } else
//                                helper.adapterPosition.toString()
//                    var type = ".jpg"
//                    if (finalImagesrcurl.contains("png")) {
//                        type = ".png"
//                    }
//                    Works.ImageDownload(finalImagesrcurl, data1.user.id.toString(), data1.id.toString(), position, type)
//                    val user=data1.user.id.toString()
//                    val name=data1.id.toString()
//                    val part:String?=null
//                    var filename = "${name}_p$part$type"
//                    if (part != null) {
//                        when (PxEZApp.saveformat) {
//                            0 -> {
//                                filename = "${name}_$part$type"
//                            }
//                            1 -> {
//                                filename = "${name}_p$part$type"
//                            }
//                            2 -> {
//                                filename = "${user}_${name}_$part$type"
//                            }
//                        }
//                    } else {
//                        when (PxEZApp.saveformat) {
//                            0 -> {
//                                filename = "$name$type"
//                            }
//                            1 -> {
//                                filename = "$name$type"
//                            }
//                            2 -> {
//                                filename = "${user}_$name$type"
//                            }
//                        }
//                    }
//                    val tempImgSavePath = filename;
//
//                    val file =  File(tempImgSavePath);
//
//                    val intent = Intent(Intent.ACTION_ATTACH_DATA);
//
//                    intent.setDataAndType(Uri.fromFile(file), "image/*");
//
//                    mContext.startActivity(intent);
//
//
//                }
                if (data1.meta_pages.isNotEmpty()) {
                    builder.setNeutralButton(R.string.mutichoicesave) { dialog, which ->

                        val list = ArrayList<String>()
                        data1.meta_pages.map { ot ->
                            list.add(ot.image_urlsX.original)
                        }
                        val mSelectedItems = ArrayList<Int>()  // Where we track the selected items
                        val builder = AlertDialog.Builder(mContext as Activity, R.style.ThemeOverlay_AppCompat_Dialog)
                        val showlist = ArrayList<String>()
                        for (i in list.indices) {
                            showlist.add(i.toString())
                        }
                        val boolean = BooleanArray(showlist.size)
                        for (i in 0..boolean.size - 1) {
                            boolean[i] = false
                        }
                        builder.setTitle(R.string.choice)
                                .setMultiChoiceItems(showlist.toTypedArray(), boolean
                                ) { dialog, which, isChecked ->
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items

                                        mSelectedItems.add(which)
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which))
                                    }
                                }
                                // Set the action buttons
                                .setPositiveButton(R.string.confirm) { dialog, id ->

                                    mSelectedItems.map {
                                        var type = ".jpg"
                                        if (list[it].contains("png")) {
                                            type = ".png"
                                        }
                                        Works.ImageDownload(list[it], data1.user.id.toString(), data1.id.toString(), it.toString(), type)

                                    }
                                }
                                .setNegativeButton(R.string.cancel) { dialog, id -> }
                                .setNeutralButton("全选") { dialog, id ->
                                    for (i in 0..boolean.size - 1) {
                                        boolean[i] = true
                                    }

                                    mSelectedItems.clear()
                                    for (i in showlist.indices) {
                                        mSelectedItems.add(i)
                                    }
                                    builder.setMultiChoiceItems(showlist.toTypedArray(), boolean
                                    ) { dialog, which, isChecked ->
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items

                                            mSelectedItems.add(which)
                                        } else if (mSelectedItems.contains(which)) {
                                            // Else, if the item is already in the array, remove it
                                            mSelectedItems.remove(Integer.valueOf(which))
                                        }
                                    }
                                    builder.create().show()
                                }

                        val dialog = builder.create()

                        dialog.show()
                    }
                }

                val dialog = builder.create()
                dialog.show()
                true
            }

    }


}
