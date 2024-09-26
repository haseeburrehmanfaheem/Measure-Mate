package com.haseeb.measuremate.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.haseeb.measuremate.presentation.add_item.AddItemScreen
import com.haseeb.measuremate.presentation.dashboard.DashboardScreen
import com.haseeb.measuremate.presentation.dashboard.DashboardState
import com.haseeb.measuremate.presentation.dashboard.DashboardViewModel
import com.haseeb.measuremate.presentation.details.DetailsScreen
import com.haseeb.measuremate.presentation.signin.SignInScreen
import com.haseeb.measuremate.presentation.signin.SignInViewModel
import com.haseeb.measuremate.presentation.util.UiEvent

@Composable
fun NavGraphSetup(
    navController: NavHostController,
    windowSize : WindowWidthSizeClass,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    signInViewModel: SignInViewModel
) {


    LaunchedEffect(key1 = Unit) {
        signInViewModel.uiEvent.collect {
                event -> when(event){
            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message)
            }
        }
        }

    }
    NavHost(
        navController = navController,
        startDestination =  Routes.DashboardScreen){

        composable<Routes.DashboardScreen>{
            val dashboardViewModel = hiltViewModel<DashboardViewModel>()
            val state by dashboardViewModel.state.collectAsStateWithLifecycle()



            DashboardScreen(
                onFabClicked = {navController.navigate(Routes.AddItemScreen)},
                onItemCardClicked = {
                    navController.navigate(
                        Routes.DetailsScreen(it)
                    )
                },
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                uiEvent = dashboardViewModel.uiEvent,
                state = state,
                onEvent = dashboardViewModel::onEvent
            )
        }
        composable<Routes.SignInScreen> {

            val state by signInViewModel.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSize = windowSize,
                paddingValues = paddingValues,
                state = state,
                onEvent = signInViewModel::onEvent,
                snackbarHostState = snackbarHostState,
                uiEvent = signInViewModel.uiEvent
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
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState
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
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState
            )
        }

    }
    
}