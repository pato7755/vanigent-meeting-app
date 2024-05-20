package com.vanigent.meetingapp.di

import com.vanigent.meetingapp.data.CoordinatorDao
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
import com.vanigent.meetingapp.data.remote.RemoteApi
import com.vanigent.meetingapp.data.repository.EndpointNumberRepositoryImpl
import com.vanigent.meetingapp.data.repository.MeetingRepositoryImpl
import com.vanigent.meetingapp.domain.repository.CryptoManager
import com.vanigent.meetingapp.domain.repository.EndpointNumberRepository
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
    fun providesMeetingRepository(
        dao: MeetingDao,
        coordinatorDao: CoordinatorDao,
        remoteApi: RemoteApi,
        database: MeetingDatabase,
        cryptoManager: CryptoManager): MeetingRepository =
        MeetingRepositoryImpl(dao, coordinatorDao, remoteApi, database, cryptoManager)

    @Provides
    fun provideEndpointNumberRepository(): EndpointNumberRepository = EndpointNumberRepositoryImpl()

}