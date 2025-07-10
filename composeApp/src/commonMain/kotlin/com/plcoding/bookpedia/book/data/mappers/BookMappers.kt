package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.response_objects.BookResponseObject
import com.plcoding.bookpedia.book.domain.Book

fun BookResponseObject.toBook(): Book =
    Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if (coverKey != null) "https://covers.openlibrary.org/b/olid/$coverKey-L.jpg"
                   else "https://covers.openlibrary.org/b/id/$altCoverKey-L.jpg",
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear,
        subjects = subjects,
        averageRating = averageRating,
        numRatings = numRatings,
        numPages = numPages,
        numEditions = numEditions,
    )

fun Book.toBookEntity(): BookEntity =
    BookEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authors,
        description = description,
        languages = languages,
        firstPublishYear = firstPublishYear,
        subjects = subjects,
        averageRating = averageRating,
        numRatings = numRatings,
        numPages = numPages,
        numEditions = numEditions,
    )

fun BookEntity.toBook(): Book =
    Book(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authors,
        description = description,
        languages = languages,
        firstPublishYear = firstPublishYear,
        subjects = subjects,
        averageRating = averageRating,
        numRatings = numRatings,
        numPages = numPages,
        numEditions = numEditions
    )