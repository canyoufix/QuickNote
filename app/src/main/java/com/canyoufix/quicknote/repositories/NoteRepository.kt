package com.canyoufix.quicknote.repositories

import com.canyoufix.quicknote.data.database.QuickNoteDatabase
import com.canyoufix.quicknote.data.entities.NoteEntity
import com.canyoufix.quicknote.domain.Note
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository @Inject constructor(
    private val db: QuickNoteDatabase
) {
    fun getAllNotes(): Flow<List<Note>> =
        db.dao().getAllNotes().map { entities ->
            entities.map {
                it.toNote()
            }
        }

    fun getAllDeletedNotes(): Flow<List<Note>> =
        db.dao().getAllDeletedNotes().map { entities ->
            entities.map {
                it.toNote()
            }
        }

    suspend fun getNoteById(id: String){
        db.dao().getNoteById(id)
    }

    suspend fun addNote(title: String, content: String) {
        val note = Note(
            title = title,
            content = content,
            created_at = System.currentTimeMillis(),
            deleted_at = null
        )
        db.dao().addNote(note.toEntity())
    }

    suspend fun deleteNote(id: String){
        db.dao().deleteNoteById(id)
    }

    suspend fun softDeleteNote(id: String, time: Long){
        db.dao().softDeleteNote(id, time)
    }

    suspend fun returnNoteToList(id: String){
        db.dao().returnNoteToList(id)
    }

    suspend fun togglePinNote(id: String, isPinned: Boolean){
        db.dao().togglePinNote(id, isPinned)
    }

    fun searchNotes(query: String): Flow<List<Note>> =
        db.dao().searchNotes(query).map { entities ->
            entities.map{
                it.toNote()
            }
        }

    suspend fun restoreDeletedNotes(deletedTime: Long){
        db.dao().restoreDeletedNotes(deletedTime)
    }

    // Mappers
    private fun NoteEntity.toNote() = Note(
        id,
        title,
        content,
        created_at,
        deleted_at,
        is_visible,
        is_pinned
    )

    private fun Note.toEntity() = NoteEntity(
        id,
        title,
        content,
        created_at,
        deleted_at,
        is_visible,
        is_pinned
    )
}