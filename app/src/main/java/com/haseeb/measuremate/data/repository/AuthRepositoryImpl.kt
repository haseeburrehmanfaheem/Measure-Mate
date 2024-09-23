package com.haseeb.measuremate.data.repository

import com.haseeb.measuremate.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun SignInAnonymously(): Result<Boolean> {
        return try {
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}