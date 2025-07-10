package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.book_description
import cmp_bookpedia.composeapp.generated.resources.book_description_missing
import cmp_bookpedia.composeapp.generated.resources.book_subjects
import cmp_bookpedia.composeapp.generated.resources.book_languages
import cmp_bookpedia.composeapp.generated.resources.book_pages
import cmp_bookpedia.composeapp.generated.resources.book_rating
import cmp_bookpedia.composeapp.generated.resources.book_year
import com.plcoding.bookpedia.book.presentation.book_detail.components.BlurredBookCover
import com.plcoding.bookpedia.book.presentation.book_detail.components.BookDataBubble
import com.plcoding.bookpedia.book.presentation.book_detail.components.BubbleSize
import com.plcoding.bookpedia.book.presentation.book_detail.components.TitledContent
import com.plcoding.bookpedia.core.presentation.SandYellow
import com.plcoding.bookpedia.core.presentation.getCommaSeparated
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is BookDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit
){
    BlurredBookCover(
        imageUrl = state.book?.imageUrl,
        isFavorite = state.isFavorite,
        onFavoriteClick = { onAction(BookDetailAction.OnFavoriteClick) },
        onBackClick = { onAction(BookDetailAction.OnBackClick) },
        modifier = Modifier.fillMaxSize(),
    ){
        val book = state.book
        if(book != null)
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = book.title,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall
                )
                book.authors
                    .filter { it.isNotBlank() }
                    .takeIf { it.isEmpty().not() }
                    ?.joinToString(", ")
                    ?.let { authors ->
                        Text(
                            text = authors,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                        )
                    }
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    book.averageRating?.let { rating ->
                        TitledContent(
                            title = stringResource(Res.string.book_rating),
                        ){
                             BookDataBubble {
                                Text(
                                    text = "${round(rating * 10) / 10.0}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                                Text(
                                    text = "(${book.numRatings?.getCommaSeparated()})",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    book.firstPublishYear?.let { publishYear ->
                        TitledContent(
                            title = stringResource(Res.string.book_year),
                        ){
                            BookDataBubble {
                                Text(
                                    text = "${book.firstPublishYear}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    book.numPages?.let { pageCount ->
                        TitledContent(
                            title = stringResource(Res.string.book_pages),
                        ){
                            BookDataBubble {
                                Text(
                                    text = "$pageCount",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                book.languages
                    .filter { it.isNotBlank() }
                    .takeIf { it.isEmpty().not() }
                    ?.let { languages ->
                        TitledContent(
                            title = stringResource(Res.string.book_languages),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ){
                            FlowRow(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.wrapContentSize(Alignment.Center)
                            ){
                                languages.forEach {
                                    BookDataBubble(
                                        size = BubbleSize.SMALL,
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Text(
                                            text = it.uppercase(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                            }
                        }
                    }

                book.subjects?.run {
                    filter { it.isNotBlank() }
                    .takeIf { it.isEmpty().not() }
                    ?.take(10)
                    ?.let { subjects ->
                        TitledContent(
                            title = stringResource(Res.string.book_subjects),
                            modifier = Modifier.padding(vertical = 48.dp)
                        ){
                            FlowRow(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.wrapContentSize(Alignment.Center)
                            ){
                                subjects.forEach {
                                    BookDataBubble(
                                        size = BubbleSize.REGULAR,
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Text(
                                            text = it.uppercase(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                            }
                        }
                    }
                }


                Text(
                    text = stringResource(Res.string.book_description),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp,)
                )
                    if(state.isLoading)
                        CircularProgressIndicator()
                    else
                        Text(
                            text = if(!book.description.isNullOrBlank()) book.description
                                   else stringResource(Res.string.book_description_missing),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Left,
                            color = if(!book.description.isNullOrBlank()) Color.Black
                                    else Color.Black.copy(alpha = 0.4f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
            }
    }
}