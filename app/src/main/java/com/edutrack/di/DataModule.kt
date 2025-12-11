package com.edutrack.di

import android.content.Context
import android.content.SharedPreferences
import com.edutrack.data.api.ApiService
import com.edutrack.data.repository.AuthRepositoryImpl
import com.edutrack.data.storage.TokenStorage
import com.edutrack.data.storage.SharedPrefsTokenStorage
import com.edutrack.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // SharedPreferences
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("edutrack_prefs", Context.MODE_PRIVATE)
    }

    // TokenStorage
    @Provides
    @Singleton
    fun provideTokenStorage(prefs: SharedPreferences): TokenStorage {
        return SharedPrefsTokenStorage(prefs)
    }

    // Network
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.168.123:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        tokenStorage: TokenStorage
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, tokenStorage)
    }
}