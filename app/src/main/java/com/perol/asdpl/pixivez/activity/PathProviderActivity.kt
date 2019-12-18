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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PathProviderAdapter
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.activity_path_provider.*
import java.io.File

class PathProviderActivity : RinkActivity() {
    lateinit var nowPath: String;
    lateinit var nowPathLists: ArrayList<File>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        var a = 1
        nowPath = getExternalFilesDir(null)!!.absolutePath
        nowPathLists = ArrayList<File>()
        val pathProviderAdapter = PathProviderAdapter(R.layout.view_item_path_provider, getPathList(nowPath))
        setContentView(R.layout.activity_path_provider)
        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setHomeButtonEnabled(true);
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        }
        clicktotop.setOnClickListener {
            if (nowPath != getExternalFilesDir(null)!!.absolutePath) {
                nowPath = File(nowPath).parent
                pathProviderAdapter.setNewData(getPathList(nowPath))
                textView!!.text = nowPath
            }
        }
        textView.text = nowPath
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@PathProviderActivity)
            adapter = pathProviderAdapter.apply {
                onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    nowPathLists = adapter.data as ArrayList<File>
                    nowPath = nowPathLists[position].path
                    pathProviderAdapter.setNewData(getPathList(nowPath))
                    textView!!.text = nowPath
                }
            }
        }


    }

    fun getPathList(path: String): ArrayList<File> {
        val file = File(path)
        val listFiles = file.listFiles().toList()
        val listPaths = ArrayList<File>()
        for (i in listFiles) {
            if (i.isDirectory)
                listPaths.add(i)
        }
        return listPaths
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                this.setResult(Activity.RESULT_OK, Intent().apply {
                    this.putExtra("path", nowPath)
                })
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_path, menu)
        return true
    }

}
