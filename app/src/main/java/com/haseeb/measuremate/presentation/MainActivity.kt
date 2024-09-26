package com.haseeb.measuremate.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.haseeb.measuremate.domain.model.AuthStatus
import com.haseeb.measuremate.presentation.navigation.NavGraphSetup
import com.haseeb.measuremate.presentation.navigation.Routes
import com.haseeb.measuremate.presentation.signin.SignInViewModel
import com.haseeb.measuremate.presentation.theme.MeasureMateTheme
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MeasureMateTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val navController = rememberNavController()
//                SignInScreen(windowSize = windowSizeClass.widthSizeClass )
//                AddItemScreen()
//                DashboardScreen()
//                DetailsScreen(windowSizeClass = windowSizeClass.widthSizeClass)
                val signInViewModel : SignInViewModel = hiltViewModel()
                val authStatus by signInViewModel.authStatus.collectAsStateWithLifecycle()
                var previousAuthStatus by rememberSaveable {
                    mutableStateOf<AuthStatus?>(null)
                }
                LaunchedEffect(key1 = authStatus) {
                    if(previousAuthStatus != authStatus){
                        when(authStatus){
                            AuthStatus.AUTHORIZED -> {
                                navController.navigate(Routes.DashboardScreen){popUpTo(0)}
                            }
                            AuthStatus.UNAUTHORIZED -> {
                                navController.navigate(Routes.SignInScreen){popUpTo(0)}
                            }

                            AuthStatus.LOADING -> {}
                        }
                    }
                    previousAuthStatus = authStatus
                }
                val snackbarHostState = remember {
                    SnackbarHostState()
                }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { paddingValues ->
                    NavGraphSetup(
                        navController = navController,
                        windowSize = windowSizeClass.widthSizeClass,
                        paddingValues = paddingValues,
                        snackbarHostState = snackbarHostState,
                        signInViewModel = signInViewModel
                    )
                }

            }

        }
    }
}

