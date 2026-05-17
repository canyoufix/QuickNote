package com.canyoufix.quicknote.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.domain.Note
import com.canyoufix.quicknote.presentation.viewmodels.ListViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun EditNoteScreen(
    onBackClick: () -> Unit,
    note: Note,
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel()
) {

    // State
    val titleState = rememberTextFieldState()
    val contentState = rememberTextFieldState()

    LaunchedEffect(Unit) {
        titleState.edit {
            append(note.title)
        }

        contentState.edit {
            append(note.content)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        content = { Icon(painterResource(R.drawable.ic_close), contentDescription = null) },
                    )
                },
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            val title = titleState.text.toString()
                            val content = contentState.text.toString()

                            if (note.title == title && note.content == content){
                                onBackClick()
                            } else{
                                val editedNote = note.copy(
                                    title = title,
                                    content = content,
                                    created_at = System.currentTimeMillis()
                                )
                                viewModel.editNote(editedNote)
                                onBackClick()
                            }
                        },
                        content = { Icon(painterResource(R.drawable.ic_check), contentDescription = null) },
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextField(
                state = titleState,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                shape = RectangleShape,
                placeholder = { Text(stringResource(R.string.title)) },
                modifier = Modifier
                    .padding(innerPadding)
                    .wrapContentHeight()
            )
            TextField(
                state = contentState,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                shape = RectangleShape,
                placeholder = { Text(stringResource(R.string.what_to_note)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}