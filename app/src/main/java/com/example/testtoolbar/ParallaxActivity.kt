@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)

package com.example.testtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.rememberImagePainter
import com.example.testtoolbar.ui.theme.CollapsingToolbarTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

val tabs = listOf("Tab1", "Tab2", "Tab3", "Tab4", "Tab5")

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ParallaxEffect() {
    val state = rememberCollapsingToolbarScaffoldState()
    val collapsingState = state.toolbarState
    var showSuggestions by remember { mutableStateOf(false) }
    val toggle: (() -> Unit) = { showSuggestions = !showSuggestions }
    val pagerState = rememberPagerState()
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
                CollapsedState2(
                    modifier = Modifier
                        .fillMaxWidth()
                        /* .graphicsLayer {
                             translationY = -collapsingState.progress
                         },*/
                        .alpha(1f - collapsingState.progress),
                    pagerState = pagerState,
                    pages = tabs
                )
                ExpandedState2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(rememberScrollState(), Orientation.Vertical)
                        /*.graphicsLayer {
                            translationY = -collapsingState.progress * 0.18f
                        },*/
                        .alpha(collapsingState.progress),
                    pagerState = pagerState,
                    pages = tabs,
                    show = showSuggestions,
                    toggleShow = toggle
                )
            }
        ) {
            HorizontalPager(
                count = tabs.size,
                state = pagerState,
                itemSpacing = 4.dp,
                modifier = Modifier.fillMaxWidth(),
                //contentPadding = PaddingValues(top = this@EnterAlwaysCollapsed)
            ) {
                LazyVerticalGrid(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.fillMaxSize(),
                    cells = GridCells.Fixed(3),
                    // contentPadding = cp,
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
fun ExpandedState(
    modifier: Modifier,
    pagerState: PagerState,
    pages: List<String>,
    showSuggestions: Boolean,
    toggleShow: (() -> Unit) = {}
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
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
            Column {
                Text(
                    text = "Ravi",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                    Text(
                        text = "Posts",
                        color = Color.White,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        Button(onClick = toggleShow) {
            Text(
                text = "Ravi",
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
        if (showSuggestions) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
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
        TabLayout(pagerState = pagerState, pages = pages)
    }
}

val listItems = List(400) { randomSampleImageUrl(it) }

@Composable
fun CollapsedState(
    modifier: Modifier,
    pagerState: PagerState,
    pages: List<String>,
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
        TabLayout(pagerState = pagerState, pages = pages)
    }
}

@ExperimentalPagerApi
@Composable
fun TabLayout(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    pages: List<String> = tabs
) {
    val scope = rememberCoroutineScope()
    TabRow(
        modifier = modifier,
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        // Add tabs for all of our pages
        pages.forEachIndexed { index, title ->
            val textColor by animateColorAsState(if (pagerState.currentPage == index) Color.Gray else Color.White)
            Tab(
                text = { Text(title, color = textColor) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
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
fun ListItem(
    page: Int,
    index: Int,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Row(modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "Text: $index Page: $page",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}
