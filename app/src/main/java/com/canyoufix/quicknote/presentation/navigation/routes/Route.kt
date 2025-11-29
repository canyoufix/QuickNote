package com.canyoufix.quicknote.presentation.navigation.routes

import androidx.navigation3.runtime.NavKey
import com.canyoufix.quicknote.domain.Note
import kotlinx.serialization.Serializable

@Serializable
sealed class Route : NavKey {

    @Serializable
    data object List : Route()

    @Serializable
    data class EditNote(val note: Note): Route()

    @Serializable
    data object NewNote : Route()

    @Serializable
    data object Trash : Route()

    @Serializable
    data object Settings : Route()
}