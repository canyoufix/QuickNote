package com.canyoufix.quicknote.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canyoufix.quicknote.data.datastore.NoteStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ListViewModel @Inject constructor(
    private val noteStorage: NoteStorage,
) : ViewModel() {

    val notes = noteStorage.getNotes()

    fun deleteNote(id: String) {
        viewModelScope.launch {
            noteStorage.deleteNote(id)
        }
    }
}