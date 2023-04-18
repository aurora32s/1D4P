package com.haman.allformemory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.feature.home.HOME_SCREEN_ROUTE
import com.feature.home.homeScreen
import com.feature.monthly.monthlyScreen
import com.feature.monthly.navigateToMonthly
import com.feature.post.navigateToPost
import com.feature.post.postScreen

@Composable
fun HarooNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = HOME_SCREEN_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            onDailyClick = { navController.navigateToPost(it) },
            onMonthlyClick = { navController.navigateToMonthly(it) }
        )
        monthlyScreen(
            onBackPressed = navController::popBackStack,
            onDailyClick = { navController.navigateToPost(it) }
        )
        postScreen(
            onBackPressed = navController::popBackStack
        )
    }
}