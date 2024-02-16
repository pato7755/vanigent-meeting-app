package com.vanigent.meetingapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.vanigent.meetingapp.data.MeetingDao
import com.vanigent.meetingapp.data.MeetingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providesMeetingDao(db: MeetingDatabase): MeetingDao {
        val dao = db.meetingDao()
        Timber.e("dao - $dao")
        return dao
    }

    @Provides
    @Singleton
    fun providesMeetingDatabase(app: Application): MeetingDatabase {
//        val db: MeetingDatabase
//        try {
            val db = Room.databaseBuilder(
                context = app,
                MeetingDatabase::class.java,
                "meeting_database"
            ).build()
            Timber.e("context - $app")
            Timber.e("Database initialized successfully")
//        }
        return db
    }

}
