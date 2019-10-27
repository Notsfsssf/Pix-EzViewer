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

package com.perol.asdpl.pixivez.dialog


import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.databinding.DialogSearchsectionBinding

class SearchSectionDialog : DialogFragment() {
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "LongHoldDialogFragment")
    }

    var callback: Callback? = null
    override fun onDestroy() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss();
        }
        super.onDestroy()
        callback = null
    }

    interface Callback {
        fun onClick(search_target: Int, sort: Int, duration: Int)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(activity)
        val inflater = activity!!.layoutInflater
        val binding = inflate<DialogSearchsectionBinding>(inflater, R.layout.dialog_searchsection, null, false)
        val view = binding.root
        val first = view.findViewById<TabLayout>(R.id.tablayout_search_target)
        val second = view.findViewById<TabLayout>(R.id.tablayout_sort)
        val third = view.findViewById<TabLayout>(R.id.tablayout_duration)
        builder.setView(view)
        builder.setNegativeButton(android.R.string.ok) { p0, p1 ->
            if (callback != null) {
                callback!!.onClick(first.selectedTabPosition, second.selectedTabPosition, third.selectedTabPosition)
            }
        }
        builder.setPositiveButton(android.R.string.cancel) { p0, p1 -> }

        val dialog = builder.create()

        return dialog
    }
}