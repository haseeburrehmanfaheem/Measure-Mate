package com.haseeb.measuremate.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.haseeb.measuremate.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun SignInAnonymously(): Result<Boolean> {
        return try {
            firebaseAuth.signInAnonymously().await()
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}