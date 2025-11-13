package com.canyoufix.quicknote.presentation.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.extensions.plus
import com.canyoufix.quicknote.presentation.components.NoteCard
import com.canyoufix.quicknote.presentation.components.SearchTopBar
import com.canyoufix.quicknote.presentation.components.SelectionTopBar
import com.canyoufix.quicknote.presentation.theme.QuickNoteTheme
import com.canyoufix.quicknote.presentation.viewmodels.ListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onAddClick: () -> Unit,
    viewModel: ListViewModel = hiltViewModel(),
) {

    // All notes
    val notes by viewModel.notes.collectAsStateWithLifecycle(emptyList())
    val selectedNotes by viewModel.selectedNotes.collectAsStateWithLifecycle()
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()

    // Snackbar
    val snackbarHostState = SnackbarHostState()
    val snackBarMessage = stringResource(R.string.note_deleted)
    val snackBarActionLabel = stringResource(R.string.cancel)

    // SearchBar
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()

    // Scope
    val scope = rememberCoroutineScope()

    // Focus manager
    val focusManager = LocalFocusManager.current

    // Update notes on search text change
    LaunchedEffect(textFieldState.text) {
        viewModel.onSearchQueryChanged(textFieldState.text as String)
    }

    // Clear selected notes
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelection()
        }
    }


    Scaffold(
        topBar = {
            Crossfade(
                targetState = isSelectionMode
            ) { selectionMode ->
                if (selectionMode) {
                    SelectionTopBar(
                        isRecycleBin = false,
                        onUnpinClick = {
                            viewModel.unpinSelected()
                        },
                        onBackClick = {
                            viewModel.clearSelection()
                        },
                        onPinClick = {
                            viewModel.pinSelected()
                        },
                        onDeleteClick = {
                            viewModel.softDeleteSelected()

                            if (snackbarHostState.currentSnackbarData == null) {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = snackBarMessage,
                                        actionLabel = snackBarActionLabel,
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )

                                    when(result){
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.undoDelete()
                                        }
                                        SnackbarResult.Dismissed -> {
                                            viewModel.clearDeletedBuffer()
                                        }
                                    }
                                }
                            }
                        }
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                content = { Icon(painterResource(R.drawable.ic_add), contentDescription = null) },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column {
//            FilterRow(
//                filters = filters,
//                selectedFilter = selectedFilter,
//                onFilterSelected = { selectedFilter = it }
//            )
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                contentPadding = innerPadding + PaddingValues(16.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = notes,
                    key = { it.id },
                ) {
                    NoteCard(
                        note = it,
                        isSelected = it.id in selectedNotes,
                        onClick = {
                            if (isSelectionMode) {
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
}

@Preview(device = Devices.PIXEL_9_PRO, showSystemUi = true)
@Composable
private fun ListScreen_Preview() {
    QuickNoteTheme {
        ListScreen(
            onAddClick = {},
        )
    }
}