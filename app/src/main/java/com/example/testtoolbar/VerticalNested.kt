@file:OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)

package com.example.testtoolbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun VerticalNestedTest() {
    val nestedScrollViewState = rememberNestedScrollViewState()
    LogCompositions(tag = "VerticalNestedTest")
    Scaffold(
        //topBar = { BackTopBar(state = nestedScrollViewState, isTitleTopBar = true) }
    ) {
        var show by remember { mutableStateOf(false) }
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        VerticalNestedScrollView(
            state = nestedScrollViewState,
            header = {
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
                                    androidx.compose.material3.Text(
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
                                        androidx.compose.material3.Text(
                                            text = "Followers \n 100",
                                            color = Color.Black
                                        )
                                        androidx.compose.material3.Text(
                                            text = "Following \n 20",
                                            color = Color.Black
                                        )
                                        androidx.compose.material3.Text(
                                            text = "Posts \n 203",
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                        androidx.compose.material3.Text(
                            text = "Raviteja Gameboy",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.Text(
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
                                    androidx.compose.material3.OutlinedButton(
                                        onClick = {},
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        androidx.compose.material3.Text(
                                            text = "Badge $it",
                                            color = Color.Black
                                        )
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
                            androidx.compose.material3.Button(
                                onClick = { show = !show }, shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .height(buttonHeight.toDp())
                                    .weight(1F)
                            ) {
                                androidx.compose.material3.Text(
                                    text = "Follow",
                                    color = Color.White
                                )
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
                    BackTopBar(Modifier.align(Alignment.TopCenter), nestedScrollViewState)
                }
            },
            content = {
                val pages = (1..5).map { it }
                Column {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                            )
                        }
                    ) {
                        pages.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(text = "tab $title") },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                            )
                        }
                    }
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
                        ) {
                            itemsIndexed(listItems) { _, imageUrl ->
                                GridItem(imageUrl)
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    isTitleTopBar: Boolean = false
) {
    BackInsetTopBar(
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
}