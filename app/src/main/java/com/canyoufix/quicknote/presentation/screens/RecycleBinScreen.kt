package com.canyoufix.quicknote.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.extensions.plus
import com.canyoufix.quicknote.presentation.components.NoteItemRecycleBin
import com.canyoufix.quicknote.presentation.viewmodels.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    viewModel: ListViewModel = hiltViewModel(),
) {
    val textList by viewModel.deletedNotes.collectAsStateWithLifecycle(emptyList())
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()

    Scaffold(
        topBar = {
            TopSearchBar(
                state = searchBarState,
                inputField = {
                    SearchBarDefaults.InputField(
                        textFieldState = textFieldState,
                        searchBarState = searchBarState,
                        onSearch = { TODO() },
                        trailingIcon = {
                            IconButton(
                                onClick = { TODO() },
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_search),
                                    contentDescription = null
                                )
                            }
                        },
                        placeholder = {
                            Text(stringResource(R.string.search))
                        }
                    )
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            searchBarState.currentValue

                        })
                    },
            )
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
                NoteItemRecycleBin(
                    note = it,
                    onSwipeLeft = { viewModel.deleteNote(it.id) },
                    onSwipeRight = { viewModel.returnNoteToList(it.id) }
                )
            }
        }
    }
}