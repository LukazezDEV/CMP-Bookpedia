package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.response_objects.BookDetailsResponseObject
import com.plcoding.bookpedia.book.data.response_objects.BookSearchResponseObject
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
): RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<BookSearchResponseObject, DataError.Remote>
        = safeCall<BookSearchResponseObject> {
            httpClient.get(
                urlString = "$BASE_URL/search.json"
            ) {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter("fields", "key,title,first_publish_year,author_name,author_key,ratings_average,ratings_count,number_of_pages_median,edition_count,cover_edition_key,cover_i,language")
            }
        }

    override suspend fun getBookDetails(
        bookId: String
    ): Result<BookDetailsResponseObject, DataError.Remote>
        = safeCall<BookDetailsResponseObject> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookId.json"
            )
    }
}