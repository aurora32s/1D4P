package com.feature.monthly

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.time.LocalDate
import java.time.YearMonth

private const val YEAR_ARG = "year"
private const val MONTH_ARG = "month"
private const val MONTHLY_SCREEN_ROUTE = "monthly"

internal class YearMonthArg(savedStateHandle: SavedStateHandle) {
    var currentYearMonth: YearMonth

    init {
        val year: Int? = savedStateHandle[YEAR_ARG]
        val month: Int? = savedStateHandle[MONTH_ARG]
        currentYearMonth = if (year == null || month == null)
            YearMonth.now() else YearMonth.of(year, month)
    }
}

fun NavController.navigateToMonthly(yearMonth: YearMonth) {
    val year = yearMonth.year
    val month = yearMonth.monthValue
    this.navigate("$MONTHLY_SCREEN_ROUTE/$year/$month")
}

fun NavGraphBuilder.monthlyScreen(
    onBackPressed: () -> Unit,
    onDailyClick: (LocalDate) -> Unit
) {
    composable(
        route = "$MONTHLY_SCREEN_ROUTE/{${YEAR_ARG}}/{${MONTH_ARG}}",
        arguments = listOf(
            navArgument(YEAR_ARG) { type = NavType.IntType },
            navArgument(MONTH_ARG) { type = NavType.IntType }
        )
    ) {
        MonthlyRoute(
            onBackPressed = onBackPressed,
            onDailyClick = onDailyClick
        )
    }
}