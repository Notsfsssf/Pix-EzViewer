package com.perol.asdpl.pixivez.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.util.Linkify
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.activity.SearchResultActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.*
import com.perol.asdpl.pixivez.databinding.FragmentPictureMBinding
import com.perol.asdpl.pixivez.dialog.CommentDialog
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse
import com.perol.asdpl.pixivez.responses.IllustDetailResponse
import com.perol.asdpl.pixivez.responses.IllustsBean
import com.perol.asdpl.pixivez.responses.UgoiraMetadataResponse
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.viewmodel.PictureMViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.PictureFactory
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_picture_m.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import java.io.File

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [PictureMFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PictureMFragment : Fragment() {


    var imagebutton_gif: ImageView? = null


    lateinit var pictureMViewModel: PictureMViewModel
    var pictureAdapter: PictureAdapter? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lazyLoad()
    }

    fun lazyLoad() {
        val pictureAdapter1 = PictureAdapter(R.layout.view_picture_item1, null, null)
        pictureAdapter1!!.emptyView = layoutInflater.inflate(R.layout.empty_picture, null)
        recyclerview_srcpicture.adapter = pictureAdapter1
        val typedValue = TypedValue();
        activity!!.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.resourceId
        initData()
        initView()
    }


    lateinit var aboutPictureAdapter: AboutPictureAdapter


    private fun initView() {
        recyclerview_srcpicture.layoutManager = LinearLayoutManager(activity)
        recyclerview_srcpicture.transitionName = "mainimage"
        fab.setOnClickListener {
            pictureMViewModel.fabclick(pictureMViewModel.illustDetailResponse.value!!.illust.id)

        }
        fab.setOnLongClickListener {
            toast(resources.getString(R.string.fetchtags))
            pictureMViewModel.fabsetOnLongClick()
            true
        }
    }

    private fun initData() {
        pictureMViewModel = ViewModelProviders.of(this, PictureFactory()).get(PictureMViewModel::class.java)

        pictureMViewModel.illustDetailResponse.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.illust.type.contains("ugoira")) {
                    loadimages(it)
                    loadgif(it)
                } else {
                    loadimages(it)
                }
            }
        })
        pictureMViewModel.tags.observe(this, Observer {
            showTagsDialog(it)
        })
        pictureMViewModel.ugoiraMetadataResponse.observe(this, Observer {
            loadinggif(it)
        })
        pictureMViewModel.followuser.observe(this, Observer {
            followUser(it)

        })
        pictureMViewModel.likeillust.observe(this, Observer {
            bookmark(it)
        })
        pictureMViewModel.downloadgifsucc.observe(this, Observer {
            playgif(it)
        })
        pictureMViewModel.aboutpics.observe(this, Observer {
            aboutpics(it)
        })
        pictureMViewModel.firstget(param1!!.toLong())

    }

    private fun aboutpics(it: ArrayList<IllustsBean>?) {
        if (it != null) {
            val a = ArrayList<String>()
            it.map { a.add(it.image_urls.square_medium) }
            aboutPictureAdapter.setNewData(a)
        }
    }

    private fun showTagsDialog(it: List<BookMarkDetailResponse.BookmarkDetailBean.TagsBean>?) {
        val arrayList = ArrayList<String>()

        if (it != null && !pictureMViewModel.likeillust.value!!) {
            it.map {
                arrayList.add(it.name)
            }
            val checkStatus = HashMap<Int, Boolean>()
            for (i in it.indices) {
                checkStatus[i] = false
            }
            val tagsAdapter = TagsAdapter(R.layout.view_tags_item, arrayList, checkStatus)
            var switch: Switch? = null
            alert {
                customView {
                    verticalLayout {
                        linearLayout {
                            val tagText = editText { }.lparams {
                                weight = 3f
                                width = dip(0)
                                height = wrapContent
                            }
                            button("Add") {
                                setOnClickListener {
                                    if (tagText.text.isNotBlank())
                                        tagsAdapter.addData(0, tagText.text.toString())
                                }
                            }.lparams {
                                weight = 1f
                                width = dip(0)
                                height = wrapContent
                            }

                        }.lparams(width = matchParent, height = dip(0)) {
                            margin = dip(8)
                            weight = 1f
                        }
                        recyclerView {
                            layoutManager = LinearLayoutManager(activity!!.applicationContext)
                            adapter = tagsAdapter
                        }.lparams(width = matchParent, height = dip(0)) {
                            weight = 5f
                        }
                        switch = switch {
                            hint = resources.getString(R.string.privatep)
                            isChecked = pictureMViewModel.illustDetailResponse.value!!.illust.isIs_bookmarked
                        }.lparams(width = matchParent, height = wrapContent) {
                            marginStart = dip(8)
                            marginEnd = dip(8)
                            weight = 1f
                        }

                    }
                }
                positiveButton("LIKE!") { dialog ->
                    val taglist = ArrayList<String>()
                    if (tagsAdapter.checkStatus.isNotEmpty())
                        for (i in 0 until tagsAdapter.checkStatus.size) {
                            if (tagsAdapter.checkStatus[i]!!) {
                                taglist.add(arrayList[i])
                            }
                        }
                    println(taglist)
                    pictureMViewModel.onDialogclick(taglist, switch!!.isChecked, pictureMViewModel.illustDetailResponse.value!!.illust.id);
                    dialog.dismiss()
                }
            }.show()


        }
    }

    private fun playgif(it: Boolean?) {
        if (it != null) {
            toast(resources.getString(R.string.encodinggif))
            recyclerview_srcpicture.adapter = gifAdapter
        }
    }

    var pic_image_back: ImageView? = null
    var colorPrimary: Int = 0
    private fun followUser(it: Boolean?) {
        if (it != null) {
            if (it) {
                GlideApp.with(this).load(ResourcesCompat.getDrawable(resources, R.color.yellow, null)).circleCrop().into(pic_image_back!!)
            } else {
                GlideApp.with(this).load(ResourcesCompat.getDrawable(resources, colorPrimary, null)).circleCrop().into(pic_image_back!!)
            }
        }
    }

    private var gifAdapter: GifAdapter? = null
    private fun loadinggif(it: UgoiraMetadataResponse?) {
        val path = activity!!.applicationContext!!.cacheDir.path + "/" + pictureMViewModel.illustDetailResponse.value!!.illust.id.toString()
        val files = ArrayList<File>()
        files.add(File(path))
        gifAdapter = GifAdapter(R.layout.view_picture_item, files, it, pictureMViewModel.illustDetailResponse.value!!.illust, activity!!.applicationContext)
        //   gifAdapter = BiliGifAdapter(R.layout.view_picture_item,files,  pictureMViewModel.illustDetailResponse.value!!.illust,it!!)
        imagebutton_gif!!.visibility = View.VISIBLE
        imagebutton_gif!!.setOnClickListener {
            imagebutton_gif!!.visibility = View.GONE
            downloadgif()
        }
    }

    private fun isgifdownloaded(): Boolean {
        val ugoria = pictureMViewModel.ugoiraMetadataResponse.value!!.ugoira_metadata.frames
        val finalfilename = ugoria.get(ugoria.size - 1).getFile();
        val firstfilename = ugoria.get(0).getFile();
        val file = File(activity!!.applicationContext.getCacheDir().getPath() + "/" + pictureMViewModel.illustDetailResponse.value!!.illust.id + "/" + finalfilename);
        val file1 = File(activity!!.applicationContext.getCacheDir().getPath() + "/" + pictureMViewModel.illustDetailResponse.value!!.illust.id + "/" + firstfilename);
        return !(!file.exists() || !file1.exists())

    }

    inline fun ViewManager.tagFlowLayout(init: TagFlowLayout.() -> Unit = {}): TagFlowLayout {
        return ankoView({ TagFlowLayout(it) }, theme = 0, init = init)
    }

    private fun downloadgif() {
        Toasty.info(activity!!.applicationContext, "等待加载zip", Toast.LENGTH_LONG).show()
        if (!isgifdownloaded()) {
            pictureMViewModel.downloadzip(pictureMViewModel.ugoiraMetadataResponse.value!!.ugoira_metadata.zip_urls.medium)

        } else pictureMViewModel.downloadgifsucc.value = true
    }

    private fun bookmark(it: Boolean?) {
        if (it != null) {
            if (it) {
                GlideApp.with(this).load(R.drawable.heart_red).into(fab)
            } else {
                GlideApp.with(this).load(R.drawable.ic_action_heart).into(fab)
            }

        }
    }

    private fun loadgif(it: IllustDetailResponse) {
        pictureMViewModel.loadgif(it.illust.id)
    }

    private object Ids {
        val userpic = 1
        val username = 2
        val title = 3
        val date = 4
        val chahuaid = 5
        val illustid = 6
        val chakan = 7
        val totalview = 8
        val shoucang = 9
        val totalbookmark = 10
        val fenbianlv = 11
        val pix = 12
        val pic_user = 13
        val recyclerview_aboutpic = 14
        val search_page_flowlayout = 15


    }

    var viewinfoot: View? = null
    private fun loadimages(it: IllustDetailResponse) {

        val imageurls: ArrayList<String> = ArrayList()
        val mVals = ArrayList<String>()
        it.illust.tags.map {
            mVals.add(it.name)
        }
        when (SharedPreferencesServices.getInstance()!!.getInt("quality")) {
            0 -> {
                if (it.illust.meta_pages.isEmpty()) {
                    imageurls.add(it.illust.image_urls.medium)
                } else {
                    it.illust.meta_pages.map {
                        imageurls.add(it.image_urlsX.medium)
                    }

                }
            }
            else -> {
                if (it.illust.meta_pages.isEmpty()) {
                    imageurls.add(it.illust.image_urls.large)
                } else {
                    it.illust.meta_pages.map {
                        imageurls.add(it.image_urlsX.large)
                    }

                }
            }
        }
        pictureAdapter = PictureAdapter(R.layout.view_picture_item1, imageurls, it.illust)
        viewinfoot = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                constraintLayout {
                    val userpic = frameLayout {
                        id = Ids.userpic
                        pic_image_back = imageView {
                        }.lparams(width = dip(58), height = dip(58)) {
                            margin = dip(2)
                        }
                        imageView {
                            setOnLongClickListener {
                                pictureMViewModel.likeuser(pictureMViewModel.illustDetailResponse.value!!.illust.user.id)
                                true
                            }
                            setOnClickListener {
                                val intent = Intent(context, UserMActivity::class.java)
                                intent.putExtra("data", pictureMViewModel.illustDetailResponse.value!!.illust.user.id)
                                startActivity(intent)
                            }
                            GlideApp.with(this).load(it.illust.user.profile_image_urls.medium).circleCrop().into(this)

                        }.lparams(width = dip(50), height = dip(50)) {
                            margin = dip(6)
                        }
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToTop = PARENT_ID
                    }
                    val username = textView {
                        id = Ids.username
                        transitionName = "picuser"
                        setTextIsSelectable(true)
                        text = it.illust.user.name
                    }.lparams(width = dip(0), height = wrapContent) {
                        startToEnd = Ids.userpic
                        topToBottom = Ids.title
                        endToEnd = PARENT_ID
                        margin = dip(4)
                    }
                    val title = textView() {
                        id = Ids.title
                        text = it.illust.title
                        textColor = colorAttr(R.attr.colorPrimary)
                        setTextIsSelectable(true)
                    }.lparams(width = dip(0), height = wrapContent) {
                        startToEnd = Ids.userpic
                        endToEnd = PARENT_ID
                        topToTop = PARENT_ID
                        margin = dip(4)
                    }
                    textView {
                        id = Ids.date
                        text = it.illust.create_date
                    }.lparams(width = dip(0), height = wrapContent) {
                        topToBottom = Ids.username
                        startToEnd = Ids.userpic
                        bottomToBottom = PARENT_ID
                        margin = dip(4)
                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(8)
                }

                constraintLayout {
                    val chahuaid = textView(getString(R.string.illustid).toLowerCase()) {
                        id = Ids.chahuaid
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToTop = PARENT_ID
                        margin = dip(8)
                    }
                    val illustid = textView {
                        id = Ids.illustid
                        text = it.illust.id.toString()
                        textColor = colorAttr(R.attr.colorPrimary)
                        setTextIsSelectable(true)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.chahuaid
                        topToTop = PARENT_ID
                        margin = dip(8)
                    }
                    val fenbianlv = textView(getString(R.string.pixel)) {
                        id = Ids.fenbianlv
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.illustid
                        topToTop = PARENT_ID
                        margin = dip(8)
                    }
                    val pix = textView {
                        id = Ids.pix
                        text = "${it.illust.width}X${it.illust.height}"
                        textColor = colorAttr(R.attr.colorPrimary)
                        setTextIsSelectable(true)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.fenbianlv
                        topToTop = PARENT_ID
                        margin = dip(8)
                    }
                    val chakan = textView(getString(R.string.view)) {
                        id = Ids.chakan
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToBottom = Ids.chahuaid
                        margin = dip(8)
                    }
                    val totalview = textView {
                        id = Ids.totalview
                        text = it.illust.total_view.toString()
                        textColor = colorAttr(R.attr.colorPrimary)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.chakan
                        topToBottom = Ids.chahuaid
                        margin = dip(8)
                    }
                    val shoucang = textView(getString(R.string.bookmark)) {
                        id = Ids.shoucang
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.totalview
                        topToBottom = Ids.chahuaid
                        margin = dip(8)
                    }
                    val totalbookmark = textView {
                        id = Ids.totalbookmark
                        text = it.illust.total_bookmarks.toString()
                        textColor = colorAttr(R.attr.colorPrimary)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToEnd = Ids.shoucang
                        topToBottom = Ids.chahuaid
                        margin = dip(8)
                    }
                    imageView {
                        setImageResource(R.drawable.ic_action_download)
                        setOnClickListener { th ->
                            if (it.illust.meta_pages.isEmpty()) {
                                Works.ImageDownload(it.illust.meta_single_page.original_image_url, it.illust.user.id.toString(), it.illust.id.toString(), null, if (it.illust.meta_single_page.original_image_url.contains("png")) {
                                    ".png"
                                } else {
                                    ".jpg"
                                })
                            } else {
                                for (i in it.illust.meta_pages.indices) {
                                    val imageurl = it.illust.meta_pages[i].image_urlsX.original
                                    Works.ImageDownload(imageurl, it.illust.user.id.toString(), it.illust.id.toString(), i.toString(),
                                            if (imageurl.contains("png")) {
                                                ".png"
                                            } else {
                                                ".jpg"
                                            })
                                }
                            }
                        }
                        visibility = if (it.illust.type.equals("ugoira")) {
                            View.GONE
                        } else {
                            View.VISIBLE
                        }
                    }.lparams(width = dip(30), height = dip(30)) {
                        rightToRight = PARENT_ID
                        topToTop = PARENT_ID
                        margin = dip(8)
                        bottomToBottom = PARENT_ID
                    }
                    imagebutton_gif = imageView {
                        setImageResource(R.drawable.ic_action_play)
                        visibility = View.GONE
                    }.lparams(width = dip(30), height = dip(30)) {
                        rightToRight = PARENT_ID
                        topToTop = PARENT_ID
                        bottomToBottom = PARENT_ID
                        margin = dip(8)
                    }
                }.lparams(width = matchParent, height = wrapContent)
                tagFlowLayout {
                    adapter = object : TagAdapter<String>(mVals) {
                        override fun getView(parent: FlowLayout, position: Int, s: String): View {
                            val tv = LayoutInflater.from(context).inflate(R.layout.picture_tag, this@tagFlowLayout, false) as TextView
                            tv.text = s
                            if (s.equals("R-18") || s.equals("R-18G")) {
                                tv.setTextColor(Color.RED)
                            }
                            return tv
                        }
                    }
                    setOnTagClickListener { view, position, parent ->
                        val bundle = Bundle()
                        bundle.putString("searchword", mVals[position])
                        val intent = Intent(activity!!.applicationContext, SearchResultActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        true
                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(6)
                }
                cardView {
                    textView {
                        text = if (pictureMViewModel.illustDetailResponse.value!!.illust.caption.isNotBlank()) {
                            Html.fromHtml(pictureMViewModel.illustDetailResponse.value!!.illust.caption)
                        } else {
                            resources.getString(R.string.nodiscribe)
                        }
                        setTextIsSelectable(true)
                        isLongClickable = true
                        autoLinkMask = Linkify.WEB_URLS
                        isEnabled = true
                    }.lparams(width = matchParent, height = wrapContent) {
                        margin = dip(8)
                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(8)
                    padding = dip(8)
                }
                relativeLayout {
                    textView(getText(R.string.viewcomment)) {
                        textSize = 14f
                        setOnClickListener {
                            val commentDialog = CommentDialog.newInstance(pictureMViewModel.illustDetailResponse.value!!.illust.id)
                            commentDialog.show(fragmentManager)
                        }
                    }.lparams(width = wrapContent, height = wrapContent) {
                        alignParentBottom()
                        centerHorizontally()
                        bottomMargin = dip(20)
                    }
                }.lparams(width = matchParent, height = dip(50))
                textView(getString(R.string.aboutpic)) {
                }.lparams(width = wrapContent, height = wrapContent) {
                    margin = dip(8)
                }
            }
        }.view
        pictureAdapter!!.addFooterView(viewinfoot)
        pictureAdapter!!.addFooterView(UI {
            linearLayout {
                recyclerView {
                    adapter = aboutPictureAdapter.apply {
                        setOnItemClickListener { adapter, view, position ->
                            val id = pictureMViewModel.aboutpics.value!![position].id
                            val bundle = Bundle()
                            val arrayList = LongArray(1)
                            arrayList[0] = id
                            bundle.putLongArray("illustlist", arrayList)
                            bundle.putLong("illustid", id)
                            val intent = Intent(activity!!.applicationContext, PictureActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }.lparams(width = matchParent, height = matchParent) {
                    margin = dip(8)
                }
            }
        }.view)
        recyclerview_srcpicture.adapter = pictureAdapter
        pictureAdapter!!.callback = object : PictureAdapter.Callback {
            override fun onFirst() {
                activity!!.startPostponedEnterTransition()
            }

            override fun onClick() {
                pictureMViewModel.loadmore(param1!!.toLong())
            }
        }
        fab.show()

    }

    private var param1: Long? = null

    lateinit var view: FragmentPictureMBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

//        act!!.setSupportActionBar(toolbar)

        aboutPictureAdapter = AboutPictureAdapter(R.layout.view_aboutpic_item)
        view = FragmentPictureMBinding.inflate(inflater, container, false)
        view.lifecycleOwner = this
//        isViewCreated = true
        return view.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment PictureMFragment.
         */
        @JvmStatic
        fun newInstance(param1: Long): Fragment =
                PictureMFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)

                    }
                }
    }
}
