package com.perol.asdpl.pixivez.adapters

import android.content.Context
import android.net.Uri
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.GlideApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import java.io.File
import java.util.ArrayList



/**
 * Created by Notsfsssf on 2018/3/15.
 */

class ZoomPagerAdapter(private val context: Context, private val arrayList: ArrayList<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_pager_zoom, container, false)
        val photoView = view.findViewById<SubsamplingScaleImageView>(R.id.photoview_zoom)
        photoView.isEnabled = true
        GlideApp.with(context).asFile().load(arrayList[position]).skipMemoryCache(true).into(object :SimpleTarget<File>(){
            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                photoView.setImage(ImageSource.uri(Uri.fromFile(resource)))
            }
        })
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
