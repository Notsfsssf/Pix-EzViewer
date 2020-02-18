package com.perol.asdpl.pixivez.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.BlockTagFragment
import kotlinx.android.synthetic.main.activity_block.*


class BlockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
