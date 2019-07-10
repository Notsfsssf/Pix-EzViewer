package com.perol.asdpl.pixivez.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "illusts")
data class Illust constructor(
                  var url:String,
                  var illustid: Int,
                  var userid: Int,
                  var part: String?
){
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}
