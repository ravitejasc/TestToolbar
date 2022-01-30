@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class
)

package com.example.testtoolbar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.testtoolbar.ui.theme.TestToolbarTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi

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
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        color = Color.Transparent
                    ) {
                        /*val state by viewModel.state.collectAsState()
                    TopDataScreen(state, viewModel::changeParent, viewModel::changeChild)*/
                        //Greeting("Android")
                        //MainScreen()
                        //HomeScreen()
                        //NestedBox()
                        //NestedBox2()
                        //TestEnter()
                        //TestCollapse()
                        ParallaxEffect()
                        //ParallaxEffect2()
                        //VerticalNestedTest()
                        //MatToolBar()
                        //ListTransitionRecipe()
                        //NestedLists()
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            CalendarView()
                        }*/
                        //CustomColumnScreen()
                        //LargeTopbarTest()
                        //MainScreen()
                    }
                }
            }
        }
    }
}
