package com.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.time.LocalDate
import java.time.YearMonth

private const val HOME_SCREEN_ROUTE = "home"

fun NavGraphBuilder.homeScreen(
    onDailyClick: (LocalDate) -> Unit,
    onMonthlyClick: (YearMonth) -> Unit
) {
    composable(route = HOME_SCREEN_ROUTE) {
        HomeRoute(
            onDailyClick = onDailyClick,
            onMonthlyClick = onMonthlyClick
        )
    }
}