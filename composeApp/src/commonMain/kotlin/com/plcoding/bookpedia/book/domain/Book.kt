package com.plcoding.bookpedia.book.domain

data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String?,
    val languages: List<String>,
    val firstPublishYear: Int?,
    val genres: List<String>,
    val averageRating: Double?,
    val numRatings: Int?,
    val numPages: Int?,
    val numEditions: Int?
)

object Genres{
    fun list() = listOf(
    "Fantasy",
    "Science Fiction",
    "Mystery",
    "Thriller",
    "Romance",
    "Historical Fiction",
    "Horror",
    "Adventure",
    "Non-Fiction",
    "Biography",
    "Self-Help",
    "Poetry",
    "Young Adult",
    "Graphic Novel",
    "Dystopian",
    "Contemporary",
    "Children's Literature",
    "Memoir",
    "Cookbook",
    "True Crime"
    )
}