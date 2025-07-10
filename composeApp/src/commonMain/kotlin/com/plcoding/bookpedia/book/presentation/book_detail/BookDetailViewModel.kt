package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.DomainBookRepository
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: DomainBookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            fetchBookDetails(bookId)
            observeFavoriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookDetailAction){
        when(action){
            is BookDetailAction.OnSelectedBook -> _state.update { it.copy(book = action.book) }
            BookDetailAction.OnFavoriteClick ->
                viewModelScope.launch{
                    if(state.value.isFavorite)
                        bookRepository.deleteFromFavorites(bookId)
                    else
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                }
            BookDetailAction.OnBackClick -> Unit //handled at UI level
        }
    }

    private fun fetchBookDetails(bookId: String){
        viewModelScope.launch {
            bookRepository
                .getBookDetails(bookId)
                .onSuccess { bookDetails ->
                    _state.update {
                        it.copy(
                            book = it.book?.copy(description = bookDetails?.description, subjects = bookDetails?.subjects),
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun observeFavoriteStatus(){
        bookRepository
            .isBookFavorite(bookId)
            .onEach { isFavorite ->
                _state.update { it.copy(
                    isFavorite = isFavorite
                ) }
            }
            .launchIn(viewModelScope)
    }
}