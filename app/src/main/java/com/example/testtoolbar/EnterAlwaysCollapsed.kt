@file:OptIn(ExperimentalPagerApi::class)

package com.example.testtoolbar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun EnterAlwaysCollapsed(
    parentModifier: Modifier.() -> Modifier = { this },
    topBar: @Composable () -> Unit = {},
    collapsingToolbar: @Composable ColumnScope.() -> Unit,
    stickyToolbar: @Composable ColumnScope.() -> Unit = {},
    nestedList: @Composable Dp.() -> Unit
) {
    val topBarHeight = remember { mutableStateOf(0F) }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val toolbarHeight = remember { mutableStateOf(0f) }
    val stickyToolbarHeight = remember { mutableStateOf(0f) }
    val scrollState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                // called when you scroll the content
                Log.e(
                    "NSC",
                    "onPreScroll $delta tos ${toolbarOffsetHeightPx.value}"
                )
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                scrollState.layoutInfo
                    .takeIf {
                        Log.e("NSC", "ops ${it.visibleItemsInfo.lastOrNull()?.index}")
                        it.visibleItemsInfo.lastOrNull()?.index != it.totalItemsCount - 1
                    }?.run {
                        val delta = consumed.y
                        val newOffset = toolbarOffsetHeightPx.value + delta
                        toolbarOffsetHeightPx.value = newOffset.coerceAtMost(0f)
                        Log.e(
                            "NSC",
                            "delta $delta tos ${toolbarOffsetHeightPx.value}"
                        )
                    }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .let { parentModifier(it) }
    ) {
        nestedList(
            with(LocalDensity.current) {
                abs(toolbarHeight.value).toDp() + abs(topBarHeight.value).toDp()
            },
        )
        val collapsingOffset = max(
            toolbarOffsetHeightPx.value.roundToInt(),
            toolbarHeight.value.toInt().unaryMinus()
        )
        val stickyOffset = max(
            toolbarOffsetHeightPx.value.roundToInt(),
            toolbarHeight.value.toInt().minus(stickyToolbarHeight.value.toInt()).unaryMinus()
        )
        Column(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .zIndex(1F)
                .onGloballyPositioned {
                    topBarHeight.value = it.size.height.toFloat()
                }) {
                topBar()
            }
            Column(
                modifier = Modifier
                    .onGloballyPositioned {
                        toolbarHeight.value = it.size.height.toFloat()
                    }
            ) {
                Column(
                    modifier = Modifier.offset {
                        IntOffset(x = 0, y = collapsingOffset)
                    }
                ) {
                    collapsingToolbar(this)
                }
                Column(
                    modifier = Modifier
                        .onGloballyPositioned {
                            stickyToolbarHeight.value = it.size.height.toFloat()
                        }
                        .offset {
                            IntOffset(x = 0, y = stickyOffset)
                        }) {
                    stickyToolbar()
                }
            }
        }
    }
}

@Composable
fun TestEnter() {
    var show by remember { mutableStateOf(false) }
    EnterAlwaysCollapsed(topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Green)
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            androidx.compose.material.Text(
                text = "<-",
                color = Color.Black,
                style = MaterialTheme.typography.h5
            )
            androidx.compose.material.Text(
                text = "|||",
                color = Color.Black,
                style = MaterialTheme.typography.h5
            )
        }
    }, collapsingToolbar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.Magenta),
            )
            Button(onClick = { show = !show }) {
                Text(text = "Toggle")
            }
            AnimatedVisibility(visible = show) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    repeat(30) {
                        item {
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(200.dp)
                                    .padding(4.dp)
                                    .background(if (it % 2 == 0) Color.Magenta else Color.Blue)
                            )
                        }
                    }
                }
            }
        }
    }, stickyToolbar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Red),
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(top = this),
        ) {
            itemsIndexed(listItems) { index, imageUrl ->
                ListItem(1, index, imageUrl, Modifier.fillMaxWidth())
            }
        }
    }
}