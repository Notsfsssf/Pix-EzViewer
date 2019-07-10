package com.perol.asdpl.pixivez.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PathProviderAdapter
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.view_ranking_item.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk21.coroutines.onClick
import java.io.File

class PathProviderActivity : RinkActivity() {
    lateinit var nowPath: String;
    lateinit var nowPathLists: ArrayList<File>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        var a=1
        nowPath = Environment.getExternalStorageDirectory().absolutePath
        if(nowPath.isBlank()){
            toast(resources.getString(R.string.noaccess))
            finish()
        }
        nowPathLists = ArrayList<File>()
        val pathProviderAdapter = PathProviderAdapter(R.layout.view_item_path_provider, getPathList(nowPath))
        var textView:TextView?=null
        verticalLayout {
            themedAppBarLayout(theme = R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                toolbar { title = resources.getString(R.string.pickapath) }.lparams(width = matchParent, height = wrapContent).also {
                    setSupportActionBar(it)
                    supportActionBar!!.setHomeButtonEnabled(true);
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true);
                }
            }
            relativeLayout {
             textView=textView(nowPath).lparams() {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(8)
                }
            }.lparams(width = matchParent, height = wrapContent)
            relativeLayout {
                textView("...").lparams() {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(8)
                }
                onClick {
                    if (nowPath != Environment.getExternalStorageDirectory().absolutePath) {
                        nowPath = File(nowPath).parent
                        pathProviderAdapter.setNewData(getPathList(nowPath))
                        textView!!.text=nowPath
                    }
                }
            }.lparams(width = matchParent, height = wrapContent)
            recyclerView {
                layoutManager = LinearLayoutManager(this@PathProviderActivity)
                adapter = pathProviderAdapter.apply {
                    onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                        nowPathLists = adapter.data as ArrayList<File>
                        nowPath = nowPathLists[position].path
                        pathProviderAdapter.setNewData(getPathList(nowPath))
                        textView!!.text=nowPath
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
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
            android.R.id.home  -> {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_path, menu)
        return true
    }

}
