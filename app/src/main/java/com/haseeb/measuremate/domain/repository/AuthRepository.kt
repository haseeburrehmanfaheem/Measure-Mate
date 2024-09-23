package com.haseeb.measuremate.domain.repository

import com.haseeb.measuremate.presentation.signin.SignInEvent

interface AuthRepository {
    suspend fun SignInAnonymously() : Result<Boolean>
}