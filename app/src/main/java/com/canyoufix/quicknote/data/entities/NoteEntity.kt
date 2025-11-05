package com.canyoufix.quicknote.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val title: String,
    val content: String,
    val created_at: Long,
    val deleted_at: Long?,
    val is_visible: Boolean = true
)