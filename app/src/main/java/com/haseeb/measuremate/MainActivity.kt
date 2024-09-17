package com.haseeb.measuremate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.haseeb.measuremate.ui.add_item.AddItemScreen
import com.haseeb.measuremate.ui.dashboard.DashboardScreen
import com.haseeb.measuremate.ui.details.DetailsScreen
import com.haseeb.measuremate.ui.signin.SignInScreen
import com.haseeb.measuremate.ui.theme.MeasureMateTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeasureMateTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
//                SignInScreen(windowSize = windowSizeClass.widthSizeClass )
//                AddItemScreen()
//                DashboardScreen()
                DetailsScreen()
            }

        }
    }
}

