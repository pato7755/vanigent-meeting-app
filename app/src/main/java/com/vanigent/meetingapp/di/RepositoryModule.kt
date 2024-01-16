package com.vanigent.meetingapp.di

import com.vanigent.meetingapp.data.repository.MeetingRepositoryImpl
import com.vanigent.meetingapp.domain.repository.MeetingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesMeetingRepository(): MeetingRepository = MeetingRepositoryImpl()

}