package com.haseeb.measuremate.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.haseeb.measuremate.data.repository.AuthRepositoryImpl
import com.haseeb.measuremate.data.repository.DatabaseRepositoryImpl
import com.haseeb.measuremate.domain.repository.AuthRepository
import com.haseeb.measuremate.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ) : AuthRepository{
        return AuthRepositoryImpl(
            firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        firebaseAuth: FirebaseAuth
    ) : DatabaseRepository{
        return DatabaseRepositoryImpl(
            firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return Firebase.auth
    }

}