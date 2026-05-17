package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canyoufix.quicknote.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    icon: Painter,
    title: String,
    easterEgg: Boolean = false,
    snackbarHostState: SnackbarHostState? = null
){

    val scope = rememberCoroutineScope()
    val easterEggMessage = stringResource(R.string.easter_egg)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (easterEgg && snackbarHostState?.currentSnackbarData == null){
                            scope.launch {
                                snackbarHostState?.showSnackbar(
                                    message = easterEggMessage
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp),
                    content = {
                        Icon(
                            contentDescription = null,
                            painter = icon,
                            tint = MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                )
            },
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    fontWeight = FontWeight.Medium,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(vertical = 4.dp)
        )
    }
}