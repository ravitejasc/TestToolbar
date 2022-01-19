@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package com.example.testtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.testtoolbar.ui.theme.CollapsingToolbarTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import kotlin.math.absoluteValue
import kotlin.math.max

class ParallaxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ParallaxEffect()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ParallaxEffect() {
    val state = rememberCollapsingToolbarScaffoldState()
    val collapsingState = state.toolbarState
    var showSuggestions by remember { mutableStateOf(false) }
    val toggle: ((Boolean) -> Unit) = { showSuggestions = it }
    val pagerState = rememberPagerState()
    val tabData = listOf("Tab1", "Tab2", "Tab3", "Tab4")
    // Simulate a fake 2-second 'load'. Ideally this 'refreshing' value would
    // come from a ViewModel or similar
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(2000)
            refreshing = false
        }
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbarModifier = Modifier.background(MaterialTheme.colors.primary),
            enabled = true,
            toolbar = {
                CollapsedState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(1f - collapsingState.progress),
                    pagerState = pagerState,
                    coroutineScope = rememberCoroutineScope(),
                    tabData = tabData,
                )
                ExpandedState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(collapsingState.progress),
                    pagerState = pagerState,
                    showSuggestions = showSuggestions,
                    toggle = toggle,
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    List(100) { "Hello World!! $it" }
                ) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedState(
    modifier: Modifier,
    pagerState: PagerState,
    showSuggestions: Boolean,
    toggle: (Boolean) -> Unit
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Cyan),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "<-", color = Color.White, style = MaterialTheme.typography.h5)
                Text(text = "|||", color = Color.White, style = MaterialTheme.typography.h5)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Followers",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Followings",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
                Text(text = "Posts", color = Color.White, style = MaterialTheme.typography.body1)
            }
        }
        Button(
            onClick = { toggle(!showSuggestions) },
            modifier = modifier
                .width(55.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        ) {
            Text(text = "Toggle", color = Color.White)
        }
        if (showSuggestions) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                repeat(30) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(55.dp)
                                .padding(16.dp)
                                .background(if (it % 2 == 0) Color.Magenta else Color.Blue)
                        )
                    }
                }
            }
        }
        TabLayout(pagerState = pagerState)
    }
}

@Composable
private fun CollapsedState(
    modifier: Modifier,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    tabData: List<String>,
) {

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "<- Ravi", color = Color.Black, style = MaterialTheme.typography.h5)
            Text(text = "|||", color = Color.White, style = MaterialTheme.typography.h5)
        }
        TabLayout(pagerState = pagerState)
    }
}

@ExperimentalPagerApi
@Composable
private fun TabLayout(pagerState: PagerState) {
    // Display 10 items
    HorizontalPager(
        count = 10,
        state = pagerState,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = 2.dp),
        // Add some horizontal spacing between items
        itemSpacing = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        PagerSampleItem(page = page)
    }
    Divider()
}

@ExperimentalPagerApi
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
): Modifier = composed {
    // If there are no pages, nothing to show
    if (pagerState.pageCount == 0) return@composed this

    val targetIndicatorOffset: Dp
    val indicatorWidth: Dp

    val currentTab = tabPositions[pagerState.currentPage]
    val targetPage = pagerState.targetPage
    val targetTab = tabPositions.getOrNull(targetPage)

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction = (pagerState.currentPageOffset / max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
        indicatorWidth = lerp(currentTab.width, targetTab.width, fraction).absoluteValue
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
        indicatorWidth = currentTab.width
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = targetIndicatorOffset)
        .width(indicatorWidth)
}

private inline val Dp.absoluteValue: Dp
    get() = value.absoluteValue.dp

@Composable
internal fun PagerSampleItem(
    page: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        // Displays the page index
        Text(
            text = page.toString(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(4.dp))
                .sizeIn(minWidth = 40.dp, minHeight = 40.dp)
                .padding(8.dp)
                .wrapContentSize(Alignment.Center)
        )
    }
}