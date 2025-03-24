package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.response_objects.BookResponseObject
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookMockAttributes

fun BookResponseObject.toBook(): Book
    = Book(
    id = id.substringAfterLast("/"),
    title = title,
    imageUrl = if (coverKey != null) "https://covers.openlibrary.org/b/olid/$coverKey-L.jpg"
               else "https://covers.openlibrary.org/b/id/$altCoverKey-L.jpg",
    authors = authorNames ?: emptyList(),
    description = null,
    languages = languages ?: emptyList(),
    firstPublishYear = firstPublishYear,
    genres = genres ?: BookMockAttributes.randomGenres(), //TODO remove sample data
    averageRating = averageRating,
    numRatings = numRatings,
    numPages = numPages,
    numEditions = numEditions,
)
