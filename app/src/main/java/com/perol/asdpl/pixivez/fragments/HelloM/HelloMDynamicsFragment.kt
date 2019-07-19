package com.perol.asdpl.pixivez.fragments.HelloM


import androidx.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RankingMAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.viewmodel.factory.RankingShareViewModel
import kotlinx.android.synthetic.main.fragment_hello_mdynamics.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [HelloMDynamicsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMDynamicsFragment : LazyV4Fragment() {
    override fun loadData() {

    }


    fun lazyLoad() {
        initview()
    }

    private fun initview() {
        viewpage_rankingm.adapter = RankingMAdapter(context!!,childFragmentManager)
        val sharemodel = ViewModelProviders.of(activity!!).get(RankingShareViewModel::class.java)
        tablayout_rankingm.setupWithViewPager(viewpage_rankingm)

    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoad()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hello_mdynamics, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMDynamicsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                HelloMDynamicsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)

                    }
                }
    }
}
