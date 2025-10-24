package com.canyoufix.quicknote.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.canyoufix.quicknote.ui.theme.QuickNoteTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val PrefsDataKey = stringSetPreferencesKey("data")

@Composable
fun ListScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = remember {
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("prefs")
        }
    }

    var text by remember { mutableStateOf("") }
    val textList by prefs.data
        .map { it[PrefsDataKey]?.toList() ?: emptyList() }
        .collectAsState(emptyList())

    val scope = rememberCoroutineScope()
    fun saveValue() {
        val keyedText = "" + System.currentTimeMillis() + "|$text"
        scope.launch {
            prefs.updateData {
                val prefs = it.toMutablePreferences()
                prefs[PrefsDataKey] = (textList + keyedText).toSet()
                prefs.toPreferences()
            }
        }
        text = ""
    }

    fun deleteValue() {
        scope.launch {
            prefs.updateData {
                val prefs = it.toMutablePreferences()
                //prefs[PrefsDataKey].
                prefs.toPreferences()
            }
        }
        text = ""
    }

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions { saveValue() },
                trailingIcon = {
                    TextButton(
                        onClick = { saveValue() },
                        content = { Text("Save") },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            LazyColumn {
                items(
                    items = textList,
                    key = { it.split("|", limit = 2).first() },
                ) {
                    ListItem(
                        headlineContent = {
                            Text(it.split("|", limit = 2).last())
                        },
                        trailingContent = {
                            Button(
                                onClick = {},
                                content = { Text("Delete") },
                            )
                        },
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
        ListScreen()
    }
}