package com.perol.asdpl.pixivez.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil.inflate
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.perol.asdpl.pixivez.R


import com.perol.asdpl.pixivez.databinding.DialogSearchsectionBinding
import java.time.Duration

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
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val binding = inflate<DialogSearchsectionBinding>(inflater, R.layout.dialog_searchsection, null, false)
        val view = binding.root
        val first = view.findViewById<TabLayout>(R.id.tablayout_search_target)
        val second = view.findViewById<TabLayout>(R.id.tablayout_sort)
        val third = view.findViewById<TabLayout>(R.id.tablayout_duration)
        builder.setView(view)
        builder.setNegativeButton("确认") { p0, p1 ->
            if (callback != null) {
                callback!!.onClick(first.selectedTabPosition, second.selectedTabPosition, third.selectedTabPosition)
            }
        }
        builder.setPositiveButton("取消") { p0, p1 -> }

        val dialog = builder.create()

        return dialog
    }
}