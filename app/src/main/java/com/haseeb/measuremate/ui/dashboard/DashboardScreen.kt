package com.haseeb.measuremate.ui.dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.User
import com.haseeb.measuremate.domain.model.predefinedBodyParts
import com.haseeb.measuremate.ui.component.Dialog
import com.haseeb.measuremate.ui.component.ProfileBottomSheet
import com.haseeb.measuremate.ui.component.ProfilePicPlaceholder
import com.haseeb.measuremate.ui.theme.MeasureMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(){
    var isSignOutDialogOpen by rememberSaveable { mutableStateOf(false) }

    var isProfileBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val user = User(
        name = "Haseeb",
        email = "haseebrehman208@gmail.com",
        profilePictureUrl = "",
        isAnonymous = false
    )
    ProfileBottomSheet(
        isOpen = isProfileBottomSheetOpen,
        user = user,
        onDismissRequest = {
            isProfileBottomSheetOpen = false
        },
        buttonLoadingState = false,
        buttonPrimaryText = "Sign out with Google",
        onGoogleButtonClick = {
            isSignOutDialogOpen = true
        }
    )

    Dialog(
        onDismissRequest = {
            isSignOutDialogOpen = false
        },
        onConfirm = {
            isSignOutDialogOpen = false
        },
        title = "Sign Out",
        body = {
            Text("Are you sure you want to sign out?")
        },
        isOpen = isSignOutDialogOpen
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DashboardTopBar(
                profilePictureUrl = user.profilePictureUrl,
                onProfilePicClick = {
                    isProfileBottomSheetOpen = true
                }
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                items(predefinedBodyParts){
                    ItemCart(
                        bodyPart = it
                    )
                }
            }

        }
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(30.dp)
            )
        }

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


@Composable
private fun ItemCart(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart
){
    Card {
        Row (
            modifier = modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.weight(8f),
                maxLines = 1 ,
                text = bodyPart.name,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${ bodyPart.latestValue?.toString() ?: "" } ${bodyPart.measuringUnit}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(4.dp)
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Add",
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenPreview(){

    MeasureMateTheme {
        DashboardScreen()
    }
}