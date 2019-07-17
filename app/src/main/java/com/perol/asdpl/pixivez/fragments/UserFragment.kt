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

import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.UserMActivity
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.responses.SearchUserResponse
import com.perol.asdpl.pixivez.viewmodel.UserViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.userFactory
import kotlinx.android.synthetic.main.fragment_user.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserFragment : LazyV4Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var userShowAdapter: UserShowAdapter;
    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun lazyLoad() {
        userViewModel = ViewModelProviders.of(this, userFactory()).get(UserViewModel::class.java)

        userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)
        recyclerview_user.adapter = userShowAdapter
        recyclerview_user.layoutManager = LinearLayoutManager(activity)
        userViewModel.users.observe(this, Observer {
            users(it)
        })
        userViewModel.getSearchUser(param1!!)
        userViewModel.nexturl.observe(this, Observer {
            nexturl(it)
        })
        userShowAdapter.setOnLoadMoreListener( {
            if (userViewModel.nexturl.value != null)
                    userViewModel.getnextusers(userViewModel.nexturl.value!!)

        },recyclerview_user)
        userShowAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity!!.applicationContext, UserMActivity::class.java)
            intent.putExtra("data", userShowAdapter.data[position].user.id)
            startActivity(intent)
        }

    }

    private fun users(it: SearchUserResponse?) {
        if (it != null) {
            userShowAdapter.addData(it.user_previews)
        }
    }

    private fun nexturl(it: String?) {
        if (it != null) {
            userShowAdapter.loadMoreComplete()
        } else userShowAdapter.loadMoreEnd()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                UserFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
