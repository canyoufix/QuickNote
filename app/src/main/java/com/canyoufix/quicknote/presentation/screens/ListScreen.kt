package com.canyoufix.quicknote.presentation.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.data.models.NoteFilter
import com.canyoufix.quicknote.domain.Note
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
    onEditClick: (Note) -> Unit,
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

    // Filter
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val filters = listOf(
        NoteFilter.Default,
        NoteFilter.New,
        NoteFilter.Old
    )

    // Scroll to top when apply filter
    val gridState = remember { LazyStaggeredGridState() }
    LaunchedEffect(selectedFilter) {
        gridState.scrollToItem(0)
    }

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
            LazyRow(
                contentPadding = innerPadding + PaddingValues(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter

                    Button(
                        onClick = { viewModel.setFilter(filter) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = when (filter) {
                                NoteFilter.Default -> stringResource(R.string.filter_default)
                                NoteFilter.New -> stringResource(R.string.filter_new)
                                NoteFilter.Old -> stringResource(R.string.filter_old)
                            }
                        )
                    }
                }
            }
            if (notes.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.list_empty),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalStaggeredGrid(
                    state = gridState,
                    columns = StaggeredGridCells.Adaptive(200.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = notes,
                    ) {
                        NoteCard(
                            note = it,
                            isSelected = it.id in selectedNotes,
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleSelection(it.id)
                                } else {
                                    onEditClick(it)
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
}

@Preview(device = Devices.PIXEL_9_PRO, showSystemUi = true)
@Composable
private fun ListScreen_Preview() {
    QuickNoteTheme {
        ListScreen(
            onAddClick = {},
            onEditClick = {}
        )
    }
}