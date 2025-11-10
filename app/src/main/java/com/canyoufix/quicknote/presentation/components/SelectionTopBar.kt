package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.canyoufix.quicknote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(
    isRecycleBin: Boolean?,
    onBackClick: () -> Unit,
    onPinClick: () -> Unit,
    onUnpinClick: () -> Unit,
    onRestoreClick: () -> Unit = {},
    onDeleteClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TopAppBar(
            title = { stringResource(R.string.select_an_action) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = onUnpinClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_unpin),
                        contentDescription = null
                    )
                }
                IconButton(onClick = onPinClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pin),
                        contentDescription = null
                    )
                }
                if (isRecycleBin == true) {
                    IconButton(
                        onClick = {
                            onRestoreClick()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_return),
                            contentDescription = null
                        )
                    }
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            },
        )
    }
}