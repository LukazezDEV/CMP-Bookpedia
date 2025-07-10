package com.plcoding.bookpedia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookMockAttributes
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreen
import com.plcoding.bookpedia.book.presentation.book_list.BookListState
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.SandYellow

@Preview
@Composable
private fun BookSearchBarPreview() {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = SandYellow,
            backgroundColor = SandYellow
        )
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            BookSearchBar(
                searchQuery = "",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

private val mockBooks = (0..BookMockAttributes.books.lastIndex).map{
    Book(
        id = "$it",
        title = BookMockAttributes.books[it],
        imageUrl = BookMockAttributes.urls[it],
        authors = BookMockAttributes.randomAuthors(),
        description = "Mock Description.",
        languages = BookMockAttributes.randomLanguages(),
        firstPublishYear = (1900..2025).random(),
        subjects = BookMockAttributes.randomSubjects(),
        averageRating = (25..50).random() / 10.0,
        numRatings = (1..1_500_000).random(),
        numPages = (20..800).random(),
        numEditions = (1..6).random(),
    )
}

@Preview
@Composable
private fun BookListScreenPreview(){
    BookListScreen(
        state = BookListState(
            searchResults = mockBooks
        ),
        onAction = {}
    )
}

@Preview
@Composable
private fun BookDetailScreenPreview(){
//    BookDetailScreen()
}