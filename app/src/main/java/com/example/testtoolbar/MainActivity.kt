package com.example.testtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.testtoolbar.ui.theme.TestToolbarTheme
import com.google.accompanist.insets.*
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar

class MainActivity : ComponentActivity() {

    val viewModel: TopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                TestToolbarTheme {
                    StatusBarRender()
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        /*val state by viewModel.state.collectAsState()
                    TopDataScreen(state, viewModel::changeParent, viewModel::changeChild)*/
                        //Greeting("Android")
                        //MainScreen()
                        //HomeScreen()
                        //NestedBox()
                        //NestedBox2()
                        TestEnter()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var show by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            var appBarHeight by remember { mutableStateOf(0) }
            val showAppBarBackground by remember {
                derivedStateOf {
                    val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                    when {
                        visibleItemsInfo.isEmpty() -> false
                        appBarHeight <= 0 -> false
                        else -> {
                            val firstVisibleItem = visibleItemsInfo[0]
                            when {
                                // If the first visible item is > 0, we want to show the app bar background
                                firstVisibleItem.index > 0 -> true
                                // If the first item is visible, only show the app bar background once the only
                                // remaining part of the item is <= the app bar
                                else -> firstVisibleItem.size + firstVisibleItem.offset <= appBarHeight
                            }
                        }
                    }
                }
            }
            Column(Modifier.fillMaxWidth()) {
                ShowDetailsAppBar(
                    title = "Title",
                    isRefreshing = false,
                    showAppBarBackground = showAppBarBackground,
                    navigateUp = {},
                    refresh = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { appBarHeight = it.height }
                )
                Box(
                    modifier = Modifier
                        .background(Color.Blue)
                        .height(240.dp)
                        .fillMaxWidth()
                )
                Button(onClick = { show = !show }) {
                    Text(text = "Toggle")
                }
                AnimatedVisibility(visible = show) {
                    Box(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .height(250.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(all = 16.dp)
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

@Composable
private fun ShowDetailsAppBar(
    title: String?,
    isRefreshing: Boolean,
    showAppBarBackground: Boolean,
    navigateUp: () -> Unit,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LogCompositions("ShowDetailsAppBar")
    val backgroundColor by animateColorAsState(
        targetValue = when {
            showAppBarBackground -> androidx.compose.material.MaterialTheme.colors.surface
            else -> Color.Transparent
        },
        animationSpec = spring(),
    )

    val elevation by animateDpAsState(
        targetValue = when {
            showAppBarBackground -> 4.dp
            else -> 0.dp
        },
        animationSpec = spring(),
    )

    TopAppBar(
        title = {
            Crossfade(showAppBarBackground && title != null) { show ->
                if (show) androidx.compose.material.Text(text = title!!)
            }
        },
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.systemBars,
            applyBottom = false
        ),
        navigationIcon = {
            androidx.compose.material.IconButton(
                onClick = navigateUp,
                modifier = Modifier.iconButtonBackgroundScrim(enabled = !showAppBarBackground),
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Up",
                )
            }
        },
        actions = {
            if (isRefreshing) {
                AutoSizedCircularProgressIndicator(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .padding(16.dp)
                )
            } else {
                androidx.compose.material.IconButton(
                    onClick = refresh,
                    modifier = Modifier.iconButtonBackgroundScrim(enabled = !showAppBarBackground),
                ) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        },
        elevation = elevation,
        backgroundColor = backgroundColor,
        modifier = modifier
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestToolbarTheme {
        Greeting("Android")
    }
}