package com.perol.asdpl.pixivez.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ThanksAdapter

class ThanksDialog : DialogFragment() {
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "ThanksDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!, R.style.AlertDialogCustom)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_thanks, null)
        val re = view.findViewById<RecyclerView>(R.id.list)
        val strings = activity!!.getString(R.string.thanksother)
        val array = strings.split(",")
        re.adapter = ThanksAdapter(R.layout.simple_list_item, array)
        re.layoutManager= LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        builder.setView(view)
        return builder.create()
    }
}