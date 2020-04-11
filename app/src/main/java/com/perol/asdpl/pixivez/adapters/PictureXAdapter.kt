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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.util.Linkify
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.activity.SearchResultActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.activity.ZoomActivity
import com.perol.asdpl.pixivez.databinding.ViewPicturexDetailBinding
import com.perol.asdpl.pixivez.fragments.PictureXFragment
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.objects.TToast
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.responses.Tag
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import com.perol.asdpl.pixivez.viewmodel.PictureXViewModel
import com.perol.asdpl.pixivez.viewmodel.ProgressInfo
import com.shehuan.niv.NiceImageView
import com.waynejo.androidndkgif.GifEncoder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class PictureXAdapter(
    private val pictureXViewModel: PictureXViewModel,
    private val data: Illust,
    private val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val imageUrls = ArrayList<String>()
    lateinit var mListen: () -> Unit
    lateinit var mViewCommentListen: () -> Unit
    lateinit var mUserPicLongClick: () -> Unit
    fun setUserPicLongClick(listener: () -> Unit) {
        this.mUserPicLongClick = listener

    }

    fun setListener(listener: () -> Unit) {
        this.mListen = listener
    }

    fun setViewCommentListen(listener: () -> Unit) {
        this.mViewCommentListen = listener
    }

    init {
        when (PreferenceManager.getDefaultSharedPreferences(mContext).getString(
            "quality",
            "0"
        )?.toInt()
            ?: 0) {
            0 -> {
                if (data.meta_pages.isEmpty()) {
                    imageUrls.add(data.image_urls.medium)
                } else {
                    data.meta_pages.map {
                        imageUrls.add(it.image_urls.medium)
                    }

                }
            }
            else -> {
                if (data.meta_pages.isEmpty()) {
                    imageUrls.add(data.image_urls.large)
                } else {
                    data.meta_pages.map {
                        imageUrls.add(it.image_urls.large)
                    }

                }
            }
        }
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class FisrtDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class DetailViewHolder(
        var binding: ViewPicturexDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val tagFlowLayout = itemView.findViewById<TagFlowLayout>(R.id.tagflowlayout)
        private val captionTextView = itemView.findViewById<TextView>(R.id.textview_caption)
        private val btnTranslate = itemView.findViewById<TextView>(R.id.btn_translate)
        private val viewCommentTextView = itemView.findViewById<TextView>(R.id.textview_viewcomment)
        private val imageView = itemView.findViewById<NiceImageView>(R.id.imageView5)
        private val imageButtonDownload =
            itemView.findViewById<ImageButton>(R.id.imagebutton_download)

        fun updateWithPage(
            mContext: Context,
            s: Illust,
            mViewCommentListen: () -> Unit,
            mUserPicLongClick: () -> Unit
        ) {
            binding.illust = s
            captionTextView.autoLinkMask = Linkify.WEB_URLS
            val typedValue = TypedValue();
            mContext.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            val colorPrimary = typedValue.resourceId
            if (s.type == "ugoira")
                imageButtonDownload.visibility = View.GONE
            else
                imageButtonDownload.setColorFilter(ContextCompat.getColor(mContext, colorPrimary))
            if (s.user.is_followed)
                imageView.setBorderColor(Color.YELLOW)
            else
                imageView.setBorderColor(ContextCompat.getColor(mContext, colorPrimary))
            imageView.setOnLongClickListener {
                mUserPicLongClick.invoke()
                true
            }
            imageView.setOnClickListener {
                val intent = Intent(mContext, UserMActivity::class.java)
                intent.putExtra("data", s.user.id)
                mContext.startActivity(intent)
            }
            if (s.caption.isNotBlank())
                binding.html = Html.fromHtml(s.caption)
            else
                binding.html = Html.fromHtml("~")
            viewCommentTextView.setOnClickListener {
                mViewCommentListen.invoke()
            }

            //google translate app btn click listener
            val intent = Intent()
                .setType("text/plain")
            var componentPackageName = ""
            var componentName = ""
            var isGoogleTranslateEnabled = false
            for (resolveInfo: ResolveInfo in mContext.packageManager.queryIntentActivities(
                intent,
                0
            )) {
                try {
                    //emui null point exception
                    if (resolveInfo.activityInfo.packageName.contains("com.google.android.apps.translate")) {
                        isGoogleTranslateEnabled = true
                        componentPackageName = resolveInfo.activityInfo.packageName
                        componentName = resolveInfo.activityInfo.name
                    }
                } catch (e: Exception) {

                }

            }
            if (!isGoogleTranslateEnabled) btnTranslate.visibility = View.GONE
            else {
                btnTranslate.setOnClickListener {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intent.action = Intent.ACTION_PROCESS_TEXT
                        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, captionTextView.text.toString())
                    } else {
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, captionTextView.text.toString())
                    }
                    intent.component = ComponentName(
                        componentPackageName,
                        componentName
                    )
                    mContext.startActivity(intent)

                }
            }

            tagFlowLayout.apply {

                adapter = object : TagAdapter<Tag>(s.tags) {
                    override fun getView(parent: FlowLayout, position: Int, t: Tag): View {
                        val tv = LayoutInflater.from(context)
                            .inflate(R.layout.picture_tag, parent, false)
                        val name = tv.findViewById<TextView>(R.id.name)
                        val translateName = tv.findViewById<TextView>(R.id.translated_name)
                        name.text = "#${t.name} "
                        if (!t.translated_name.isNullOrBlank()) {
                            translateName.visibility = View.VISIBLE
                            translateName.text = t.translated_name
                        }
                        if (t.name == "R-18" || t.name == "R-18G") {
                            name.setTextColor(Color.RED)
                        }
                        translateName.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("searchword", s.tags[position].name)
                            val intent = Intent(context, SearchResultActivity::class.java)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        }
                        name.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("searchword", s.tags[position].name)
                            val intent = Intent(context, SearchResultActivity::class.java)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        }
                        translateName.setOnLongClickListener {
                            MaterialDialog(mContext).show {
                                title(R.string.add_to_block_tag_list)
                                negativeButton(android.R.string.cancel)
                                positiveButton(android.R.string.ok) {
                                    runBlocking {
                                        withContext(Dispatchers.IO) {
                                            AppDatabase.getInstance(PxEZApp.instance).blockTagDao()
                                                .insert(
                                                    BlockTagEntity(
                                                        name = t.name,
                                                        translateName = "${t.translated_name}"
                                                    )
                                                )
                                        }
                                        EventBus.getDefault().post(AdapterRefreshEvent())
                                    }
                                }
                                lifecycleOwner(binding.lifecycleOwner)
                            }
                            true
                        }
                        name.setOnLongClickListener {
                            MaterialDialog(mContext).show {
                                title(R.string.add_to_block_tag_list)
                                negativeButton(android.R.string.cancel)
                                positiveButton(android.R.string.ok) {
                                    runBlocking {
                                        withContext(Dispatchers.IO) {
                                            AppDatabase.getInstance(PxEZApp.instance).blockTagDao()
                                                .insert(
                                                    BlockTagEntity(
                                                        name = t.name,
                                                        translateName = "${t.translated_name}"
                                                    )
                                                )
                                        }
                                        EventBus.getDefault().post(AdapterRefreshEvent())
                                    }
                                }
                                lifecycleOwner(binding.lifecycleOwner)
                            }
                            true
                        }
                        return tv
                    }
                }


            }
            imageButtonDownload.setOnClickListener {
                Works.imageDownloadAll(s)
            }
        }
    }

    class RelativeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun updateWithPage(s: AboutPictureAdapter, mContext: Context) {
            recyclerView.layoutManager = GridLayoutManager(mContext, 3)
            recyclerView.adapter = s

        }

        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerview_relative)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_TYPE.ITEM_TYPE_PICTURE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_picturex_item, parent, false)
                return PictureViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_GIF.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_picturex_gif, parent, false)
                return GifViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_BLANK.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_picturex_firstdetail, parent, false)
                return BlankViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_RELATIVE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_picturex_relative, parent, false)
                return RelativeHolder(view)
            }
            else -> {
                val binding = ViewPicturexDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DetailViewHolder(binding)
            }

        }

    }

    class BlankViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            imageUrls.size -> ITEM_TYPE.ITEM_TYPE_DETAIL.ordinal
            imageUrls.size + 1 -> ITEM_TYPE.ITEM_TYPE_RELATIVE.ordinal
            imageUrls.size + 2 -> ITEM_TYPE.ITEM_TYPE_BLANK.ordinal

            else -> {
                if (data.type != "ugoira")
                    ITEM_TYPE.ITEM_TYPE_PICTURE.ordinal
                else
                    ITEM_TYPE.ITEM_TYPE_GIF.ordinal
            }
        }
    }


    enum class ITEM_TYPE {
        ITEM_TYPE_PICTURE,
        ITEM_TYPE_BLANK,
        ITEM_TYPE_DETAIL,
        ITEM_TYPE_RELATIVE,
        ITEM_TYPE_GIF,
    }

    private val path2: String = PxEZApp.storepath + "/" + data.id + ".gif"
    override fun getItemCount() = imageUrls.size + 3


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PictureViewHolder) {
            val imageView = holder.itemView.findViewById<ImageView>(R.id.imageview_pic)
            GlideApp.with(imageView).load(imageUrls[position]).placeholder(R.color.white)
                .transition(withCrossFade()).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        mListen.invoke()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (position == 0) {
                            mListen.invoke()
                        }

                        return false
                    }
                }).into(imageView)
            imageView.apply {
                setOnLongClickListener { it ->
                    val builder = MaterialAlertDialogBuilder(mContext as Activity)
                    builder.setTitle(mContext.resources.getString(R.string.saveselectpic1))
                    builder.setMessage("描述: " + Html.fromHtml(data.caption))
                    builder.setPositiveButton(mContext.resources.getString(R.string.confirm)) { dialog, which ->
                        TToast.startDownload(PxEZApp.instance)
                        Works.imgD(data, position)
                    }
                    builder.setNegativeButton(mContext.resources.getString(android.R.string.cancel)) { dialog, which ->

                    }
                    if (data.meta_pages.isNotEmpty()) {
                        builder.setNeutralButton(R.string.mutichoicesave) { dialog, which ->

                            val list = ArrayList<String>()
                            data.meta_pages.map { ot ->
                                list.add(ot.image_urls.original)
                            }
                            val mSelectedItems =
                                ArrayList<Int>()  // Where we track the selected items
                            val builder = MaterialAlertDialogBuilder(mContext as Activity)
                            val showlist = ArrayList<String>()
                            for (i in list.indices) {
                                showlist.add(i.toString())
                            }
                            val boolean = BooleanArray(showlist.size)
                            for (i in boolean.indices) {
                                boolean[i] = false
                            }
                            builder.setTitle(R.string.choice)
                                .setMultiChoiceItems(
                                    showlist.toTypedArray(), boolean
                                ) { _, which, isChecked ->
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items

                                        mSelectedItems.add(which)
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which))
                                    }
                                }
                                // Set the action buttons
                                .setPositiveButton(android.R.string.ok) { dialog, id ->
                                    TToast.startDownload(PxEZApp.instance)
                                    mSelectedItems.map {
                                        Works.imgD(data, it)
                                    }
                                }
                                .setNegativeButton(android.R.string.cancel) { dialog, id -> }
                                .setNeutralButton("全选") { _, id ->
                                    for (i in boolean.indices) {
                                        boolean[i] = true
                                    }

                                    mSelectedItems.clear()
                                    for (i in showlist.indices) {
                                        mSelectedItems.add(i)
                                    }
                                    builder.setMultiChoiceItems(
                                        showlist.toTypedArray(), boolean
                                    ) { _, which, isChecked ->
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
                setOnClickListener {

                    val intent = Intent(mContext, ZoomActivity::class.java)
                    val bundle = Bundle()
                    bundle.putInt("num", position)
                    val arrayList = ArrayList<String>()
                    if (data.meta_pages.isEmpty()) {
                        arrayList.add(data.meta_single_page.original_image_url!!)
                    } else {
                        data.meta_pages.map {
                            arrayList.add(it.image_urls.original)
                        }
                    }
                    bundle.putStringArrayList("url", arrayList)
                    bundle.putParcelable(
                        "illust",
                        pictureXViewModel.illustDetailResponse.value?.illust
                    )
                    intent.putExtras(bundle)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext.startActivity(intent)
                }
                if (position == 0 && PxEZApp.animationEnable) {
                    transitionName = "mainimage"
                }
            }
//            (mContext as FragmentActivity).supportStartPostponedEnterTransition()
        } else if (holder is GifViewHolder) {
            progressBar = holder.itemView.findViewById<CircleProgressBar>(R.id.progressbar_gif)
            val play = holder.itemView.findViewById<ImageView>(R.id.imageview_play)
            imageViewGif = holder.itemView.findViewById(R.id.imageview_gif)
            imageViewGif!!.setOnLongClickListener {
                if (progressBar?.visibility != View.VISIBLE) {
                    Snackbar.make(
                        imageViewGif!!,
                        mContext.getString(R.string.encodegif),
                        Snackbar.LENGTH_LONG
                    ).setAction(android.R.string.ok) { view ->
                        if (!isEncoding) {
                            isEncoding = true
                            val file1 = File(path2)
                            if (!file1.parentFile.exists()) {
                                file1.parentFile.mkdirs()
                            }
                            val ob = encodingGif()
                            if (ob != null) {
                                ob.subscribe({
                                    runBlocking {
                                        withContext(Dispatchers.IO) {
                                            File(path).copyTo(file1, true)
                                        }
                                        MediaScannerConnection.scanFile(
                                            PxEZApp.instance, arrayOf(path2), arrayOf(
                                                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                                    file1.extension
                                                )
                                            )
                                        ) { _, _ ->

                                        }
                                        isEncoding = false
                                        Toast.makeText(
                                            PxEZApp.instance,
                                            R.string.savegifsuccess,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }, {
                                    isEncoding = false
                                    Toast.makeText(
                                        PxEZApp.instance,
                                        R.string.savegifsuccesserr,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }, {

                                })
                            } else {
                                runBlocking {
                                    withContext(Dispatchers.IO) {
                                        File(path).copyTo(file1, true)
                                    }
                                    MediaScannerConnection.scanFile(
                                        PxEZApp.instance, arrayOf(path2), arrayOf(
                                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                                file1.extension
                                            )
                                        )
                                    ) { _, _ ->
                                    }
                                    isEncoding = false
                                    Toast.makeText(
                                        PxEZApp.instance,
                                        R.string.savegifsuccess,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        } else {
                            Toasty.info(
                                PxEZApp.instance,
                                "It's already going on",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.show()
                }
                true
            }
            play!!.setOnClickListener {
                play.visibility = View.GONE
                Toasty.info(PxEZApp.instance, "Downloading...", Toast.LENGTH_SHORT).show()
                pictureXViewModel.loadGif(data.id).flatMap {
                    duration = it.ugoira_metadata.frames[0].delay
                    pictureXViewModel.downloadZip(
                        it.ugoira_metadata.zip_urls.medium
                    )
                    return@flatMap Observable.just(it)
                }.subscribe({
                }, {
                    Log.d("throw", "throw it")
                    play.visibility = View.VISIBLE
                }, {})
            }
            GlideApp.with(imageViewGif!!).load(data.image_urls.medium).placeholder(R.color.white)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : DrawableImageViewTarget(imageViewGif) {

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        mListen.invoke()
                    }
                })
        } else if (holder is DetailViewHolder) {

            holder.updateWithPage(mContext, data, mViewCommentListen, mUserPicLongClick)
        } else if (holder is RelativeHolder) {
            aboutPictureAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
            holder.updateWithPage(aboutPictureAdapter, mContext)
        }
    }

    var isEncoding = false

    private val path: String = PxEZApp.instance.cacheDir.toString() + "/" + data.id + ".gif"
    private val path3: String = PxEZApp.instance.cacheDir.toString() + "/" + data.id + ".ojbk"
    private fun encodingGif(): Observable<Int>? {

        val file1 = File(path3)
        if (file1.exists()) {
            return null
        }
        val parentPath = PxEZApp.instance.cacheDir.path + "/" + data.id
        val parentFile = File(parentPath)
        val listFiles = parentFile.listFiles()
        if (listFiles == null || listFiles.isEmpty()) {
            throw RuntimeException("unzip files not found")
        }
        if (listFiles.size < size) {
            throw RuntimeException("something wrong in ugoira files")
        }
        Toasty.info(PxEZApp.instance, "约有${listFiles.size}张图片正在合成", Toast.LENGTH_SHORT).show()
        return Observable.create<Int> {
            listFiles.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
            val gifEncoder = GifEncoder()
            for (i in listFiles.indices) {
                if (listFiles[i].isFile) {
                    val bitmap = BitmapFactory.decodeFile(listFiles[i].absolutePath)

                    if (i == 0) {
                        gifEncoder.init(
                            bitmap.width,
                            bitmap.height,
                            path,
                            GifEncoder.EncodingType.ENCODING_TYPE_STABLE_HIGH_MEMORY
                        )
                    }
                    gifEncoder.encodeFrame(bitmap, duration)
                    Log.d("progressset", i.toString())
                }

            }
            gifEncoder.close()
            file1.mkdirs()
            it.onNext(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    }


    var size = 1
    var duration: Int = 50
    var progressBar: CircleProgressBar? = null
    var imageViewGif: ImageView? = null
    val aboutPictureAdapter = AboutPictureAdapter(R.layout.view_aboutpic_item)
    fun setRelativeNow(it: ArrayList<Illust>) {
        if (it.isEmpty()) {
            return
        }
        val list = ArrayList<String>()
        it.forEach {
            list.add(it.image_urls.square_medium)
        }

        aboutPictureAdapter.setNewData(list)
        aboutPictureAdapter.setOnItemClickListener { adapter, view, position ->
            val id = it[position].id
            val bundle = Bundle()
            val arrayList = ArrayList<Long>()
            it.forEach {
                arrayList.add(it.id)
            }
            bundle.putLongArray("illustlist", arrayList.toLongArray())
            bundle.putLong("illustid", id)
            val intent = Intent(mContext, PictureActivity::class.java)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
    }

    fun setProgress(it: ProgressInfo) {
        if (progressBar != null) {
            progressBar!!.max = it.all.toInt()
            progressBar!!.progress = it.now.toInt()
        }
    }

    fun setUserPicColor(it: Boolean) {
        data.user.is_followed = it
        notifyItemChanged(imageUrls.size)

    }

    var animationDrawable: AnimationDrawable? = null
    private fun createAnimationFrame(illustBean: Illust, imageView: ImageView, duration: Int) {
        if (animationDrawable == null)
            animationDrawable = AnimationDrawable()
        val parentPath = PxEZApp.instance.cacheDir.path + "/" + illustBean.id
        val parentFile = File(parentPath)
        val listFiles = parentFile.listFiles()
        listFiles.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })

        runBlocking {
            withContext(Dispatchers.IO) {
                for (i in listFiles) {
                    val bitmap = GlideApp.with(imageView).asBitmap().load(i).skipMemoryCache(true)
                        .submit(illustBean.width, illustBean.height).get()
                    val drawable = BitmapDrawable(imageView.context.resources, bitmap)
                    animationDrawable!!.addFrame(drawable, duration)
                }
                animationDrawable!!.isOneShot = false
            }
            imageView.setImageDrawable(animationDrawable)
            animationDrawable!!.start()
        }
    }

    fun setProgressComplete(it: Boolean) {
        progressBar?.visibility = View.GONE
        createAnimationFrame(data, imageViewGif!!, duration)
    }
}