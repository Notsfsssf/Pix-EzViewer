package com.perol.asdpl.pixivez.responses

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
        val series: Any,
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
)
data class MetaPage(
    val image_urls: ImageUrlsX
)

data class ImageUrlsX(
    val large: String,
    val medium: String,
    val original: String,
    val square_medium: String
)
data class ImageUrls(
        val large: String,
        val medium: String,
        val square_medium: String
)

data class MetaSinglePage(
        val original_image_url: String
)

data class Tag(
        val name: String,
        val translated_name: String
)

data class User(
        val account: String,
        val id: Long,
        var is_followed: Boolean,
        val name: String,
        val profile_image_urls: ProfileImageUrls
)

data class ProfileImageUrls(
        val medium: String
)