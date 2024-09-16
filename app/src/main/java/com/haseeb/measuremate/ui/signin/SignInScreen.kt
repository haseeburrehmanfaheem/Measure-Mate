package com.haseeb.measuremate.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.R
import com.haseeb.measuremate.ui.component.AnonymousSignInButton
import com.haseeb.measuremate.ui.component.Dialog
import com.haseeb.measuremate.ui.component.GoogleSignInButton
import com.haseeb.measuremate.ui.theme.MeasureMateTheme


@Composable
fun SignInScreen(
    windowSize : WindowWidthSizeClass
){

    var isSignInAnonymousDialogOpen by rememberSaveable { mutableStateOf(false) }
    Dialog(
        onDismissRequest = {
            isSignInAnonymousDialogOpen = false
        },
        onConfirm = {
            isSignInAnonymousDialogOpen = false
        },
        title = "Login anonymously?",
        body = {
            Text("You can use the app without signing in. However, your data will not be saved. \n Are you sure you want to proceed?")
        },
        isOpen = isSignInAnonymousDialogOpen
    )

    when(windowSize){
        WindowWidthSizeClass.Compact -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(R.drawable.app_logo),
                    contentDescription = "Application Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Measure Mate",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(text = "Measure progress, not perfection",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle =  FontStyle.Italic)
                )
                Spacer(modifier = Modifier.fillMaxHeight( 0.4f))

                GoogleSignInButton(
                    onClick = {

                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                AnonymousSignInButton(
                    onClick = {
                        isSignInAnonymousDialogOpen = true
                    }
                )

            }
        }
        else -> {
            Row (
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(painter = painterResource(R.drawable.app_logo),
                        contentDescription = "Application Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Measure Mate",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(text = "Measure progress, not perfection",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle =  FontStyle.Italic)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){

                    GoogleSignInButton(
                        onClick = {

                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    AnonymousSignInButton(
                        onClick = {
                            isSignInAnonymousDialogOpen = true
                        }
                    )
                }
            }
        }
    }

}



@Preview(showSystemUi = true, showBackground = true)
//@PreviewScreenSizes
@Composable
private fun SignInScreenPreview(){
    MeasureMateTheme {
        SignInScreen(windowSize = WindowWidthSizeClass.Compact)
    }
}