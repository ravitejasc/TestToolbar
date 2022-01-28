package com.example.testtoolbar

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

@Composable
fun WeekView(
    modifier: Modifier = Modifier,
    week: WeekState,
    content: @Composable RowScope.(LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        week.days.forEach { day ->
            content(day)
        }
    }
}

