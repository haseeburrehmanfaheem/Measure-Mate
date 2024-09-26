package com.haseeb.measuremate.presentation.util

sealed class UiEvent{
    data class ShowSnackbar(val message: String) : UiEvent()
    data object HideBottomSheet : UiEvent()
    data object Navigate: UiEvent()
}