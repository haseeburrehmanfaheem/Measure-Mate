package com.haseeb.measuremate.di

import android.content.Context
import androidx.credentials.CredentialManager
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        credentialManager: CredentialManager
    ) : AuthRepository{
        return AuthRepositoryImpl(
            firebaseAuth,credentialManager
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


    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ) : CredentialManager{
        return CredentialManager.create(context)
    }

}