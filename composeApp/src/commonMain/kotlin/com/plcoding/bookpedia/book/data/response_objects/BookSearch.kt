package com.plcoding.bookpedia.book.data.response_objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseObject(
    @SerialName("docs") val results: List<BookResponseObject>
)

@Serializable
data class BookResponseObject(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("author_name") val authorNames: List<String>? = null,
    @SerialName("author_key") val authorKeys: List<String>? = null,
    @SerialName("ratings_average") val averageRating: Double? = null,
    @SerialName("ratings_count") val numRatings: Int? = null,
    @SerialName("number_of_pages_median") val numPages: Int? = null,
    @SerialName("edition_count") val numEditions: Int? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("cover_i") val altCoverKey: Int? = null,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("genre") val genres: List<String>? = null
 )
