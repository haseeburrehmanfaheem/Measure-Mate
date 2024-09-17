package com.haseeb.measuremate.ui.details

import android.icu.util.Measure
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.MeasuringUnit
import com.haseeb.measuremate.ui.component.Dialog
import com.haseeb.measuremate.ui.component.MeasuringUnitBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen() {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    MeasuringUnitBottomSheet(
        isOpen = isBottomSheetOpen,
        onDismissRequest = {isBottomSheetOpen = false},
        onitemClicked = {
            scope
                .launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        isBottomSheetOpen = false
                    }
                }
        },
        sheetState = sheetState
    )

    var isDeleteBodyPartDialogOpen by rememberSaveable { mutableStateOf(false) }
    Dialog(
        onDismissRequest = {
            isDeleteBodyPartDialogOpen = false
        },
        onConfirm = {
            isDeleteBodyPartDialogOpen = false
        },
        title = "Delete Body Part?",
        body = {
            Text("Are you sure you want to delete this body part?")
        },
        isOpen = isDeleteBodyPartDialogOpen,
        confirmButtonText = "Delete"
    )


    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        DetailsTopBar(
            onDeleteIconClick = {isDeleteBodyPartDialogOpen = true},
            onBackButtonClick = {  },
            bodyPart = BodyPart(
                "Chest",
                true,
                MeasuringUnit.CM.code,
                ),
            onUnitIconClick = {isBottomSheetOpen = true}
        )

    }
    
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    onDeleteIconClick : () -> Unit,
    onBackButtonClick : () -> Unit,
    onUnitIconClick : () -> Unit ,
    bodyPart: BodyPart?
){
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = bodyPart?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
                },
        navigationIcon = {
            IconButton(onClick = {onBackButtonClick()}) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate back" )
            }

        },
        actions = {
            IconButton(onClick = {onDeleteIconClick()}) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete Icon" )
            }
            Spacer(modifier =Modifier.width(4.dp))
            Text(text = bodyPart?.measuringUnit ?: "")
            IconButton(onClick = {onUnitIconClick()}) {
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = "Drop down Icon" )
            }

        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    DetailsScreen()
    
}