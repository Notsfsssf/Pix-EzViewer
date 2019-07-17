package com.perol.asdpl.pixivez.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMDynamicsFragment
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMThFragment
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMainFragment

class HelloMViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HelloMainFragment.newInstance("s","s")

            }
            1 -> {
                HelloMDynamicsFragment.newInstance("d")
            }
            3->{
                HelloMThFragment.newInstance("d","c")
            }
            else->{
                HelloMThFragment.newInstance("d","c")
            }
        }
    }


}
