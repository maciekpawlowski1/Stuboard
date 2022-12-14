package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FullEventEntity::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [



    ]

)
abstract class EditingEventsDatabase: RoomDatabase() {
    abstract fun editingEventsDao(): EditingEventsDao
}