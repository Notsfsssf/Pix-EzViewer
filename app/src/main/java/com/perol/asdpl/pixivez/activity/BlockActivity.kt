package com.perol.asdpl.pixivez.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import com.perol.asdpl.pixivez.viewmodel.BlockViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_block.*


class BlockActivity : AppCompatActivity() {
    lateinit var viewModel: BlockViewModel
    private val mDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)
        viewModel = ViewModelProviders.of(this).get(BlockViewModel::class.java)

    }

    override fun onStop() {
        super.onStop()

        // clear all the subscriptions
        mDisposable.clear()
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
            viewModel.deleteSingleTag(blockTagEntity).observeOn(AndroidSchedulers.mainThread())
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .doOnComplete {

                }.doOnError {

                }
            true
        }
        return chip
    }

    private fun getTagList() {
        mDisposable.add(
            viewModel.getAllTags().observeOn(AndroidSchedulers.mainThread()).observeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe({
                    chipgroup.removeAllViews()
                    it.forEach { v ->
                        chipgroup.addView(getChip(v))
                    }
                }, {}, {})
        )
    }

    override fun onStart() {
        super.onStart()
        getTagList()
    }

}
