package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.ViewModel
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BlockViewModel : ViewModel() {
    private val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    suspend fun getAllTags() =
        withContext(Dispatchers.IO) {
            appDatabase.blockTagDao().getAllTags()
        }


    suspend fun deleteSingleTag(blockTagEntity: BlockTagEntity) =
        appDatabase.blockTagDao().deleteTag(blockTagEntity)

    suspend fun insertBlockTag(blockTagEntity: BlockTagEntity) = appDatabase.blockTagDao()
        .insert(blockTagEntity)
}