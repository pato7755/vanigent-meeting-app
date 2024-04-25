package com.vanigent.meetingapp.di

import android.app.Application
import androidx.room.Room
import com.vanigent.meetingapp.data.CoordinatorDao
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providesMeetingDao(db: MeetingDatabase): MeetingDao = db.meetingDao()

    @Provides
    @Singleton
    fun providesCoordinatorDao(db: MeetingDatabase): CoordinatorDao = db.coordinatorDao()


    @Provides
    @Singleton
    fun providesMeetingDatabase(app: Application): MeetingDatabase =
        Room.databaseBuilder(
            context = app,
            MeetingDatabase::class.java,
            "meeting_database"
        ).fallbackToDestructiveMigration()
            .build()


}
