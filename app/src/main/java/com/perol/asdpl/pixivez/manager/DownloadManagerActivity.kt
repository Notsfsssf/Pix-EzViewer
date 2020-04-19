package com.perol.asdpl.pixivez.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arialyy.aria.core.Aria
import com.perol.asdpl.pixivez.R
import kotlinx.android.synthetic.main.activity_download_manager.*


class DownloadManagerActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_download_manager, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {

                startActivity(Intent(this, ManagerSettingsActivity::class.java))
            }
            R.id.action_resume -> {
                Aria.download(this).resumeAllTask()
            }
            R.id.action_cancel -> {
                Aria.download(this).removeAllTask(false);
            }

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_manager)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_body, DownLoadManagerFragment.newInstance()).commitNow()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DownloadManagerActivity::class.java))
        }
    }
}