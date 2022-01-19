@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testtoolbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(openItem: (Long) -> Unit = {}, openSheet: () -> Unit = {}) {
    val scrollBehavior: TopAppBarScrollBehavior =
        remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }
    // layout to provide system bar scrims on all sides
    Box {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                val colors = TopAppBarDefaults.smallTopAppBarColors()
                SmallTopAppBar(
                    title = {
                        Row {
                            Column {
                                Text(text = "HackerNews")
                                CompositionLocalProvider(
                                    LocalTextStyle provides MaterialTheme.typography.titleSmall
                                ) { Text(text = "Top Stories") }
                            }
                        }
                    },
                    modifier = Modifier.statusBarsPadding(),
                    navigationIcon = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = openSheet) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = colors,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            Row {
                val swipeRefreshState: SwipeRefreshState =
                    rememberSwipeRefreshState(isRefreshing = false)
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            scale = true,
                        )
                    },
                ) {
                    val systemBarsBottom =
                        with(LocalDensity.current) {
                            LocalWindowInsets.current.systemBars.layoutInsets.bottom.toDp()
                        }
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = systemBarsBottom),
                    ) {
                        repeat(100) {
                            item {
                                Text(
                                    text = "Hello $it", modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}