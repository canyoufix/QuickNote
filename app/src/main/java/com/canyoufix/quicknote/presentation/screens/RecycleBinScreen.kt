package com.canyoufix.quicknote.presentation.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canyoufix.quicknote.extensions.plus
import com.canyoufix.quicknote.presentation.components.NoteCard
import com.canyoufix.quicknote.presentation.components.SearchTopBar
import com.canyoufix.quicknote.presentation.components.SelectionTopBar
import com.canyoufix.quicknote.presentation.viewmodels.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    viewModel: ListViewModel = hiltViewModel(),
) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelection()
        }
    }

    val textList by viewModel.deletedNotes.collectAsStateWithLifecycle(emptyList())
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()

    val selectedNotes by viewModel.selectedNotes.collectAsStateWithLifecycle()
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(textFieldState.text) {
        viewModel.onSearchQueryDeletedChanged(textFieldState.text as String)
    }

    Scaffold(
        topBar = {
            Crossfade(
                targetState = isSelectionMode
            ) { selecionMode ->
                if (selecionMode) {
                    SelectionTopBar(
                        isRecycleBin = true,
                        onBackClick = {
                            viewModel.clearSelection()
                        },
                        onUnpinClick = {
                            viewModel.unpinSelected()
                        },
                        onPinClick = {
                            viewModel.pinSelected()
                        },
                        onRestoreClick = {
                            viewModel.restoreSelectedNotes()
                        },
                        onDeleteClick = {
                            viewModel.deleteSelected()
                        },
                    )
                } else {
                    SearchTopBar(
                        textFieldState = textFieldState,
                        searchBarState = searchBarState,
                        onSearch = {
                            viewModel.onSearchQueryChanged(textFieldState.text.toString())
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        },
        ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            contentPadding = innerPadding + PaddingValues(16.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = textList,
                key = { it.id },
            ) {
                NoteCard(
                    note = it,
                    isSelected = it.id in selectedNotes,
                    onClick = {
                        if (isSelectionMode){
                            viewModel.toggleSelection(it.id)
                        } else {
                            // TODO
                        }
                    },
                    onLongClick = {
                        viewModel.toggleSelection(it.id)
                    }
                )
            }
        }
    }
}