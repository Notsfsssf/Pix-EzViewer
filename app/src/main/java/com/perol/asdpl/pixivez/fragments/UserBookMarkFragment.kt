package com.perol.asdpl.pixivez.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RecommendAdapter
import com.perol.asdpl.pixivez.dialog.TagsShowDialog
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.UserBookMarkViewModel
import kotlinx.android.synthetic.main.fragment_user_book_mark.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserBookMarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class UserBookMarkFragment : LazyV4Fragment(), TagsShowDialog.Callback {
    override fun loadData() {
        viewmodel!!.first(param1!!, pub).doOnSuccess {
            if (it) {
                val view = layoutInflater.inflate(R.layout.header_bookmark, null)
                val imagebutton = view.findViewById<ImageView>(R.id.imagebutton_showtags)
                recommendAdapter.addHeaderView(view)
                imagebutton.setOnClickListener {
                    showtagdialog()
                }
            }
        }.subscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mrecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mrecyclerview.adapter = recommendAdapter
        recommendAdapter.setOnLoadMoreListener({
            viewmodel!!.OnLoadMoreListener()
        }, mrecyclerview)

        mrefreshlayout.setOnRefreshListener {
            viewmodel!!.OnRefreshListener(param1!!, pub, null)
        }
    }
    override fun onClick(string: String, public: String) {
        viewmodel!!.OnRefreshListener(param1!!, public, if(string.isNotBlank()){string}else{null})
    }

    fun lazyLoad() {
        viewmodel = ViewModelProviders.of(this).get(UserBookMarkViewModel::class.java)
        viewmodel!!.nexturl.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                recommendAdapter.loadMoreEnd()
            } else {
                recommendAdapter.loadMoreComplete()
            }
        })
        viewmodel!!.data.observe(this, Observer {
            if (it != null) {
                mrefreshlayout.isRefreshing = false
                recommendAdapter.setNewData(it)

            }

        })
        viewmodel!!.adddata.observe(this, Observer {
            if (it != null) {
                recommendAdapter.addData(it)
                recommendAdapter.loadMoreComplete()
            }
        })
        viewmodel!!.tags.observe(this, Observer {

        })

    }

//    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        when (position) {
//            0 -> {
//                pub = "public"
//
//            }
//            1 -> {
//                pub = "private"
//
//            }
//        }
//        if (!first)
//            viewmodel!!.OnRefreshListener(param1!!, pub, null)
//        else first = false
//    }

    var first = true

    // TODO: Rename and change types of parameters
    private var param1: Long? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        lazyLoad()

    }

    var pub = "public"

    var viewmodel: UserBookMarkViewModel? = null


    fun showtagdialog() {
        val arrayList = ArrayList<String>()
        val arrayList1 = ArrayList<Int>()
        if (viewmodel!!.tags.value != null) {
            val tagsShowDialog = TagsShowDialog()
            tagsShowDialog.callback = this
            for (i in viewmodel!!.tags.value!!.bookmark_tags) {
                arrayList.add(i.name)
                arrayList1.add(i.count)
            }
            val bundle = Bundle()
            bundle.putStringArrayList("tags", arrayList)
            bundle.putIntegerArrayList("counts", arrayList1)
            bundle.putString("nexturl", viewmodel!!.tags.value!!.next_url)
            bundle.putLong("id", param1!!)
            tagsShowDialog.setArguments(bundle)
            tagsShowDialog.show(childFragmentManager)
        }


    }

    lateinit var recommendAdapter: RecommendAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        recommendAdapter = RecommendAdapter(R.layout.view_recommand_item, null, PxEZApp.isR18On)
        return inflater.inflate(R.layout.fragment_user_book_mark, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserBookMarkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long, param2: String) =
                UserBookMarkFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
