package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity

class BlockViewModel : ViewModel() {
    private val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    fun getAllTags() = appDatabase.blockTagDao().getAllTags()
    fun deleteSingleTag(blockTagEntity: BlockTagEntity) =
        appDatabase.blockTagDao().deleteTag(blockTagEntity)
}