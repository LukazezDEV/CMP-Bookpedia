package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class BookListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null

    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if(cachedBooks.isEmpty()){
                observeSearchQuery()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookListAction){
        when(action) {
            is BookListAction.OnBookClick -> {}
            is BookListAction.OnSearchQueryChange ->
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            is BookListAction.OnTabSelected ->
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
        }
    }

    private fun observeSearchQuery(){
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500)
            .onEach { searchQuery ->
                when {
                    searchQuery.isBlank() -> _state.update {
                        it.copy(errorMessage = null, searchResults = cachedBooks)
                    }
                    searchQuery.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(searchQuery)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update{ it.copy(
            isLoading = true
        ) }
        bookRepository
            .searchBooks(query)
            .onSuccess { books ->
                _state.update{ it.copy(
                    searchResults = books,
                    isLoading = false,
                    errorMessage = null
                ) }
            }
            .onError { error ->
                _state.update { it.copy(
                    searchResults = emptyList(),
                    isLoading = false,
                    errorMessage = error.toUiText()
                ) }
            }
    }
}