package com.example.testtoolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TopViewModel : ViewModel() {

    val state = MutableStateFlow(ParentData())

    fun changeParent() {
        val top = state.value.flag + 1
        state.value = state.value.copy(flag = top)
    }

    fun changeChild() {
        val child = state.value.child.flag + 1
        state.value = state.value.copy(child = state.value.child.copy(flag = child))
    }
}

@Composable
fun TopDataScreen(state: ParentData, changeTop: (() -> Unit), changeChild: (() -> Unit)) {
    LogCompositions(tag = "TopDataScreen")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = changeTop,
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(
                color = Color.White,
                text = "IncreaseTop",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        Text(
            color = Color.White,
            text = "Top: ${state.flag}",
            modifier = Modifier.padding(16.dp)
        )
        state.ChildDataScreen(changeChild)
        //ChildDataScreen(child = state.child.flag, changeChild)
    }

}

@Composable
fun ParentData.ChildDataScreen(onAction: (() -> Unit)) {
    LogCompositions(tag = "ChildDataScreen")
    Column(
        modifier = Modifier
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onAction,
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(
                color = Color.White,
                text = "IncreaseChild",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        Text(
            text = "Child: ${child.flag}",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
fun ChildDataScreen(child: Int, onAction: (() -> Unit)) {
    LogCompositions(tag = "ChildDataScreen")
    Column(
        modifier = Modifier
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            color = Color.White,
            text = "IncreaseChild",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onAction() }
        )
        Text(
            text = "Child: $child",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Stable
data class ParentData(val flag: Int = 1, val child: ChildData = ChildData())

data class ChildData(val flag: Int = 1)