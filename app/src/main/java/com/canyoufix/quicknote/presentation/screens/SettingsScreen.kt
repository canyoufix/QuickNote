package com.canyoufix.quicknote.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.presentation.components.DefaultTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(){

    val snackbarHostState = remember{ SnackbarHostState() }

    val icon = painterResource(R.drawable.ic_settings)
    val title = stringResource(R.string.settings)

    Scaffold(
        topBar = {
            DefaultTopBar(
                icon,
                title,
                easterEgg = true,
                snackbarHostState = snackbarHostState
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

        }
    }
}