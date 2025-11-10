package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canyoufix.quicknote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,
    onSearch: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ){
        TopSearchBar(
            state = searchBarState,
            inputField = {
                SearchBarDefaults.InputField(
                    textFieldState = textFieldState,
                    searchBarState = searchBarState,
                    onSearch = { onSearch() },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    },
                    trailingIcon = {
                        if (textFieldState.text.isNotEmpty()){
                            IconButton(
                                onClick = {
                                    textFieldState.edit {
                                        replace(0, textFieldState.text.length, "")
                                    }
                                    focusManager.clearFocus()
                                },
                            ){
                                Icon(
                                    painterResource(R.drawable.ic_cancel),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(stringResource(R.string.search))
                    }
                )
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}