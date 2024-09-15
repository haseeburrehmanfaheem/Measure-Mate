package com.haseeb.measuremate.ui.dashboard

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.ui.component.ProfilePicPlaceholder

@Composable
fun DashboardScreen(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DashboardTopBar(
            profilePictureUrl = null,
            onProfilePicClick = {}
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    modifier: Modifier = Modifier,
    profilePictureUrl : String?,
    onProfilePicClick : () -> Unit
){
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "MeasureMate") },
        actions = {
            IconButton(onClick = {onProfilePicClick()}) {
                ProfilePicPlaceholder(
                    placeholderSize = 30.dp,
                    borderWidth = 1.dp,
                    padding = 2.dp,
                    profilePictureUrl = profilePictureUrl
                )
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenPreview(){
    DashboardScreen()
}