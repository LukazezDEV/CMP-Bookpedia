package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = mockBooks,
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)

val mockBooks = (1..100).map{
    Book(
        id = "$it",
        title = "Book $it",
        imageUrl = "https://test.com",
        authors = listOf("Luka Djin"),
        description = "Mock Description.",
        languages = listOf("English", "Espanol"),
        firstPublishYear = 1994,
        genres = listOf("Non-fiction", "Science"),
        averageRating = 4.43335,
        numRatings = it,
        numPages = 100,
        numEditions = 3,
    )
}