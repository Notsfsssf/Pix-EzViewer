/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

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
class IllustBeanEntity(
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
