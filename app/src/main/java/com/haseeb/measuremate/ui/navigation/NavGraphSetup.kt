package com.haseeb.measuremate.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.haseeb.measuremate.ui.add_item.AddItemScreen
import com.haseeb.measuremate.ui.dashboard.DashboardScreen
import com.haseeb.measuremate.ui.details.DetailsScreen
import com.haseeb.measuremate.ui.signin.SignInScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController,
    windowSize : WindowWidthSizeClass,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination =  Routes.DashboardScreen){
        composable<Routes.DashboardScreen>{
            DashboardScreen(
                onFabClicked = {navController.navigate(Routes.AddItemScreen)},
                onItemCardClicked = {
                    navController.navigate(
                        Routes.DetailsScreen(it)
                    )
                },
                paddingValues = paddingValues
            )
        }
        composable<Routes.SignInScreen> {
            SignInScreen(
                windowSize = windowSize,
                paddingValues = paddingValues
            )
        }
        composable<Routes.AddItemScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {slideOutOfContainer(
                animationSpec = tween(durationMillis = 500),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )}
        ) {
            AddItemScreen(
                onBackButtonClick = {navController.navigateUp()},
                paddingValues = paddingValues
            )
        }
        composable<Routes.DetailsScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {slideOutOfContainer(
                animationSpec = tween(durationMillis = 500),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )}
        ) { navBackStackEntry ->
            val bodyPartId = navBackStackEntry.toRoute<Routes.DetailsScreen>().bodyPartId
            DetailsScreen(
                bodyPartId = bodyPartId,
                windowSizeClass = windowSize,
                onBackButtonClick = {navController.navigateUp()},
                paddingValues = paddingValues
            )
        }

    }
    
}