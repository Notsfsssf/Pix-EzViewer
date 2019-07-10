package com.perol.asdpl.pixivez.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perol.asdpl.pixivez.sql.entity.Illust

@Dao
abstract class IllustDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(query: Illust)
    @Query("SELECT * FROM illusts WHERE userid=:userid")
    abstract fun findIllustsByUserId(userid:Int):MutableList<Illust>
}