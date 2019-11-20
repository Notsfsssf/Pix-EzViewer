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

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Illust(
        val caption: String,
        val create_date: String,
        val height: Int,
        val id: Long,
        val image_urls: ImageUrls,
        var is_bookmarked: Boolean,
        val is_muted: Boolean,
        val meta_pages: List<MetaPage>,
        val meta_single_page: MetaSinglePage,
        val page_count: Int,
        val restrict: Int,
        val sanity_level: Int,
        val tags: List<Tag>,
        val title: String,
        val tools: List<String>,
        val total_bookmarks: Int,
        val total_view: Int,
        val type: String,
        val user: User,
        val visible: Boolean,
        val width: Int,
        val x_restrict: Int
) : Parcelable

@Parcelize
data class MetaPage(
        val image_urls: ImageUrlsX
) : Parcelable

@Parcelize
data class ImageUrlsX(
        val large: String,
        val medium: String,
        val original: String,
        val square_medium: String
) : Parcelable

@Parcelize
data class ImageUrls(
        val large: String,
        val medium: String,
        val square_medium: String
) : Parcelable

@Parcelize
data class MetaSinglePage(
    val original_image_url: String?
) : Parcelable

@Parcelize
data class Tag(
    val name: String,
    val translated_name: String? = null
) : Parcelable

@Parcelize
data class User(
        val account: String,
        val id: Long,
        var is_followed: Boolean,
        val name: String,
        val profile_image_urls: ProfileImageUrls
) : Parcelable

@Parcelize
data class ProfileImageUrls(
        val medium: String
) : Parcelable