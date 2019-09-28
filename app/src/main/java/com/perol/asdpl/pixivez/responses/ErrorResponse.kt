package com.perol.asdpl.pixivez.responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
        val errors: Errors,
        val has_error: Boolean
)

data class Errors(
        @SerializedName("system")
        val system: SystemX
)

data class SystemX(
        val code: Int,
        val message: String
)