package com.canyoufix.quicknote.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.canyoufix.quicknote.data.dao.NoteDao
import com.canyoufix.quicknote.data.entities.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class QuickNoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}