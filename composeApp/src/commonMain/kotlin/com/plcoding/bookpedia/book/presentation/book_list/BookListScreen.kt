package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.missing_favorite_books
import cmp_bookpedia.composeapp.generated.resources.search_no_results
import cmp_bookpedia.composeapp.generated.resources.tab_favorites
import cmp_bookpedia.composeapp.generated.resources.tab_search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

//Slightly different from BookListScreen(), so that BookListScreen is testable without ViewModel
@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state = state,
        onAction = { action ->
            when(action){
                //specified outside viewmodel because onBookClick is a UI navigation action
                is BookListAction.OnBookClick -> onBookClick(action.book)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit
){
    val keyBoardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState { 2 }
    val (searchResultsListState, favoriteBooksListState) = rememberLazyListState() to rememberLazyListState()
    val (searchResultListScrollPosition, favoriteBooksListScrollPosition) = rememberSaveable { mutableStateOf(0) } to rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(state.searchResults, state.favoriteBooks) {
        searchResultsListState.animateScrollToItem(searchResultListScrollPosition.value)
        favoriteBooksListState.animateScrollToItem(favoriteBooksListScrollPosition.value)
    }
    LaunchedEffect(searchResultsListState.firstVisibleItemIndex) { searchResultListScrollPosition.value = searchResultsListState.firstVisibleItemIndex }
    LaunchedEffect(favoriteBooksListState.firstVisibleItemIndex) { favoriteBooksListScrollPosition.value = favoriteBooksListState.firstVisibleItemIndex }
    LaunchedEffect(state.selectedTabIndex){ pagerState.animateScrollToPage(state.selectedTabIndex) }
    LaunchedEffect(pagerState.currentPage){ onAction(BookListAction.OnTabSelected(pagerState.currentPage)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { onAction(BookListAction.OnSearchQueryChange(it)) },
            //search will be automatic with key inputs, no need to implement here in presentation layer.
            //but makes sense to define here to hide keyboard when search icon is pressed.
            onImeSearch = { keyBoardController?.hide() },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp,)
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            color = SandYellow,
                            modifier = Modifier.tabIndicatorOffset(it[state.selectedTabIndex])
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth()
                ){
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { onAction(BookListAction.OnTabSelected(0)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ){
                        Text(
                            text = stringResource(Res.string.tab_search_results),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = { onAction(BookListAction.OnTabSelected(1)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f)
                    ){
                        Text(
                            text = stringResource(Res.string.tab_favorites),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ){ page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when{
                            state.isLoading -> CircularProgressIndicator()
                            state.errorMessage != null ->
                                Text(
                                    text = state.errorMessage.asString(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            else -> when(page){
                                0 ->
                                    if(state.searchResults.isEmpty())
                                        Text(
                                            text = stringResource(Res.string.search_no_results),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    else
                                        BookList(
                                            books = state.searchResults,
                                            onBookClick = { onAction(BookListAction.OnBookClick(it)) },
                                            modifier = Modifier.fillMaxSize(),
                                            scrollState = searchResultsListState
                                        )
                                1 ->
                                    if(state.favoriteBooks.isEmpty())
                                        Text(
                                            text = stringResource(Res.string.missing_favorite_books),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                        )
                                    else
                                        BookList(
                                            books = state.favoriteBooks,
                                            onBookClick = { onAction(BookListAction.OnBookClick(it)) },
                                            modifier = Modifier.fillMaxSize(),
                                            scrollState = favoriteBooksListState
                                        )
                            }
                        }
                    }
                }
            }
        }
    }
}