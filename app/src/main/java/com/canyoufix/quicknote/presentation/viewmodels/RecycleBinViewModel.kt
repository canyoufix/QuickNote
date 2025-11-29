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
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RecycleBinViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    // Search in RecycleBin
    private val _searchQuery = MutableStateFlow("")

    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query
    }

    // All deleted notes
    val notes: Flow<List<Note>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                noteRepository.searchDeletedNotes(query)
            } else {
                noteRepository.getAllDeletedNotes()
            }
        }


    // Selection
    private val _selectedNotes = MutableStateFlow<Set<String>>(emptySet())
    val selectedNotes: StateFlow<Set<String>> = _selectedNotes.asStateFlow()

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


    // Delete notes
    fun deleteNote(id: String) {
        viewModelScope.launch {
            _deletedBuffer.add(id)
            noteRepository.deleteNote(id)
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


    // Restore notes
    fun restoreSelectedNotes() {
        viewModelScope.launch {
            _selectedNotes.value.forEach { id ->
                noteRepository.restoreDeletedNote(id)
            }
            clearSelection()
        }
    }


    // Pin / Unpin
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


    // Buffer for snackbar
    private val _deletedBuffer = mutableListOf<String>()
    fun clearDeletedBuffer() {
        _deletedBuffer.clear()
    }
    fun undoDelete(){
        viewModelScope.launch {
            _deletedBuffer.forEach { id ->
                noteRepository.restoreDeletedNote(id)
            }
            clearDeletedBuffer()
        }
    }
}