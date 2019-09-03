package com.perol.asdpl.pixivez.responses

import com.google.gson.annotations.SerializedName

/**
 * Created by asdpl on 2018/2/18.
 */

data class PixivResponse (

        @SerializedName("tags") val tags : List<Tags>
)
data class Tags (

        @SerializedName("name") val name : String,
        @SerializedName("translated_name") val translated_name : String
)