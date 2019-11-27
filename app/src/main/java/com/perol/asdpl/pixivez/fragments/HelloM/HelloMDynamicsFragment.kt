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

package com.perol.asdpl.pixivez.fragments.HelloM


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RankingMAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.viewmodel.factory.RankingShareViewModel
import kotlinx.android.synthetic.main.fragment_hello_mdynamics.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


/**
 * A simple [Fragment] subclass.
 * Use the [HelloMDynamicsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMDynamicsFragment : LazyV4Fragment() {
    override fun loadData() {

    }


    fun lazyLoad() {
        initview()
    }

    private fun initview() {
        viewpage_rankingm.adapter = RankingMAdapter(context!!, childFragmentManager)
        val shareModel = ViewModelProviders.of(activity!!).get(RankingShareViewModel::class.java)
        tablayout_rankingm.setupWithViewPager(viewpage_rankingm)
        val calendar = Calendar.getInstance()
        val yearNow = calendar.get(Calendar.YEAR)
        val monthNow = calendar.get(Calendar.MONTH) + 1
        val dayNow = calendar.get(Calendar.DAY_OF_MONTH)
        val dateNow = "$yearNow-$monthNow-$dayNow"
        imageview_triangle.apply {
            setOnClickListener {
                shareModel.apply {
                    val dateDialog = DatePickerDialog(
                        activity!!,
                        DatePickerDialog.OnDateSetListener { p0, year1, month1, day1 ->
                            val monthR = month1 + 1

                            picDateShare.value = if ("$year1-$monthR-$day1" == dateNow) {
                                null
                            } else {
                                "$year1-$monthR-$day1"
                            }
                            year.value = year1
                            month.value = month1
                            day.value = day1
                            com.orhanobut.logger.Logger.i(
                                year.value!!.toString(),
                                month.value!!,
                                day.value!!
                            )
                        },
                        year.value!!,
                        month.value!!,
                        day.value!!
                    )
                    dateDialog.datePicker.maxDate = System.currentTimeMillis()
                    dateDialog.show()
                }


            }
        }
    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoad()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hello_mdynamics, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMDynamicsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            HelloMDynamicsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}
