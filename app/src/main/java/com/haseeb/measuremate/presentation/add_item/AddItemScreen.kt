package com.haseeb.measuremate.presentation.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haseeb.measuremate.domain.model.predefinedBodyParts
import com.haseeb.measuremate.presentation.component.Dialog
import com.haseeb.measuremate.presentation.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun AddItemScreen(
    onBackButtonClick: () -> Unit,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    state: AddItemState,
    uiEvent: Flow<UiEvent>,
    onEvent : (AddItemEvent) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect {
                event -> when(event){
            is UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(event.message)
            }

            UiEvent.HideBottomSheet -> {}
            UiEvent.Navigate -> {}
        }
        }
    }
    var isAddNewItemDialogOpen by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            isAddNewItemDialogOpen = false
        },
        onConfirm = {
            isAddNewItemDialogOpen = false
            onEvent(AddItemEvent.UpsertItem)
        },
        title = "Add/Update New Item",
        confirmButtonText = "Save",
        body = {
            OutlinedTextField(value = state.textFieldValue, onValueChange = { onEvent(AddItemEvent.OnTextFieldValueChange(it))  } )
        },
        isOpen = isAddNewItemDialogOpen
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        AddItemTopBar(
            onAddIconClick = { isAddNewItemDialogOpen = true },
            onBackButtonClick = {onBackButtonClick()}
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(state.bodyParts){
                ItemCart(
                    name = it.name,
                    onClick = {
                        isAddNewItemDialogOpen = true
                        onEvent(AddItemEvent.OnItemClick(it))
                    },
                    checked = it.isActive,
                    onCheckedChange = { onEvent(AddItemEvent.OnItemIsActiveChange(it)) }
                )
            }
        }
    }
    
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddItemTopBar(
    modifier: Modifier = Modifier,
    onAddIconClick : () -> Unit,
    onBackButtonClick : () -> Unit
){
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        title = { Text(text = "Add Item") },
        navigationIcon = {
            IconButton(onClick = {onBackButtonClick()}) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Navigate back" )
            }

        },
        actions = {
            IconButton(onClick = {onAddIconClick()}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Item" )
            }

        }
    )
}

@Composable
private fun ItemCart(
    modifier: Modifier = Modifier,
    name : String,
    onClick : () -> Unit,
    checked : Boolean,
    onCheckedChange : () -> Unit
){
    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.weight(8f),
                text = name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = checked, onCheckedChange = { onCheckedChange() })
        }
    }
}





@Preview
@Composable
private fun AddItemScreenPreview() {
    AddItemScreen({}, PaddingValues(0.dp), SnackbarHostState(), AddItemState(), flowOf(), {})

}