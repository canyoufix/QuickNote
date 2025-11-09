package com.canyoufix.quicknote.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canyoufix.quicknote.domain.Note
import com.canyoufix.quicknote.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@HiltViewModel
@OptIn(FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class ListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val searchQueryDeleted = MutableStateFlow("")
    fun onSearchQueryChanged(query: String){
        searchQuery.value = query
    }
    fun onSearchQueryDeletedChanged(query: String){
        searchQueryDeleted.value = query
    }

    val notes: Flow<List<Note>> = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                noteRepository.searchNotes(query)
            } else {
                noteRepository.getAllNotes()
            }
        }

    val deletedNotes: Flow<List<Note>> = searchQueryDeleted
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                noteRepository.searchDeletedNotes(query)
            } else {
                noteRepository.getAllDeletedNotes()
            }
        }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun togglePinNote(id: String, isPinned: Boolean){
        viewModelScope.launch {
            noteRepository.togglePinNote(id, isPinned)
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

    fun restoreDeletedNotes(deletedTime: Long){
        viewModelScope.launch {
            noteRepository.restoreDeletedNotes(deletedTime)
        }
    }
}