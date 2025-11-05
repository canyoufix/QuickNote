package com.canyoufix.quicknote.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.canyoufix.quicknote.data.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: String): NoteEntity

    @Query("SELECT * FROM notes WHERE is_visible = 1")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_visible = 0")
    fun getAllDeletedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes LIMIT :limit OFFSET :offset")
    suspend fun getNotesPaged(offset: Int, limit: Int): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE content LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("UPDATE notes " +
            "SET is_visible = 0, deleted_at = :time " +
            "WHERE id = :id")
    suspend fun softDeleteNote(id: String, time: Long)

    @Query("UPDATE notes " +
            "SET is_visible = 1, deleted_at = null " +
            "WHERE id = :id")
    suspend fun returnNoteToList(id: String)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: String)
}