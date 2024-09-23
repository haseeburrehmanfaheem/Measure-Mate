package com.haseeb.measuremate.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    title : String,
    confirmButtonText : String = "Yes",
    dismissButtonText : String = "Cancel",
    body : @Composable () -> Unit ,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    isOpen : Boolean
) {
    if(isOpen){
        AlertDialog(
            modifier = modifier,
            title = { Text(text = title) },
            onDismissRequest = { onDismissRequest() },
            text = { body() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text =confirmButtonText)

                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = dismissButtonText)
                }
            }
        )

    }

}


@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    Dialog(
        onDismissRequest = {},
        onConfirm = {},
        title = "Login anonymously?",
        body = {
            Text("You can use the app without signing in. However, your data will not be saved. \n Are you sure you want to proceed?")
        },
        isOpen = true
    )
}