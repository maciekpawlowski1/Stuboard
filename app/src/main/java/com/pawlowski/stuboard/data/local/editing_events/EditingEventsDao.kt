package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EditingEventsDao {

    @Query("SELECT * FROM editing_events")
    fun getAllEvents(): Flow<List<FullEventEntity>>

    @Query("SELECT * FROM editing_events WHERE id=:eventId LIMIT 1")
    suspend fun getEvent(eventId: Int): FullEventEntity

    @Query("SELECT * FROM editing_events WHERE id=:eventId LIMIT 1")
    fun observeEvent(eventId: Int): Flow<FullEventEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: FullEventEntity): Long

    @Delete
    suspend fun deleteEvent(event: FullEventEntity)

    @Query("DELETE FROM editing_events WHERE id IN (:eventsIds) AND publishingStatus = 0")
    suspend fun deleteEvents(eventsIds: List<Int>)

    @Transaction
    suspend fun runTransaction(action: suspend () -> Unit)
    {
        action()
    }
}