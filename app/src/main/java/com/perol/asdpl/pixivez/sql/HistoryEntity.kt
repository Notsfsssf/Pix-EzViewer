package com.perol.asdpl.pixivez.sql

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class SearchHistoryEntity constructor(@ColumnInfo(name = "word") var word: String) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}

@Entity(tableName = "downillusts")
class DownIllustsEntity constructor(
        @ColumnInfo(name = "userid")
        var userid: Long,
        @ColumnInfo(name = "pid")
        var pid: String,
        @ColumnInfo(name = "imageurl")
        var imageurl: String,
        @ColumnInfo(name = "imageurl")
        var imageurls: List<String>
) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}

@Entity(tableName = "downuser")
class DownUserEntity constructor(
        @ColumnInfo(name = "userid")
        var userid: Long,
        @ColumnInfo(name = "username")
        var username: String,
        @ColumnInfo(name = "userpic")
        var userpic: String,
        @ColumnInfo(name = "totalbookmark")
        var totalbookmark: Long,
        @ColumnInfo(name = "totalillust")
        var totalillust: Long,
        @ColumnInfo(name = "username")
        var nexturl: String

) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}

@Entity(tableName = "illusthistory")
class IllustBeanEntity (
        @PrimaryKey(autoGenerate = true)
        var Id: Long? = 0,
        @ColumnInfo(name = "imageurl")
        var imageurl: String,
        @ColumnInfo(name = "illustid")
        var illustid: Long

)
@Entity(tableName = "user")
class UserEntity constructor(@ColumnInfo(name = "userimage")
                             var userimage: String,
                             @ColumnInfo(name = "userid")
                             var userid: Long,
                             @ColumnInfo(name = "username")
                             var username: String,
                             @ColumnInfo(name = "useremail")
                             var useremail: String,
                             @ColumnInfo(name = "ispro")
                             var ispro: Boolean,
                             @ColumnInfo
                             var Device_token: String,
                             @ColumnInfo
                             var Refresh_token: String,
                             @ColumnInfo
                             var Authorization: String

) {
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0
}
