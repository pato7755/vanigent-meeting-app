package com.vanigent.meetingapp.di

import com.vanigent.meetingapp.domain.repository.CryptoManager
import com.vanigent.meetingapp.util.CryptoManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManagerImpl()
}