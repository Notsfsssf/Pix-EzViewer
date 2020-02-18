package com.perol.asdpl.pixivez.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import com.perol.asdpl.pixivez.viewmodel.BlockViewModel
import kotlinx.android.synthetic.main.activity_block.*
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus


class BlockActivity : AppCompatActivity() {
    lateinit var viewModel: BlockViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(BlockViewModel::class.java)

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun getChip(blockTagEntity: BlockTagEntity): Chip {
        val chip = Chip(this)
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 5f,
            resources.displayMetrics
        ).toInt()
        chip.text = "${blockTagEntity.name} ${blockTagEntity.translateName}"

        chip.setOnLongClickListener {
            runBlocking {
                viewModel.deleteSingleTag(blockTagEntity)
                getTagList()
            }
            EventBus.getDefault().post(AdapterRefreshEvent())
            true
        }
        return chip
    }

    private fun getTagList() {
        runBlocking {
            val it = viewModel.getAllTags()
            chipgroup.removeAllViews()
            it.forEach { v ->
                chipgroup.addView(getChip(v))
            }
        }

    }

    override fun onStart() {
        super.onStart()
        getTagList()
    }

}
