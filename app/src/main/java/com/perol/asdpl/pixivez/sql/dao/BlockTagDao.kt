package com.perol.asdpl.pixivez.sql.dao

import androidx.room.*
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import io.reactivex.*

@Dao
abstract class BlockTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(query: BlockTagEntity)

    @Query("SELECT * FROM blockTag")
    abstract suspend fun getAllTags(): List<BlockTagEntity>

    @Delete
    abstract suspend fun deleteTag(blockTagEntity: BlockTagEntity)

}