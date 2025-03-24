package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookDetailViewModel: ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: BookDetailAction){
        when(action){
            is BookDetailAction.OnSelectBookChange -> _state.update { it.copy(book = action.book) }
            BookDetailAction.OnFavoriteClick -> {}
            BookDetailAction.OnBackClick -> Unit //handled at UI level
        }
    }
}