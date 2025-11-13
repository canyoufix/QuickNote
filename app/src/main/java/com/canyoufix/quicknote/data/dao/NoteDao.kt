package com.canyoufix.quicknote.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.canyoufix.quicknote.data.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("""
        SELECT * FROM notes 
        WHERE is_visible = 1 
        ORDER BY is_pinned DESC, created_at DESC
    """)
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE is_visible = 0 
        ORDER BY is_pinned DESC, created_at DESC
    """)
    fun getDeletedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNote(id: String): NoteEntity

    @Query("SELECT * FROM notes LIMIT :limit OFFSET :offset")
    suspend fun getNotesPaged(offset: Int, limit: Int): List<NoteEntity>

    @Insert
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Query("UPDATE notes " +
            "SET is_visible = 0, deleted_at = :time " +
            "WHERE id = :id")
    suspend fun softDeleteNote(id: String, time: Long)

    @Query("UPDATE notes " +
            "SET is_visible = 1, deleted_at = null " +
            "WHERE id = :id")
    suspend fun restoreDeletedNote(id: String)

    @Query("""
        SELECT * FROM notes
        WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')
        AND is_visible = 1
    """)
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes
        WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')
        AND is_visible = 0
    """)
    fun searchDeletedNotes(query: String): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes
        WHERE is_visible = 1
        ORDER BY created_at DESC
    """)
    fun getNotesNew(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes
        WHERE is_visible = 1
        ORDER BY created_at ASC
    """)
    fun getNotesOld(): Flow<List<NoteEntity>>

    @Query("UPDATE notes SET is_pinned = :pinned WHERE id = :id")
    suspend fun setPinnedNote(id: String, pinned: Boolean)

    @Query("UPDATE notes SET is_pinned = NOT is_pinned WHERE id = :id")
    suspend fun togglePinNote(id: String)
}