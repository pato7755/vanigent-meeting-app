package com.vanigent.meetingapp.di

import com.vanigent.meetingapp.domain.repository.MeetingRepository
import com.vanigent.meetingapp.domain.usecase.LoginUseCase
import com.vanigent.meetingapp.domain.usecase.SaveAttendeeUseCase
import com.vanigent.meetingapp.domain.usecase.SaveMeetingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSaveAttendeeUseCase(repository: MeetingRepository) =
        SaveAttendeeUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveMeetingUseCase(repository: MeetingRepository) =
        SaveMeetingUseCase(repository)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: MeetingRepository) =
        LoginUseCase(repository)


}
