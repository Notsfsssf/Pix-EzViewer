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

package com.perol.asdpl.pixivez.responses

import com.google.gson.annotations.SerializedName

data class OneZeroResponse(

        @SerializedName("status") val status: Int,
        @SerializedName("tC") val tC: Boolean,
        @SerializedName("rD") val rD: Boolean,
        @SerializedName("rA") val rA: Boolean,
        @SerializedName("aD") val aD: Boolean,
        @SerializedName("cD") val cD: Boolean,
        @SerializedName("question") val question: List<Question>,
        @SerializedName("answer") val answer: List<Answer>
)

data class Question(

        @SerializedName("name") val name: String,
        @SerializedName("type") val type: Int
)

data class Answer(

        @SerializedName("name") val name: String,
        @SerializedName("type") val type: Int,
        @SerializedName("tTL") val tTL: Int,
        @SerializedName("data") val data: String
)