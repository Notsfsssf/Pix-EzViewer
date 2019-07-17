package com.perol.asdpl.pixivez.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.viewmodel.IllustratorViewModel
import kotlinx.android.synthetic.main.fragment_illustrator.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IllustratorFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IllustratorFragment :LazyV4Fragment(), AdapterView.OnItemSelectedListener {
    var firststart = true
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                restrict = "public"
                viewModel!!.OnRefresh(param1!!, restrict, param2!!)

            }
            1 -> {
                restrict = "private"
                viewModel!!.OnRefresh(param1!!, restrict, param2!!)
            }
        }
        if (!firststart) {

        } else {
            firststart = false
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    lateinit var userShowAdapter: UserShowAdapter
    var viewModel: IllustratorViewModel? = null
    var restrict: String = "public"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview_illustrator.adapter = userShowAdapter
    }
    override fun lazyLoad() {

        recyclerview_illustrator.layoutManager = LinearLayoutManager(activity!!.applicationContext, RecyclerView.VERTICAL, false)
        spinner_illustrator.onItemSelectedListener = this
        userShowAdapter.setOnLoadMoreListener({
            viewModel!!.OnLoadMore(viewModel!!.nexturl.value!!)
        }, recyclerview_illustrator)
        viewModel = ViewModelProviders.of(this).get(IllustratorViewModel::class.java)
        viewModel!!.userpreviews.observe(this, Observer {
            userpreviews(it)
        })
        viewModel!!.nexturl.observe(this, Observer {
            nexturl(it)
        })
        viewModel!!.adduserpreviews.observe(this, Observer {
            adduserpreviews(it)
        })
        viewModel!!.first(param1!!, restrict, param2!!)
        swiperefresh_illustrator.setOnRefreshListener {
            viewModel!!.OnRefresh(param1!!, restrict, param2!!)
        }
        viewModel!!.refreshcomplete.observe(this, Observer {
            if (it != null) {
                swiperefresh_illustrator.isRefreshing = false
            }
        })
        userShowAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity!!.applicationContext, UserMActivity::class.java)
            val userid = viewModel!!.userpreviews.value!![position].user.id
            intent.putExtra("data", userid)
            startActivity(intent)
        }
    }

    private fun adduserpreviews(it: ArrayList<SearchUserResponse.UserPreviewsBean>?) {
        if (it != null) {
            userShowAdapter.addData(it)
        }
    }

    private fun nexturl(it: String?) {
        if (it != null) {
            userShowAdapter.loadMoreComplete()
        } else {
            userShowAdapter.loadMoreEnd()
        }
    }

    private fun userpreviews(it: ArrayList<SearchUserResponse.UserPreviewsBean>?) {
        if (it != null) {
            userShowAdapter.setNewData(it)
        }
    }

    // TODO: Rename and change types of parameters
    private var param1: Long? = null
    private var param2: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)

        return inflater.inflate(R.layout.fragment_illustrator, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 isuserfollowing 2.
         * @return A new instance of fragment IllustratorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long, param2: Boolean) =
                IllustratorFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)
                        putBoolean(ARG_PARAM2, param2)
                    }
                }
    }
}
