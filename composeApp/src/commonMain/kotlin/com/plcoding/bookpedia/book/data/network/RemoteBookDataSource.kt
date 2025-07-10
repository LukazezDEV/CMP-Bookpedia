package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.response_objects.BookDetailsResponseObject
import com.plcoding.bookpedia.book.data.response_objects.BookSearchResponseObject
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<BookSearchResponseObject, DataError.Remote>

    suspend fun getBookDetails(
        bookId: String
    ): Result<BookDetailsResponseObject, DataError.Remote>
}