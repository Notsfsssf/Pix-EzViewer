package com.perol.asdpl.pixivez.adapters.viewpager

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.UserBookMarkFragment
import com.perol.asdpl.pixivez.fragments.UserIllustFragment
import com.perol.asdpl.pixivez.services.PxEZApp

class UserMPagerAdapter(var activity:Activity,fm: FragmentManager, var long: Long,var newInstance: Fragment) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int)=
        when (position) {
            0 ->  UserIllustFragment.newInstance(long, "illust")
            1->UserIllustFragment.newInstance(long, "manga")
             2->UserBookMarkFragment.newInstance(long,"1")
            else->newInstance
        }



    override fun getCount()=4

    override fun getPageTitle(position: Int): CharSequence? {
        val i=    when (position) {
            0->activity.getString(R.string.illust)
            1->activity.getString(R.string.manga)
            2->activity.getString(R.string.bookmark)
            else->activity.getString(R.string.abouts)
        }
        return i
    }


}
