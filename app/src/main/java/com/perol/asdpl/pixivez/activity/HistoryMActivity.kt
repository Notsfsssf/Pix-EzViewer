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

package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.HistoryAdapter
import com.perol.asdpl.pixivez.databinding.ActivityHistoryMBinding
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.sql.IllustBeanEntity
import com.perol.asdpl.pixivez.viewmodel.HistoryMViewModel
import kotlinx.android.synthetic.main.activity_history_m.*


class HistoryMActivity : RinkActivity() {
    private lateinit var activityHistoryMBinding: ActivityHistoryMBinding
    private var historyMViewModel: HistoryMViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        activityHistoryMBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_m)
        initView()
        initData()
        initBind()
    }

    private fun initBind() {
        fab.setOnClickListener {
            historyMViewModel!!.fabOnClick()
        }
        historyAdapter.setOnItemClickListener { _, _, position ->
            val bundle = Bundle()
            val arrayList = LongArray(1)
            arrayList[0] = (historyMViewModel!!.illustBeans.value!![position].illustid)
            bundle.putLongArray("illustidlist", arrayList)
            bundle.putLong("illustid", historyMViewModel!!.illustBeans.value!![position].illustid)
            val intent2 = Intent(applicationContext, PictureActivity::class.java)
            intent2.putExtras(bundle)
            startActivity(intent2)
        }
        historyAdapter.setOnItemLongClickListener { _, _, i ->
            MaterialAlertDialogBuilder(this)
                    .setTitle("Delete?").setPositiveButton("OK") { _, j ->
                        historyMViewModel!!.deleteSelect(i)
                    }.show()
            true
        }
    }

    private fun initData() {
        historyMViewModel = ViewModelProviders.of(this).get(HistoryMViewModel::class.java)

        historyMViewModel!!.illustBeans.observe(this, Observer {
            illustBeans(it)
        })
        historyMViewModel!!.first()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }

    private val historyAdapter = HistoryAdapter(R.layout.view_recommand_itemh)
    private fun illustBeans(it: ArrayList<IllustBeanEntity>?) {
        historyAdapter.setNewData(it)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerview_historym.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerview_historym.adapter = historyAdapter

        recyclerview_historym.smoothScrollToPosition(historyAdapter.data.size)

    }
}
