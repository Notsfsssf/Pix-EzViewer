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

package com.perol.asdpl.pixivez.fragments


import TagsBookMarkDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.BlockActivity
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.PictureXAdapter
import com.perol.asdpl.pixivez.databinding.FragmentPictureXBinding
import com.perol.asdpl.pixivez.dialog.CommentDialog
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.objects.BaseFragment
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.viewmodel.PictureXViewModel
import kotlinx.android.synthetic.main.fragment_picture_x.*
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass for pic detail.
 * Use the [PictureXFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PictureXFragment : BaseFragment() {

    private var param1: Long? = null
    private var param2: Illust? = null
    private lateinit var pictureXViewModel: PictureXViewModel
    override fun loadData() {
//        val item = activity?.intent?.extras
//        val illust = item?.getParcelable<Illust>(param1.toString())
        pictureXViewModel.firstGet(param1!!,param2)
    }

    override fun onDestroy() {
        if (pictureXAdapter != null) {
            pictureXAdapter?.setListener { }
            pictureXAdapter?.setViewCommentListen { }
            pictureXAdapter?.setUserPicLongClick { }
        }
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
        pictureXAdapter?.imageViewGif?.startPlay()
    }

    override fun onPause() {
        pictureXAdapter?.imageViewGif?.pausePlay()
        super.onPause()

    }

    private var pictureXAdapter: PictureXAdapter? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AdapterRefreshEvent) {
        runBlocking {
            val allTags = blockViewModel.getAllTags()
            blockTags = allTags.map {
                it.name
            }
            var needBlock = false
            pictureXViewModel.illustDetail.value?.tags?.forEach {
                if (blockTags.contains(it.name)) needBlock = true
            }
            if (!needBlock) {
                block_view.visibility = View.GONE
            }
        }
    }

    private fun initViewModel() {

        pictureXViewModel = ViewModelProvider(this).get(PictureXViewModel::class.java)
        pictureXViewModel.illustDetail.observe(this, Observer {
            progress_view.visibility = View.GONE
            if (it != null) {
                val tags = it.tags.map {
                    it.name
                }
                for (i in tags) {
                    if (blockTags.contains(i)) {
                        jump_button.setOnClickListener {
                            startActivity(Intent(requireActivity(), BlockActivity::class.java))
                        }
                        blocktag_textview.text = "${i}"
                        block_view.visibility = View.VISIBLE
                    }
                }
                rootBinding.illust = it

                if (it.meta_pages.isNotEmpty())
                    position = it.meta_pages.size
                else position = 1
                pictureXAdapter =
                    PictureXAdapter(pictureXViewModel, it, requireContext()).also {
                        it.setListener {
                            //                        activity?.supportStartPostponedEnterTransition()
                            if (!hasMoved) {
                                recyclerview?.scrollToPosition(0)
                                (recyclerview?.layoutManager as LinearLayoutManager)?.scrollToPositionWithOffset(
                                    0,
                                    0
                                )
                            }
                            pictureXViewModel.getRelative(param1!!)

                        }
                        it.setViewCommentListen {
                            val commentDialog =
                                CommentDialog.newInstance(pictureXViewModel.illustDetail.value!!.id)
                            commentDialog.show(childFragmentManager)
                        }
                        it.setUserPicLongClick {
                            pictureXViewModel.likeUser()
                        }

                    }

                recyclerview.adapter = pictureXAdapter
                if (it.user.is_followed)
                    imageViewUser_picX.setBorderColor(Color.YELLOW)
                //else
                //    imageViewUser_picX.setBorderColor(ContextCompat.getColor(requireContext(), colorPrimary))
                imageViewUser_picX.setOnClickListener { ot ->
                    val intent = Intent(context, UserMActivity::class.java)
                    intent.putExtra("data", it.user.id)
                    startActivity(intent)
                }
                fab.show()

            }
        })
        pictureXViewModel.aboutPics.observe(this, Observer {
            if (pictureXAdapter != null) {
                pictureXAdapter?.setRelativeNow(it)
            }
        })
        pictureXViewModel.likeIllust.observe(this, Observer {
            if (it != null) {
                if (it) {
                    GlideApp.with(this).load(R.drawable.heart_red).into(fab)
                } else {
                    GlideApp.with(this).load(R.drawable.ic_action_heart).into(fab)
                }

            }
        })
        pictureXViewModel.followUser.observe(this, Observer {
            pictureXAdapter?.setUserPicColor(it)
        })
        pictureXViewModel.progress.observe(this, Observer {
            if (pictureXAdapter != null) {
                pictureXAdapter?.setProgress(it)
            }
        })
        pictureXViewModel.downloadGifSuccess.observe(this, Observer {

            pictureXAdapter?.setProgressComplete(it)
        })


    }

    var hasMoved = false
    private fun initView() {
        fab.setOnClickListener {
            pictureXViewModel.fabClick()
        }
        fab.setOnLongClickListener {
            if (pictureXViewModel.illustDetail.value!!.is_bookmarked) {
                return@setOnLongClickListener true
            }
            Toasty.info(
                requireActivity(),
                resources.getString(R.string.fetchtags),
                Toast.LENGTH_SHORT
            )
                .show()
            val tagsBookMarkDialog = TagsBookMarkDialog();
            tagsBookMarkDialog.show(childFragmentManager, TagsBookMarkDialog::class.java.name)
            true
        }
        imageButton.setOnClickListener {
            recyclerview.scrollToPosition(position)
        }
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hasMoved = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == position || linearLayoutManager.findFirstCompletelyVisibleItemPosition() == position || linearLayoutManager.findFirstVisibleItemPosition() == position || linearLayoutManager.findLastVisibleItemPosition() == position) {
                    constraintLayout_fold.visibility = View.INVISIBLE
                } else
                    constraintLayout_fold.visibility = View.VISIBLE
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getParcelable(ARG_PARAM2)?:null
        }
        initViewModel()
    }

    lateinit var rootBinding: FragmentPictureXBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootBinding = FragmentPictureXBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PictureXFragment
        }
        return rootBinding.root
    }

    var position = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PictureXFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long?,param2: Illust?) =
            PictureXFragment().apply {
                arguments = Bundle().apply {
                    if (param2 != null) {
                        putParcelable(ARG_PARAM2, param2)
                        putLong(ARG_PARAM1, param2.id)
                    } else {
                        putLong(ARG_PARAM1, param1!!)
                    }
/*                    putParcelable("illust", illust)*/
                }
            }
    }
}
