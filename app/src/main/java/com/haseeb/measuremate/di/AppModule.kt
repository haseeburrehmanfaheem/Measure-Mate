package com.haseeb.measuremate.di

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
    fun provideAuthRepository() : AuthRepository{
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository() : DatabaseRepository{
        return DatabaseRepositoryImpl()
    }

}