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
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.perol.asdpl.pixivez.R

class ThemeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_theme, null)
            val switchMaterial = view.findViewById<SwitchMaterial>(R.id.switchmaterial)
            switchMaterial.apply {
                //                isChecked = Colorful().getDarkTheme()
                setOnCheckedChangeListener { compoundButton, b ->
                    //                    Colorful().edit().setDarkTheme(isChecked).apply(requireActivity()).apply {
//                        requireActivity().recreate()
//                    }
                    val mode = if (b) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
//            recyclerView.apply {
//                layoutManager = GridLayoutManager(requireActivity(), 4)
//                adapter = ColorfulAdapter(R.layout.view_colorfulitem, list).apply {
//                    onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
//                        SharedPreferencesServices.getInstance().setInt("colornum", position)
//                        Colorful().edit().setPrimaryColor(list[position]).apply(requireActivity()) {
//                            requireActivity().recreate()
//                        }
//                    }
//                }
//            }
            val builder = MaterialAlertDialogBuilder(it)
                    .setView(view)
            builder.setMessage("Theme")

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}