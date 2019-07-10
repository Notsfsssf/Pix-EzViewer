package com.perol.asdpl.pixivez.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "illusts")
data class DownUserEntity constructor(
        var id:Int,
        var userurl: String,
        var userid: Int,
        var part: String?
){
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}