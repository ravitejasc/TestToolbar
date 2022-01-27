@file:OptIn(ExperimentalPagerApi::class)

package com.example.testtoolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun EnterAlwaysCollapsed(
    parentLayoutDecorator: Modifier.() -> Modifier = { this },
    toolbar: @Composable ColumnScope.() -> Unit,
    keepToolbar: @Composable ColumnScope.() -> Unit = {},
    nestedList: @Composable Dp.(EnterAlwaysCollapsedArgs) -> Unit
) {
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val toolbarHeight = remember { mutableStateOf(0f) }
    val keepToolbarHeight = remember { mutableStateOf(0f) }
    val scrollState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                scrollState.layoutInfo
                    .takeIf {
                        it.visibleItemsInfo.lastOrNull()?.index != it.totalItemsCount - 1
                    }
                    ?.run {
                        val delta = consumed.y
                        val newOffset = toolbarOffsetHeightPx.value + delta
                        toolbarOffsetHeightPx.value = newOffset.coerceAtMost(0f)
                    }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .let { parentLayoutDecorator(it) }
    ) {
        nestedList(
            with(LocalDensity.current) { abs(toolbarHeight.value).toDp() },
            EnterAlwaysCollapsedArgs(scrollState)
        )
        val offset = max(
            toolbarOffsetHeightPx.value.roundToInt(),
            toolbarHeight.value.toInt().unaryMinus()
        )
        val keepOffset = max(
            toolbarOffsetHeightPx.value.roundToInt(),
            toolbarHeight.value.toInt().minus(keepToolbarHeight.value.toInt()).unaryMinus()
        )
        Column(
            modifier = Modifier
                .onGloballyPositioned {
                    toolbarHeight.value = it.size.height.toFloat()
                }
        ) {
            Column(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = offset) }
            ) {
                toolbar(this)
            }
            Column(
                modifier = Modifier
                    .onGloballyPositioned {
                        keepToolbarHeight.value = it.size.height.toFloat()
                    }
                    .offset {
                        IntOffset(x = 0, y = keepOffset)
                    }) {
                keepToolbar(this)
            }
        }
    }
}

data class EnterAlwaysCollapsedArgs(
    val scrollState: LazyListState
) {
    fun listProducer(
        lazyListScope: LazyListScope,
        nestedScrollList: (LazyListScope) -> Unit
    ) {
        lazyListScope.run {
            nestedScrollList(this)
            item { Spacer(modifier = Modifier.size(1.dp)) }
        }
    }
}

@Composable
fun TestEnter() {
    EnterAlwaysCollapsed(toolbar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.Magenta),
        )
    }, keepToolbar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Red),
        )
    }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(listItems) { index, imageUrl ->
                ListItem(1, index, imageUrl, Modifier.fillMaxWidth())
            }
        }
    }
}