package com.feature.post

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.time.LocalDate

private const val YEAR_ARG = "year"
private const val MONTH_ARG = "month"
private const val DAY_ARG = "day"
private const val POST_SCREEN_ROUTE = "post"

internal class DateArg(savedStateHandle: SavedStateHandle) {
    var currentDate: LocalDate
    val year: Int get() = currentDate.year
    val month: Int get() = currentDate.monthValue
    val day: Int get() = currentDate.dayOfMonth

    init {
        val year: Int? = savedStateHandle[YEAR_ARG]
        val month: Int? = savedStateHandle[MONTH_ARG]
        val day: Int? = savedStateHandle[DAY_ARG]
        currentDate =
            if (year == null || month == null || day == null) LocalDate.now()
            else LocalDate.of(year, month, day)
    }
}

fun NavController.navigateToPost(date: LocalDate) {
    val year = date.year
    val month = date.monthValue
    val day = date.dayOfMonth
    this.navigate("$POST_SCREEN_ROUTE/$year/$month/$day")
}

fun NavGraphBuilder.postScreen(
    onBackPressed: () -> Unit
) {
    composable(
        route = "$POST_SCREEN_ROUTE/{$YEAR_ARG}/{$MONTH_ARG}/{${DAY_ARG}}",
        arguments = listOf(
            navArgument(YEAR_ARG) { type = NavType.IntType },
            navArgument(MONTH_ARG) { type = NavType.IntType },
            navArgument(DAY_ARG) { type = NavType.IntType }
        )
    ) {
        PostRoute(onBackPressed)
    }
}