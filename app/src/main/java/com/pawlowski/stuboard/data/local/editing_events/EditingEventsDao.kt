package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EditingEventsDao {

    @Query("SELECT * FROM editing_events")
    fun getAllEvents(): Flow<List<FullEventEntity>>

    @Query("SELECT * FROM editing_events WHERE id=:eventId LIMIT 1")
    suspend fun getEvent(eventId: Int): FullEventEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: FullEventEntity): Long

    @Delete
    suspend fun deleteEvent(event: FullEventEntity)
}