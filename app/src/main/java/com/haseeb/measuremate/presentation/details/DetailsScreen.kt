package com.haseeb.measuremate.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.haseeb.measuremate.presentation.component.Dialog
import com.haseeb.measuremate.presentation.component.MeasuringUnitBottomSheet
import kotlinx.coroutines.launch
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import com.haseeb.measuremate.domain.model.BodyPartValue
import com.haseeb.measuremate.presentation.component.DatePicker
import com.haseeb.measuremate.presentation.component.LineGraph
import com.haseeb.measuremate.presentation.component.NewValueInputBar
import com.haseeb.measuremate.presentation.util.PastOrPresentSelectableDates
import com.haseeb.measuremate.presentation.util.UiEvent
import com.haseeb.measuremate.presentation.util.changeLocalDateToDateString
import com.haseeb.measuremate.presentation.util.changeMillisToLocalDate
import com.haseeb.measuremate.presentation.util.roundToDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    windowSizeClass : WindowWidthSizeClass,
    onBackButtonClick : () -> Unit = {},
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    state: DetailsState,
    onEvent: (DetailsEvent)->Unit,
    uiEvent: Flow<UiEvent>
)  {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect {
                event -> when(event){
            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message)
            }

            UiEvent.HideBottomSheet -> {}
            UiEvent.Navigate -> {
                onBackButtonClick()
            }
        }
        }
    }



    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = PastOrPresentSelectableDates
    )
    DatePicker(
        state = datePickerState,
        isOpen = isDatePickerOpen,
        onDismissRequest = {isDatePickerOpen = false},
        onConfirmButtonClicked = {
            isDatePickerOpen = false
            onEvent(DetailsEvent.OnDateChange(millis = datePickerState.selectedDateMillis))
        }
    )
    var inputValue by remember {
        mutableStateOf("")
    }
    var isInputValueCardVisible by rememberSaveable { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current

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
            onEvent(DetailsEvent.ChangeMeasuringUnit(it))
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
            onEvent(DetailsEvent.DeleteBodyPart)
        },
        title = "Delete Body Part?",
        body = {
            Text("Are you sure you want to delete this body part?")
        },
        isOpen = isDeleteBodyPartDialogOpen,
        confirmButtonText = "Delete"
    )



    when(windowSizeClass){
        WindowWidthSizeClass.Compact ->{
            Box(modifier = Modifier.fillMaxSize())
            {
                Column(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    DetailsTopBar(
                        onDeleteIconClick = { isDeleteBodyPartDialogOpen = true },
                        onBackButtonClick = {onBackButtonClick() },
                        bodyPart = state.bodyPart,
                        onUnitIconClick = { isBottomSheetOpen = true }
                    )
                    ChartTimeRangeButtons(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        selectedTimeRange = state.timeRange,
                        onClick = { onEvent(DetailsEvent.OnTimeRangeChange(it)) },
                    )

                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 2 / 1f)
                            .padding(16.dp),
                        bodyPartValues = state.graphBodyPartValues
                    )

                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        HistorySection(bodyPartValues = state.allBodyPartValues, measuringUnitCode = state.bodyPart?.measuringUnit) {}
                    }
                }
                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    date = state.date.changeLocalDateToDateString(),
                    isInputValueCardVisible = isInputValueCardVisible,
                    value = state.textFieldValue,
                    onValueChange = {onEvent(DetailsEvent.OnTextFieldValueChange(it))},
                    onDoneIconClick = {
                        focusManager.clearFocus()
                        onEvent(DetailsEvent.AddNewValue)
                                      },
                    onDoneImeActionClick = {focusManager.clearFocus()},
                    onCalendarIconClick = {isDatePickerOpen = true}
                )

                InputCardHideIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp),
                    isInputValueCardVisible = isInputValueCardVisible,
                    onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                )
            }
        }
        else ->{
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                DetailsTopBar(
                    onDeleteIconClick = { isDeleteBodyPartDialogOpen = true },
                    onBackButtonClick = { onBackButtonClick()},
                    bodyPart = state.bodyPart,
                    onUnitIconClick = { isBottomSheetOpen = true }
                )
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ChartTimeRangeButtons(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            selectedTimeRange = state.timeRange,
                            onClick = { onEvent(DetailsEvent.OnTimeRangeChange(it)) },
                        )

                        LineGraph(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 2 / 1f)
                                .padding(16.dp),
                            bodyPartValues = state.graphBodyPartValues
                        )

                    }
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ){
                        HistorySection(bodyPartValues = state.allBodyPartValues, measuringUnitCode = MeasuringUnit.CM.code) {}


                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    date = state.date.changeLocalDateToDateString(),
                    isInputValueCardVisible = isInputValueCardVisible,
                    value = state.textFieldValue,
                    onValueChange = {onEvent(DetailsEvent.OnTextFieldValueChange(it))},
                    onDoneIconClick = {},
                    onDoneImeActionClick = {focusManager.clearFocus()},
                    onCalendarIconClick = {isDatePickerOpen = true}
                )

                InputCardHideIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp),
                    isInputValueCardVisible = isInputValueCardVisible,
                    onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                )

                    }
                }

            }
        }
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
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistorySection(
    modifier: Modifier = Modifier,
    bodyPartValues: List<BodyPartValue>,
    measuringUnitCode: String?,
    onDeleteIconClick: (BodyPartValue) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        val grouped = bodyPartValues.groupBy { it.date.month }
        item {
            Text(text = "History", textDecoration = TextDecoration.Underline)
            Spacer(modifier = Modifier.height(20.dp))
        }
        grouped.forEach { (month, bodyPartValues) ->
            stickyHeader {
                Text(
                    text = month.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            items(bodyPartValues) { bodyPartValue ->
                HistoryCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    bodyPartValue = bodyPartValue,
                    measuringUnitCode = measuringUnitCode,
                    onDeleteIconClick = { onDeleteIconClick(bodyPartValue) }
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartValue: BodyPartValue,
    measuringUnitCode: String?,
    onDeleteIconClick: () -> Unit
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 5.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
            Text(
                text = bodyPartValue.date.changeLocalDateToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${bodyPartValue.value.roundToDecimal(4)} ${measuringUnitCode ?: ""}",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = { onDeleteIconClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
fun InputCardHideIcon(
    modifier: Modifier = Modifier,
    isInputValueCardVisible: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = if (isInputValueCardVisible) Icons.Default.KeyboardArrowDown
            else Icons.Default.KeyboardArrowUp,
            contentDescription = "Close or Open Input Card"
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    DetailsScreen(
        windowSizeClass = WindowWidthSizeClass.Compact,
        onBackButtonClick = {},
        paddingValues = PaddingValues(0.dp),
        snackbarHostState = SnackbarHostState(),
        state = DetailsState(),
        onEvent = {},
        uiEvent = flowOf()
    )
    
}