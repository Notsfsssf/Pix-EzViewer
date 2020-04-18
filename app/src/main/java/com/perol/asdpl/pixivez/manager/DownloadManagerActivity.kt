package com.perol.asdpl.pixivez.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perol.asdpl.pixivez.R
import kotlinx.android.synthetic.main.activity_download_manager.*


class DownloadManagerActivity : AppCompatActivity() {


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