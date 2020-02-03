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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMMyFragment
import com.perol.asdpl.pixivez.fragments.IllustratorFragment

class HelloMThViewPager(fragment: Fragment, var long: Long) : FragmentStateAdapter(fragment) {


    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> {
            HelloMMyFragment.newInstance("1", "2")

        }
        else -> {
            IllustratorFragment.newInstance(long, true)
        }

    }

/*    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> {
                    context.getString(R.string.new1)
                }
                else -> {
                    context.getString(R.string.painter)
                }
            }


    override fun getCount() = 2*/

}