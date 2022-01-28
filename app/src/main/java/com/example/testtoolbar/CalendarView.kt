package com.example.testtoolbar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstWeekOfMonth(weekFields: WeekFields = WeekFields.of(Locale.getDefault())): LocalDate {
    return withDayOfMonth(1)
        .with(TemporalAdjusters.previousOrSame(weekFields.firstDayOfWeek))
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.prevWeek(): List<LocalDate> {
    return minusDays(7L).getWeek()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.nextWeek(): List<LocalDate> {
    return plusDays(7L).getWeek()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.getWeek(): List<LocalDate> {
    return List(7) { idx -> plusDays(idx.toLong()) }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    formatter: DateTimeFormatter = remember { DateTimeFormatter.ofPattern("MMMM y") },
    firstDay: LocalDate = remember { LocalDate.now().firstWeekOfMonth() },
    scrollableState: InfiniteScrollableState = remember { InfiniteScrollableState() }
) {
    val monthTitle by remember {
        derivedStateOf {
            firstDay.plusWeeks(scrollableState.indexObservable.toLong()).format(formatter)
        }
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = monthTitle) })
    }) {
        val colors = MaterialTheme.colors
        BoxWithConstraints {
            InfiniteComputedScrollList(scrollableState = scrollableState) { idx ->
                val curWeek = firstDay.plusWeeks(idx.toLong())
                val week = remember(curWeek) {
                    WeekState(curWeek.getWeek())
                }
                WeekView(
                    modifier = Modifier
                        .requiredHeight(maxHeight / 5)
                        .draggable(
                            rememberDraggableState(onDelta = {}),
                            orientation = Orientation.Horizontal
                        ),
                    week = week
                )
                { day ->
                    val dayState by rememberUpdatedState(newValue = DayState(day))
                    val backgroundColor by remember {
                        derivedStateOf {
                            if (dayState.day.monthValue % 2 == 0) {
                                colors.background.copy(alpha = 0.8f)
                                    .compositeOver(if (colors.isLight) Color.Black else Color.White)
                            } else {
                                colors.background
                            }
                        }
                    }

                    DayView(
                        modifier = Modifier.background(backgroundColor),
                        day = dayState
                    )
                }
            }
        }
    }
}