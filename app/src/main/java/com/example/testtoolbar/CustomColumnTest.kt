@file:OptIn(ExperimentalMaterialApi::class)

package com.example.testtoolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CustomColumnScreen() {
    var collapsibleHeight by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(0f) }
    val connection = remember {
        object : NestedScrollConnection {
            private fun consume(available: Offset): Offset {
                val newOffset = (offset + available.y).coerceIn(-collapsibleHeight, 0f)
                return Offset(0f, newOffset - offset).also { offset = newOffset }
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                if (available.y >= 0) Offset.Zero else consume(available)

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset = consume(available)
        }
    }

    CustomColumn(
        modifier = Modifier.nestedScroll(connection),
        offset = offset.roundToInt(),
        onCollapsibleHeightChanged = { collapsibleHeight = it.toFloat() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        ) {}
        Surface(
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(100) { i ->
                    @OptIn(ExperimentalMaterialApi::class)
                    ListItem {
                        Text(text = i.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomColumn(
    modifier: Modifier = Modifier,
    offset: Int = 0,
    onCollapsibleHeightChanged: (Int) -> Unit = {},
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        var collapsibleHeight = 0
        val placeables = measurables.mapIndexed { i, measurable ->
            val childConstraints = Constraints(
                maxWidth = constraints.maxWidth,
                maxHeight = if (i == measurables.lastIndex) {
                    constraints.maxHeight
                } else {
                    constraints.maxHeight - collapsibleHeight
                }
            )
            measurable.measure(childConstraints).also {
                if (i < measurables.lastIndex) collapsibleHeight += it.height
            }
        }
        onCollapsibleHeightChanged(collapsibleHeight)
        collapsibleHeight = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { i, placeable ->
                if (i == placeables.lastIndex) {
                    placeable.place(0, collapsibleHeight + offset)
                } else {
                    placeable.place(0, collapsibleHeight)
                    collapsibleHeight += placeable.height
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val scrollState = rememberLazyListState()
    var scrolledY = 0f
    var previousOffset = 0
    // parallax effect by offset
    Box {
        Box(
            modifier = Modifier
                .background(Color.Magenta)
                .graphicsLayer {
                    /*val imageOffset = (-scrollState.firstVisibleItemScrollOffset * 0.18f)
                    translationY = imageOffset*/
                    scrolledY += scrollState.firstVisibleItemScrollOffset - previousOffset
                    translationY = scrolledY * 0.5f
                    previousOffset = scrollState.firstVisibleItemScrollOffset
                }
                .height(240.dp)
                .fillMaxWidth()
        )
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .padding(top = 200.dp)
                .background(
                    MaterialTheme.colors.surface,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            items(100) { i ->
                ListItem {
                    Text(text = i.toString())
                }
            }
        }
    }
}