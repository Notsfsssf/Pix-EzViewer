package com.perol.asdpl.pixivez.activity

import android.os.Bundle
import android.view.MenuItem
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.BlockTagFragment
import com.perol.asdpl.pixivez.objects.ThemeUtil
import kotlinx.android.synthetic.main.activity_block.*


class BlockActivity : RinkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_block)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, BlockTagFragment.newInstance("", "")).commit()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }

}
