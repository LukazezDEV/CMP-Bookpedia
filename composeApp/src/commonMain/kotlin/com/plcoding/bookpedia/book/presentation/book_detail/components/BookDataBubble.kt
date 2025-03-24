package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.core.presentation.LightBlue

@Composable
fun TitledContent(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        content()
    }
}

enum class BubbleSize(val dp: Dp) {
    SMALL(50.dp), REGULAR(80.dp)
}

@Composable
fun BookDataBubble(
    modifier: Modifier = Modifier,
    size: BubbleSize = BubbleSize.REGULAR,
    content: @Composable RowScope.() -> Unit
){
    Box(
        modifier = modifier
            .widthIn(min = size.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LightBlue)
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            content()
        }
    }
}