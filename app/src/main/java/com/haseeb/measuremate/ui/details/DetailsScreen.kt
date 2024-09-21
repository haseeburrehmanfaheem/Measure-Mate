package com.haseeb.measuremate.ui.details

import android.icu.util.Measure
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.MeasuringUnit
import com.haseeb.measuremate.domain.model.TimeRange
import com.haseeb.measuremate.ui.component.Dialog
import com.haseeb.measuremate.ui.component.MeasuringUnitBottomSheet
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen() {
    var selectedTimeRange by rememberSaveable { mutableStateOf(TimeRange.LAST7DAYS) }

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
        ChartTimeRangeButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            selectedTimeRange = selectedTimeRange,
            onClick = { selectedTimeRange = it },
        )

    }
    
}

@Composable
private fun ChartTimeRangeButtons(
    modifier: Modifier = Modifier,
    selectedTimeRange: TimeRange,
    onClick: (TimeRange)-> Unit
) {
    Row( modifier =
    modifier
        .height(40.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .clip(RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
            ){
        TimeRange.entries.forEach {
            TimeRangeSelectionButton(
                label = it.label,
                modifier = Modifier.weight(1f),
                labelTextStyle = if (it == selectedTimeRange) MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.labelLarge.copy(color = Color.Gray),
                backgroundColor = if (it == selectedTimeRange) MaterialTheme.colorScheme.surface else Color.Transparent,
                onClick = { onClick(it) }
            )
        }
    }
}


@Composable
private fun TimeRangeSelectionButton(
    modifier: Modifier = Modifier,
    label: String,
    labelTextStyle: TextStyle,
    backgroundColor : Color,
    onClick : () -> Unit
    ) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        Text(
            text = label,
            style = labelTextStyle,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
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