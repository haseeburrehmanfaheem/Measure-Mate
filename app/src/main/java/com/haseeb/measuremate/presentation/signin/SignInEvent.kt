package com.haseeb.measuremate.presentation.signin

import android.content.Context

sealed class SignInEvent {
    data class SignInWithGoogle(val context:Context) : SignInEvent()
    data object SignInAnonymously : SignInEvent()
}