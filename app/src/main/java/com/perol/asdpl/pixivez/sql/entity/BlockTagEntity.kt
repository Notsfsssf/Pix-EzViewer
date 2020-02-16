package com.perol.asdpl.pixivez.sql.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blockTag")
class BlockTagEntity constructor(@ColumnInfo(name = "name") var name: String, @ColumnInfo(name = "translateName") var translateName: String) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}