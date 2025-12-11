package com.edutrack.di

import com.edutrack.domain.repository.AuthRepository
import com.edutrack.domain.usecases.GetProgressUseCase
import com.edutrack.domain.usecases.GetUserUseCase
import com.edutrack.domain.usecases.LoginUseCase
import com.edutrack.domain.usecases.GoogleLoginUseCase
import com.edutrack.domain.usecases.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGoogleLoginUseCase(repository: AuthRepository): GoogleLoginUseCase {
        return GoogleLoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: AuthRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProgressUseCase(repository: AuthRepository): GetProgressUseCase {
        return GetProgressUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }
}