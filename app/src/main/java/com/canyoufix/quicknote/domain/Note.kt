package com.canyoufix.quicknote.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Note(
    val id: String = Uuid.random().toString(),
    val text: String,
)