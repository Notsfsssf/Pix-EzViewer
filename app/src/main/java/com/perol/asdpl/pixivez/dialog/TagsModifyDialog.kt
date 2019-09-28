package com.perol.asdpl.pixivez.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.TagsAdapter

class TagsModifyDialog: DialogFragment() {
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "TagsModifyDialog")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_star, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder
                    .setPositiveButton("LIKE！",
                            DialogInterface.OnClickListener { dialog, id ->
                                // FIRE ZE MISSILES!
                            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}