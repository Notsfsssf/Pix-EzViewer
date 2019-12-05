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


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ToggleButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel
import com.perol.asdpl.pixivez.viewmodel.generateDateString
import java.util.*

class SearchSectionDialog : DialogFragment() {
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "LongHoldDialogFragment")
    }

    val tms = Calendar.getInstance()
    val thisMonth01 = "${tms.get(Calendar.YEAR)}-${tms.get(Calendar.MONTH) + 1}-01"
    val halfYear01 = "${tms.get(Calendar.YEAR)}-${tms.get(Calendar.MONTH) + 1}-01"
    var sort = arrayOf("date_desc", "date_asc", "popular_desc")
    var search_target =
        arrayOf("partial_match_for_tags", "exact_match_for_tags", "title_and_caption")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val word = arguments?.getString("word", "")
        val builder = MaterialAlertDialogBuilder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(
            R.layout.dialog_searchsection,
            null
        )
        val viewModel =
            ViewModelProviders.of(requireParentFragment()).get(IllustfragmentViewModel::class.java)
        val first = view.findViewById<TabLayout>(R.id.tablayout_search_target).apply {
            clearOnTabSelectedListeners()
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewModel.searchTarget.value = search_target[tab.position]
                }
            })
        }
        val second = view.findViewById<TabLayout>(R.id.tablayout_sort)
            .apply {
                clearOnTabSelectedListeners()
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabSelected(tab: TabLayout.Tab) {
                        viewModel.sort.value = sort[tab.position]
                    }
                })
            }

        val toggleButton = view.findViewById<ToggleButton>(R.id.toggle).apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                view.findViewById<LinearLayout>(R.id.pick_date_layout).visibility = if (isChecked) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            isChecked = viewModel.endDate.value != null && viewModel.startDate.value != null

        }
        val button = view.findViewById<Button>(R.id.pick_button).apply {
            var calendar = Calendar.getInstance()
            if (viewModel.endDate.value != null) {
                calendar = viewModel.startDate.value
                this.text = viewModel.startDate.value.generateDateString()
            }
            setOnClickListener {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val dateDialog = DatePickerDialog(
                    requireActivity(),
                    DatePickerDialog.OnDateSetListener { p0, year1, month1, day1 ->
                        val monthR = month1 + 1
                        text = "${year1}-${monthR}-${day1}"
                        val calendar1 = Calendar.getInstance()
                        calendar1.set(year1, month1, day1)
                        viewModel.startDate.value = calendar1
                    },
                    year,
                    month,
                    day
                )
                dateDialog.datePicker.maxDate = System.currentTimeMillis()
                dateDialog.show()

            }
        }
        view.findViewById<Button>(R.id.pick_end_button).apply {
            var calendar = Calendar.getInstance()
            if (viewModel.endDate.value != null) {
                calendar = viewModel.endDate.value
                this.text = viewModel.endDate.value.generateDateString()
            }
            setOnClickListener {


                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val dateDialog = DatePickerDialog(
                    requireActivity(),
                    DatePickerDialog.OnDateSetListener { p0, year1, month1, day1 ->
                        val monthR = month1 + 1
                        val calendar1 = Calendar.getInstance()
                        calendar1.set(year1, month1, day1)
                        text = "${year1}-${monthR}-${day1}"
                        viewModel.endDate.value = calendar1
                    },
                    year,
                    month,
                    day
                )
                dateDialog.datePicker.maxDate = System.currentTimeMillis()
                dateDialog.show()
            }
        }

        builder.setView(view)
        builder.setNegativeButton(android.R.string.cancel) { p0, p1 ->

        }
        builder.setPositiveButton(android.R.string.ok) { p0, p1 ->
            if (word != null)
                viewModel.firstSetData(word)
        }

        val dialog = builder.create()

        return dialog
    }
}