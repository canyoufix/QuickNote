package com.canyoufix.quicknote.domain

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Note(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val title: String,
    val content: String,
    val created_at: Long,
    val deleted_at: Long?,
    val is_visible: Boolean = true,
    val is_pinned: Boolean = false
)