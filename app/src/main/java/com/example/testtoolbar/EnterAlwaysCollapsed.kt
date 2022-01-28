@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class,
    ExperimentalCoilApi::class
)

package com.example.testtoolbar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun EnterAlwaysCollapsed(
    parentModifier: Modifier.() -> Modifier = { this },
    titleToolbar: @Composable () -> Unit = {},
    collapsingToolbar: @Composable ColumnScope.() -> Unit,
    stickyToolbar: @Composable ColumnScope.() -> Unit = {},
    nestedList: @Composable Dp.(LazyListState) -> Unit,
) {
    var progress by remember { mutableStateOf(0F) }
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
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .let { parentModifier(it) }
    ) {
        nestedList(
            with(LocalDensity.current) { abs(toolbarHeight).toDp() },
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
                        progress =
                            abs(1 - (abs(toolbarHeight - collapsingOffset.unaryPlus()) / toolbarHeight))
                        Log.e(
                            "OFFS",
                            "cl stickyOffset $stickyOffset collapsingOffset $collapsingOffset " +
                                    "titleToolbarHeight $titleToolbarHeight toolbarHeight " +
                                    "$toolbarHeight progress $progress diff ${abs(collapsingOffset.unaryPlus() - stickyOffset.unaryPlus())}"
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
        Row(modifier = Modifier
            .fillMaxWidth()
            .zIndex(1F)
            .onGloballyPositioned {
                titleToolbarHeight = it.size.height.toFloat()
                Log.e("OGP", "titleToolbarh $titleToolbarHeight")
            }) {
            titleToolbar()
        }
    }
}

@Composable
fun StatusBarRender(
    statusBarColor: Color? = null,
    setStatusBarColor: Boolean = true,
    darkIcons: Boolean? = null,
) {
    val controller: SystemUiController = rememberSystemUiController()
    if (setStatusBarColor) {
        val color = statusBarColor ?: Color.Transparent
        val enableDark = darkIcons ?: (color.luminance() > 0.5f)
        SideEffect {
            controller.setStatusBarColor(color = color, darkIcons = enableDark)
            controller.setSystemBarsColor(color = color, darkIcons = enableDark)
        }
    }
}


@Composable
fun TestEnter() {
    var show by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState()
    LogCompositions(tag = "TestEnter")
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(2000)
            refreshing = false
        }
    }
    SwipeRefresh(
        modifier = Modifier.statusBarsPadding(),
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        EnterAlwaysCollapsed(parentModifier = { background(Color.White) }, titleToolbar = {
            BackInsetTopBar(
                modifier = Modifier.border(1.dp, Color.Yellow),
                title = "Raviteja",
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.AddCircle,
                            tint = Color.White,
                            contentDescription = "Back"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.Menu,
                            tint = Color.White,
                            contentDescription = "Back"
                        )
                    }
                })
        }, collapsingToolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = rememberImagePainter("https://res.cloudinary.com/ddocluzbb/image/upload/v1616689315/Top/wk_1_zyejws.webp"),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 150.dp, start = 18.dp, bottom = 16.dp)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(topStart = 40.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val profilePainter = rememberImagePainter(
                                "https://res.cloudinary.com/ddocluzbb/image/upload/v1616694749/Trainers/t19_eeij5y.webp",
                                builder = {
                                    transformations(CircleCropTransformation())
                                })
                            val profileImageState = profilePainter.state
                            Image(
                                painter = profilePainter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(1.dp)
                                    .clip(CircleShape)
                                    .placeholder(
                                        shape = RoundedCornerShape(0.dp),
                                        visible = profileImageState is ImagePainter.State.Loading,
                                        highlight = PlaceholderHighlight.fade(Color.Gray)
                                    ),
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp, start = 16.dp),
                            ) {
                                Text(
                                    text = "@raviteja",
                                    color = Color.Black,
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Followers \n 100", color = Color.Black)
                                    Text(text = "Following \n 20", color = Color.Black)
                                    Text(text = "Posts \n 203", color = Color.Black)
                                }
                            }
                        }
                    }
                    Text(
                        text = "Raviteja Gameboy",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "I love sharechat",
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(10) {
                            item {
                                OutlinedButton(onClick = {}, shape = RoundedCornerShape(4.dp)) {
                                    Text(text = "Badge $it", color = Color.Black)
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var buttonHeight by remember { mutableStateOf(0) }
                        Button(
                            onClick = { show = !show }, shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(buttonHeight.toDp())
                                .weight(1F)
                        ) {
                            Text(text = "Follow", color = Color.White)
                        }
                        IconButton(
                            onClick = { show = !show },
                            modifier = Modifier
                                .fillMaxWidth(0.1f)
                                .onGloballyPositioned {
                                    buttonHeight = it.size.height
                                }
                                .border(1.dp, Color.Blue)
                        ) {
                            val rotate by animateFloatAsState(if (show) 0F else 180F)
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(rotate)
                            )
                        }
                    }
                    AnimatedVisibility(visible = show) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
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
            }
        }, stickyToolbar = {
            TabLayout(
                pagerState = pagerState,
                modifier = Modifier//.statusBarsPadding()
            )
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BackInsetTopBar(
    modifier: Modifier = Modifier,
    title: String,
    elevation: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    InsetAwareTopAppBar(
        elevation = elevation,
        title = {
            Text(
                text = title, color = Color.White, style = MaterialTheme.typography.h6,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = backgroundColor,
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        actions = {
            actions()
        }
    )
}

@Composable
fun InsetAwareTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp
) {
    Surface(
        color = backgroundColor,
        modifier = modifier,
        shadowElevation = elevation
    ) {
        TopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            backgroundColor = Color.Transparent,
            contentColor = contentColor,
            elevation = 0.dp,
            modifier = Modifier
        )
    }
}