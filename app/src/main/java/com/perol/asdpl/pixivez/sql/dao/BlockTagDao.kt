package com.perol.asdpl.pixivez.sql.dao

import androidx.room.*
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
abstract class BlockTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(query: BlockTagEntity): Completable

    @Query("SELECT * FROM blockTag")
    abstract fun getAllTags(): Flowable<List<BlockTagEntity>>

    @Delete
    abstract fun deleteTag(blockTagEntity: BlockTagEntity): Completable

}