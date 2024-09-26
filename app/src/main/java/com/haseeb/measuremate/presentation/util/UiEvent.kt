package com.haseeb.measuremate.presentation.util

sealed class UiEvent{
    data class ShowSnackbar(
        val message: String,
        val actionLabel : String ?= null,
    ) : UiEvent()
    data object HideBottomSheet : UiEvent()
    data object Navigate: UiEvent()
}