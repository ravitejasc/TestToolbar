@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)

package com.example.testtoolbar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun EnterAlwaysCollapsed(
    parentModifier: Modifier.() -> Modifier = { this },
    titleToolbar: @Composable () -> Unit = {},
    collapsingToolbar: @Composable ColumnScope.() -> Unit,
    stickyToolbar: @Composable ColumnScope.() -> Unit = {},
    nestedList: @Composable Dp.(LazyListState) -> Unit
) {
    var titleToolbarHeight by remember { mutableStateOf(0F) }
    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }
    var toolbarHeight by remember { mutableStateOf(0f) }
    var stickyToolbarHeight by remember { mutableStateOf(0f) }
    val scrollState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                Log.e(
                    "NSC",
                    "onPreScroll $delta tos $toolbarOffsetHeightPx"
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
                        val newOffset = toolbarOffsetHeightPx + delta
                        toolbarOffsetHeightPx = newOffset.coerceAtMost(0f)
                        Log.e("NSC", "delta $delta tos $toolbarOffsetHeightPx")
                    }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize().background(Color.White)
            .nestedScroll(nestedScrollConnection)
            .let { parentModifier(it) }
    ) {
        nestedList(
            with(LocalDensity.current) {
                abs(toolbarHeight).toDp() + abs(titleToolbarHeight).toDp()
            },
            scrollState
        )
        val collapsingOffset = max(
            toolbarOffsetHeightPx.roundToInt(),
            toolbarHeight.toInt().unaryMinus()
        )
        val stickyOffset = max(
            toolbarOffsetHeightPx.roundToInt(),
            toolbarHeight.toInt().minus(stickyToolbarHeight.toInt()).unaryMinus()
        )
        Column(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .zIndex(1F)
                .onGloballyPositioned {
                    titleToolbarHeight = it.size.height.toFloat()
                    Log.e("OGP", "titleToolbarh $titleToolbarHeight")
                }) {
                titleToolbar()
            }
            Column(
                modifier = Modifier
                    .onGloballyPositioned {
                        toolbarHeight = it.size.height.toFloat()
                        Log.e("OGP", "tbh $toolbarHeight")
                    }
            ) {
                Column(
                    modifier = Modifier
                        .offset {
                            Log.e(
                                "OFFS",
                                "cl stickyOffset $stickyOffset collapsingOffset $collapsingOffset " +
                                        "titleToolbarHeight $titleToolbarHeight toolbarHeight " +
                                        "$toolbarHeight diff ${abs(collapsingOffset.unaryPlus() - stickyOffset.unaryPlus())}"
                            )
                            IntOffset(x = 0, y = collapsingOffset)
                        }
                ) {
                    collapsingToolbar(this)
                }
                Column(
                    modifier = Modifier
                        .onGloballyPositioned {
                            stickyToolbarHeight = it.size.height.toFloat()
                            Log.e("OGP", "stbh $stickyToolbarHeight")
                        }
                        .offset {
                            Log.e(
                                "OFFS",
                                "st stickyOffset $stickyOffset collapsingOffset $collapsingOffset " +
                                        "titleToolbarHeight $titleToolbarHeight toolbarHeight " +
                                        "$toolbarHeight diff ${abs(collapsingOffset.unaryPlus() - stickyOffset.unaryPlus())}"
                            )
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
    LogCompositions(tag = "TestEnter")
    var show by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState()
    EnterAlwaysCollapsed(titleToolbar = {
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
        TabLayout(pagerState = pagerState)
    }) {
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
            itemSpacing = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.fillMaxSize(),
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(top = this@EnterAlwaysCollapsed),
            ) {
                itemsIndexed(listItems) { _, imageUrl ->
                    GridItem(imageUrl)
                }
            }
        }
    }
}

@Composable
fun GridItem(imageUrl: String) {
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp)),
    )
}