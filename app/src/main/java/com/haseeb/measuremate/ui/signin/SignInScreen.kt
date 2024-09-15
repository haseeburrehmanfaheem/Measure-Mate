package com.haseeb.measuremate.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.R
import com.haseeb.measuremate.ui.theme.MeasureMateTheme


@Composable
fun SignInScreen(){
    Column {
        Image(painter = painterResource(R.drawable.app_logo),
            contentDescription = "Application Logo",
            modifier = Modifier.size(120.dp)
        )
        Text(text = "Measure Mate",
            style = MaterialTheme.typography.headlineLarge
           )
        Text(text = "Measure progress, not perfection",
            style = MaterialTheme.typography.bodySmall.copy(fontStyle =  FontStyle.Italic)
        )


    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SignInScreenPreview(){
    MeasureMateTheme {  SignInScreen() }


}