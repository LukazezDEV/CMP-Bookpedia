package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavoriteBookDao
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookDetails
import com.plcoding.bookpedia.book.domain.DomainBookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
): DomainBookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> =
        remoteBookDataSource
            .searchBooks(query)
            .map { searchResponse ->
                searchResponse.results.map { it.toBook() }
            }

    override suspend fun getBookDetails(bookId: String): Result<BookDetails?, DataError>{
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if(localResult == null)
            remoteBookDataSource
                .getBookDetails(bookId)
                .map { BookDetails(
                    description = it.description,
                    subjects = it.subjects
                ) }
        else
            Result.Success(BookDetails(localResult.description, localResult.subjects))
    }


    override fun getFavoriteBooks(): Flow<List<Book>> =
        favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBook() }
            }

    override fun isBookFavorite(bookId: String): Flow<Boolean> =
        favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == bookId }
            }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> =
        try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: Throwable) {
            Result.Error(DataError.Local.DISK_FULL)
        }

    override suspend fun deleteFromFavorites(bookId: String) {
        favoriteBookDao
            .deleteFavoriteBook(bookId)
    }
}