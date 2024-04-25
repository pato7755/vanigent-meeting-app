package com.vanigent.meetingapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vanigent.meetingapp.data.local.entity.CoordinatorEntity

@Dao
interface CoordinatorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoordinatorDetails(coordinatorEntity: CoordinatorEntity)

    @Query("SELECT * FROM coordinator limit 1")
    suspend fun fetchCoordinatorDetails(): CoordinatorEntity
}