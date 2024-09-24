package com.haseeb.measuremate.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.haseeb.measuremate.domain.repository.DatabaseRepository

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : DatabaseRepository {
}