package com.haseeb.measuremate.presentation.util

sealed class UiEvent{
    data class ShowSnackbar(val message: String) : UiEvent()
}