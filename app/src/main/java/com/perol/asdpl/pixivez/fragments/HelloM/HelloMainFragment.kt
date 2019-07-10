package com.perol.asdpl.pixivez.fragments.HelloM


import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.viewpager.HelloMRecomViewPager
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.design.themedTabLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMainFragment : LazyV4Fragment() {
    override fun lazyLoad() {
        tab.setupWithViewPager(pager)

    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private object Ids {
        val userpic = 1

    }

    lateinit var tab: TabLayout;
    lateinit var pager: ViewPager;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        isViewCreated = true
        return UI {
            verticalLayout {
                appBarLayout {
                    tab = themedTabLayout(theme = R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                        isTabIndicatorFullWidth = true
                        addTab(newTab().setText("推荐"))

                        addTab(newTab().setText("画师"))
                        setSelectedTabIndicatorColor(Color.WHITE)
                    }.lparams(width = matchParent, height = wrapContent) {

                    }
                }.lparams(width = matchParent, height = wrapContent)
                pager = viewPager {
                    id = Ids.userpic
                    adapter = HelloMRecomViewPager(this@HelloMainFragment.context,childFragmentManager).apply {
                        offscreenPageLimit = 2
                    }

                }.lparams(width = matchParent, height = matchParent)
            }
        }.view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HelloMainFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
