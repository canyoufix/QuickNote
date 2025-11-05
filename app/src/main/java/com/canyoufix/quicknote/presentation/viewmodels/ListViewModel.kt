package com.canyoufix.quicknote.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canyoufix.quicknote.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    val notes = noteRepository.getAllNotes()
    val deletedNotes = noteRepository.getAllDeletedNotes()

//    fun addNote(note: Note) {
//        viewModelScope.launch {
//            noteRepository.addNote(note)
//        }
//    }
    fun deleteNote(id: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun softDeleteNote(id: String, time: Long) {
        viewModelScope.launch {
            noteRepository.softDeleteNote(id, time)
        }
    }

    fun getNote(id: String){
        viewModelScope.launch {
            noteRepository.getNoteById(id)
        }
    }

    fun returnNoteToList(id: String){
        viewModelScope.launch {
            noteRepository.returnNoteToList(id)
        }
    }

}