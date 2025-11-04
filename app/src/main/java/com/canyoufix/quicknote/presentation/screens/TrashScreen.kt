package com.canyoufix.quicknote.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.canyoufix.quicknote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(){
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()

    Scaffold(topBar = {
        BoxWithConstraints {
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
                            ){
                                Icon(painterResource(R.drawable.ic_search), contentDescription = null)
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
        }
    },) {

    }

}