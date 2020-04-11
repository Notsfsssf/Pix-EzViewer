package com.perol.asdpl.pixivez.lighting

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.method.TextKeyListener.clear
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentLayout
import com.facebook.litho.Size
import com.facebook.litho.annotations.*
import com.facebook.litho.utils.MeasureUtils
import java.io.File

@MountSpec
object GlideImageSpec {
    private const val DEFAULT_INT_VALUE = -1

    @OnMeasure
    fun onMeasureLayout(
        c: ComponentContext?, layout: ComponentLayout?, widthSpec: Int,
        heightSpec: Int, size: Size?,
        @Prop(optional = true, resType = ResType.FLOAT) imageAspectRatio: Float
    ) {
        MeasureUtils.measureWithAspectRatio(widthSpec, heightSpec, imageAspectRatio, size)
    }

    @OnCreateMountContent
    fun onCreateMountContent(c: Context?): ImageView {
        return ImageView(c)
    }

    @OnMount
    fun onMount(
        c: ComponentContext,
        imageView: ImageView?,
        @Prop(optional = true) imageUrl: String?,
        @Prop(optional = true) file: File?,
        @Prop(optional = true) uri: Uri?,
        @Prop(optional = true) resourceId: Int?,
        @Prop(optional = true) glideRequestManager: RequestManager?,
        @Prop(optional = true, resType = ResType.DRAWABLE) failureImage: Drawable?,
        @Prop(optional = true, resType = ResType.DRAWABLE) fallbackImage: Drawable?,
        @Prop(optional = true, resType = ResType.DRAWABLE) placeholderImage: Drawable?,
        @Prop(optional = true) diskCacheStrategy: DiskCacheStrategy?,
//        @Prop(optional = true) requestListener: RequestListener<*>?,
        @Prop(optional = true) asBitmap: Boolean,
        @Prop(optional = true) asGif: Boolean,
        @Prop(optional = true) crossFade: Boolean,
        @Prop(optional = true) crossFadeDuration: Int,
        @Prop(optional = true) centerCrop: Boolean,
        @Prop(optional = true) fitCenter: Boolean,
        @Prop(optional = true) skipMemoryCache: Boolean,
        @Prop(optional = true) target: Target?
    ) {
        var glideRequestManager: RequestManager?
        require(!(imageUrl == null && file == null && uri == null && resourceId == null)) { "You must provide at least one of String, File, Uri or ResourceId" }
        glideRequestManager = Glide.with(c.androidContext)
        if (asBitmap) {
            glideRequestManager.asBitmap()
        }
        if (asGif) {
            glideRequestManager.asGif()
        }
        val request: RequestBuilder<*>?
        if (imageUrl != null) {
            request = glideRequestManager!!.load(imageUrl)
        } else if (file != null) {
            request = glideRequestManager.load(file)
        } else if (uri != null) {
            request = glideRequestManager.load(uri)
        } else {
            request = glideRequestManager!!.load(resourceId)
        }
        checkNotNull(request) { "Could not instantiate DrawableTypeRequest" }
        if (diskCacheStrategy != null) {
            request.diskCacheStrategy(diskCacheStrategy)
        }

        if (crossFade) {
            request.transition(withCrossFade())
        }
        if (crossFadeDuration != DEFAULT_INT_VALUE) {
            request.transition(withCrossFade().crossFade(crossFadeDuration))
        }
        if (centerCrop) {
            request.centerCrop()
        }
        if (failureImage != null) {
            request.error(failureImage)
        }
        if (fallbackImage != null) {
            request.fallback(fallbackImage)
        }
        if (fitCenter) {
            request.fitCenter()
        }

        if (placeholderImage != null) {
            request.placeholder(placeholderImage)
        }
        request.skipMemoryCache(skipMemoryCache)
        if (target != null) {
        } else {
            request.into(imageView!!)
        }
    }

    @OnUnmount
    fun onUnmount(c: ComponentContext?, imageView: ImageView?) {
        Glide.with(imageView!!).clear(imageView)
    }
}