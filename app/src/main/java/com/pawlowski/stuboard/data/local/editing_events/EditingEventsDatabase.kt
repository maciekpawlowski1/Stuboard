package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FullEventEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]

)
abstract class EditingEventsDatabase: RoomDatabase() {
    abstract fun editingEventsDao(): EditingEventsDao
}