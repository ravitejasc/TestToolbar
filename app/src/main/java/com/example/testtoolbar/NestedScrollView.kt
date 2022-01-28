@file:OptIn(ExperimentalFoundationApi::class)

package com.example.testtoolbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Define a [VerticalNestedScrollView].
 *
 * @param state the state object to be used to observe the [VerticalNestedScrollView] state.
 * @param modifier the modifier to apply to this layout.
 * @param content a block which describes the header.
 * @param content a block which describes the content.
 */
@Composable
fun VerticalNestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    NestedScrollView(
        modifier = modifier,
        state = state,
        orientation = Orientation.Vertical,
        header = header,
        content = content,
    )
}

/**
 * Define a [HorizontalNestedScrollView].
 *
 * @param state the state object to be used to observe the [HorizontalNestedScrollView] state.
 * @param modifier the modifier to apply to this layout.
 * @param content a block which describes the header.
 * @param content a block which describes the content.
 */
@Composable
fun HorizontalNestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    NestedScrollView(
        modifier = modifier,
        state = state,
        orientation = Orientation.Horizontal,
        header = header,
        content = content,
    )
}

@Composable
private fun NestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    orientation: Orientation,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier
            .scrollable(
                orientation = orientation,
                state = rememberScrollableState {
                    state.drag(it)
                }
            )
            .nestedScroll(state.nestedScrollConnectionHolder),
        content = {
            Box {
                header.invoke()
            }
            Box {
                content.invoke()
            }
        },
    ) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            when (orientation) {
                Orientation.Vertical -> {
                    val headerPlaceable =
                        measurables[0].measure(constraints.copy(maxHeight = Constraints.Infinity))
                    headerPlaceable.place(0, state.offset.roundToInt())
                    state.updateBounds(-(headerPlaceable.height.toFloat()))
                    val contentPlaceable =
                        measurables[1].measure(constraints.copy(maxHeight = constraints.maxHeight))
                    contentPlaceable.place(
                        0,
                        state.offset.roundToInt() + headerPlaceable.height
                    )
                }
                Orientation.Horizontal -> {
                    val headerPlaceable =
                        measurables[0].measure(constraints.copy(maxWidth = Constraints.Infinity))
                    headerPlaceable.place(state.offset.roundToInt(), 0)
                    state.updateBounds(-(headerPlaceable.width.toFloat()))
                    val contentPlaceable =
                        measurables[1].measure(constraints.copy(maxWidth = constraints.maxWidth))
                    contentPlaceable.place(
                        state.offset.roundToInt() + headerPlaceable.width,
                        0,
                    )
                }
            }
        }
    }
}

@Composable
fun NestedLists() {
    LazyColumn(
        Modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header for first inner list
        stickyHeader {
            Text(text = "List of numbers:", style = MaterialTheme.typography.h5)
        }
        // First, scrollable, inner list
        item {
            // Note the important height modifier.
            LazyColumn(Modifier.fillMaxSize()) {
                val numbersList = (0..400 step 4).toList()
                itemsIndexed(numbersList) { index, multipleOf4 ->
                    Text(
                        text = "$multipleOf4",
                        style = TextStyle(fontSize = 22.sp, color = Color.Blue)
                    )
                }
            }
        }

        // Header for second inner list
        item {
            Text(text = "List of letters:", style = MaterialTheme.typography.h5)
        }
        // Second, scrollable, inner list
        item {
            // Note the important height modifier.
            LazyColumn(Modifier.height(200.dp)) {
                val lettersList = ('a'..'z').toList()
                itemsIndexed(lettersList) { index, letter ->
                    Text(text = "$letter", style = TextStyle(color = Color.Blue, fontSize = 22.sp))
                }
            }
        }
    }
}