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

package com.perol.asdpl.pixivez.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.perol.asdpl.pixivez.fragments.UserBookMarkFragment
import com.perol.asdpl.pixivez.fragments.UserIllustFragment

class UserMPagerAdapter(
    var lifecycle: Lifecycle,
    fm: FragmentManager,
    var long: Long,
    var newInstance: Fragment
) : FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount() = 4

    override fun createFragment(position: Int) = when (position) {
        0 -> UserIllustFragment.newInstance(long, "illust")
        1 -> UserIllustFragment.newInstance(long, "manga")
        2 -> UserBookMarkFragment.newInstance(long, "1")
        else -> newInstance
    }

/*
    override fun getCount() = 4

    override fun getPageTitle(position: Int): CharSequence? {
        val i = when (position) {
            0 -> activity.getString(R.string.illust)
            1 -> activity.getString(R.string.manga)
            2 -> activity.getString(R.string.bookmark)
            else -> activity.getString(R.string.abouts)
        }
        return i
    }*/


}
