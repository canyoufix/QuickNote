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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
@OptIn(FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class ListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _searchQueryDeleted = MutableStateFlow("")
    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query
    }
    fun onSearchQueryDeletedChanged(query: String){
        _searchQueryDeleted.value = query
    }


    // Selection
    private val _selectedNotes = MutableStateFlow<Set<String>>(emptySet())
    val selectedNoted: StateFlow<Set<String>> = _selectedNotes.asStateFlow()

    val isSelectionMode: StateFlow<Boolean> = _selectedNotes
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleSelection(id: String){
        _selectedNotes.update { current ->
            if (id in current) current - id else current + id
        }
    }

    fun clearSelection(){
        _selectedNotes.value = emptySet()
    }

    fun softDeleteSelected(){
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                softDeleteNote(id)
            }
            clearSelection()
        }
    }

    fun deleteSelected(){
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                deleteNote(id)
            }
            clearSelection()
        }
    }

    fun pinSelected(){
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                noteRepository.pinNote(id)
            }
            clearSelection()
        }
    }

    fun unpinSelected(){
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                noteRepository.unpinNote(id)
            }
            clearSelection()
        }
    }

    val notes: Flow<List<Note>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                noteRepository.searchNotes(query)
            } else {
                noteRepository.getAllNotes()
            }
        }


    val deletedNotes: Flow<List<Note>> = _searchQueryDeleted
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



    fun softDeleteNote(id: String) {
        viewModelScope.launch {
            noteRepository.softDeleteNote(id, System.currentTimeMillis())
        }
    }

    fun restoreSelectedNotes(){
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                noteRepository.restoreDeletedNote(id)
            }
            clearSelection()
        }
    }

//    private val _selectedFilter = MutableStateFlow<NoteFilter>(NoteFilter.All)
//    val selectedFilter: StateFlow<NoteFilter> = _selectedFilter.asStateFlow()
//
//    fun onFilterSelected(filter: NoteFilter) {
//        _selectedFilter.value = filter
//    }


    fun getNote(id: String){
        viewModelScope.launch {
            noteRepository.getNoteById(id)
        }
    }

    fun returnNoteToList(id: String){
        viewModelScope.launch {
            noteRepository.restoreDeletedNote(id)
        }
    }

    fun restoreDeletedNotes(deletedTime: Long){
        viewModelScope.launch {
            noteRepository.restoreDeletedNotes(deletedTime)
        }
    }
}