package com.haseeb.measuremate.domain.repository

import android.content.Context
import com.haseeb.measuremate.domain.model.AuthStatus
import com.haseeb.measuremate.presentation.signin.SignInEvent
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    val authStatus: Flow<AuthStatus>
    suspend fun SignInAnonymously() : Result<Boolean>

    suspend fun SignIn(context: Context) : Result<Boolean>
}