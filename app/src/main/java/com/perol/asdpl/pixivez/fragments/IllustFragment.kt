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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.dialog.SearchSectionDialog
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.objects.BaseFragment
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.Illust
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel
import kotlinx.android.synthetic.main.fragment_illust.*
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "word"


/**

 * Use the [IllustFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IllustFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    override fun loadData() {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AdapterRefreshEvent) {
        runBlocking {
            val allTags = blockViewModel.getAllTags()
            blockTags = allTags.map {
                it.name
            }
            searchIllustAdapter.blockTags = blockTags
            searchIllustAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (view != null) {
            selectSort = position
            if (position == 2) {
                runBlocking {
                    val user = AppDataRepository.getUser()
                    if (!user.ispro) {
                        Toasty.error(PxEZApp.instance, "not premium!").show()
                        viewModel.setPreview(param1!!, sort[position], null, null)
                    } else {
                        viewModel.sort.value = sort[position]
                        viewModel.firstSetData(param1!!)
                    }

                }
            } else {
                viewModel.sort.value = sort[position]
                viewModel.firstSetData(param1!!)
            }
        }


    }

    var exitTime = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchtext = requireActivity().findViewById<TextView>(R.id.searchtext)
        searchIllustAdapter = RecommendAdapter(
            R.layout.view_recommand_item,
            emptyList(),
            isR18on,
            blockTags
        ).apply {
            val view = LayoutInflater.from(requireContext()).inflate(
                R.layout.search_result_header, null
            )
            view.findViewById<Spinner>(R.id.spinner_result).onItemSelectedListener =
                this@IllustFragment
            setHeaderView(view)
        }
        searchtext.text = param1
        recyclerview_illust.adapter = searchIllustAdapter
        recyclerview_illust.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        fab.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(activity)
            val arrayList = arrayOfNulls<String>(starnum.size)
            for (i in starnum.indices) {
                if (starnum[i] == 0)
                    arrayList[i] = ("$param1 users入り")
                else
                    arrayList[i] = (param1 + " " + starnum[i].toString() + "users入り")
            }
            builder.setTitle("users入り")
                .setItems(
                    arrayList
                ) { _, which ->

                    val query = if (starnum[which] == 0)
                        "$param1 users入り"
                    else
                        param1 + " " + starnum[which].toString() + "users入り"
                    viewModel.firstSetData(query)
                    recyclerview_illust.scrollToPosition(0)
                }
            builder.create().show()
        }
        val imageButton = activity!!.findViewById<ImageButton>(R.id.imagebutton_section)
        imageButton.setOnClickListener {
            SearchSectionDialog().apply {
                arguments = Bundle().apply {
                    putString("word", param1!!)
                }
            }.show(childFragmentManager)
        }
        searchIllustAdapter.loadMoreModule?.setOnLoadMoreListener {
            viewModel.onLoadMoreListen()
        }
        swiperefresh.setOnRefreshListener {
            runBlocking {
                val user = AppDataRepository.getUser()
                if (!user.ispro && selectSort == 2) {
                    Toasty.error(PxEZApp.instance, "not premium!").show()
                    viewModel.setPreview(
                        param1!!,
                        sort[selectSort],
                        search_target[selectTarget],
                        duration[selectDuration]
                    )
                } else {
                    viewModel.firstSetData(
                        param1!!
                    )
                }
            }
        }
/*        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {

                    return false
                }

                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    return super.onDoubleTap(e)

                }

                override fun onLongPress(e: MotionEvent?) {
                    super.onLongPress(e)

                }
            })
        requireActivity().findViewById<TabLayout>(R.id.tablayout_searchresult)?.getTabAt(0)
            ?.view?.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
        }*/
        requireActivity().findViewById<TabLayout>(R.id.tablayout_searchresult)?.getTabAt(0)
            ?.view?.setOnClickListener {
            if ((System.currentTimeMillis() - exitTime) > 3000) {

                exitTime = System.currentTimeMillis()
            } else {
                recyclerview_illust.smoothScrollToPosition(0)
            }

        }
    }

    private val starnum = intArrayOf(50000, 30000, 20000, 10000, 5000, 1000, 500, 250, 100, 0)
    private var param1: String? = null
    lateinit var searchIllustAdapter: RecommendAdapter
    var sort = arrayOf("date_desc", "date_asc", "popular_desc")
    var search_target =
        arrayOf("partial_match_for_tags", "exact_match_for_tags", "title_and_caption")

    var duration = arrayOf(
        null,
        "within_last_day",
        "within_last_week",
        "within_last_month",
        "within_half_year",
        "within_year"
    )
    var selectSort: Int = 0
    var selectTarget: Int = 0
    var selectDuration: Int = 0
    private lateinit var viewModel: IllustfragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
        lazyLoad()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_illust, container, false)
    }


    private fun lazyLoad() {
        viewModel = ViewModelProviders.of(this).get(IllustfragmentViewModel::class.java)

        viewModel.illusts.observe(this, Observer {
            updateillust(it)
        })
        viewModel.addIllusts.observe(this, Observer {
            addIllust(it)
        })
        viewModel.nexturl.observe(this, Observer {
            nexturl(it)
        })
        viewModel.bookmarkid.observe(this, Observer {
            changeToBlue(it)
        })
        viewModel.isRefresh.observe(this, Observer {
            swiperefresh.isRefreshing = it
        })

    }

    private fun addIllust(it: java.util.ArrayList<Illust>) {
        searchIllustAdapter.addData(it)
        searchIllustAdapter.loadMoreComplete()
    }

    private var position: Int? = null
    private fun changeToBlue(it: Long?) {
        if (it != null) {
            val item = searchIllustAdapter.getViewByPosition(
                position!!,
                R.id.linearlayout_isbookmark
            ) as LinearLayout
            item.setBackgroundColor(Color.YELLOW)
            Toasty.success(requireActivity(), "收藏成功", Toast.LENGTH_SHORT).show()
        }
    }

    private fun nexturl(it: String?) {
        if (it != null) {

        } else {
            searchIllustAdapter.loadMoreEnd()
        }
    }

    private fun updateillust(it: ArrayList<Illust>?) {
        if (it != null) {
            searchIllustAdapter.setNewData(it)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment IllustFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            IllustFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}
