package com.canyoufix.quicknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.canyoufix.quicknote.presentation.navigation.QuickNoteNavigation
import com.canyoufix.quicknote.ui.theme.QuickNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickNoteTheme {
                QuickNoteNavigation()
            }
        }
    }
}