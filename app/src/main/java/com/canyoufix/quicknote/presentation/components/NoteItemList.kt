package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.domain.Note
import kotlinx.coroutines.launch

@Composable
fun NoteItemList(
    note: Note,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    isPinned: Boolean,
    modifier: Modifier = Modifier,
) {
    val state = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()


    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            Box(
                contentAlignment = when (state.dismissDirection) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                },
                modifier = Modifier.fillMaxSize(),
            ) {
                if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                    if (!isPinned) {
                        Icon(painterResource(R.drawable.ic_pin), contentDescription = null, tint = Color.Green)
                    } else {
                        Icon(painterResource(R.drawable.ic_unpin), contentDescription = null, tint = Color.Yellow)
                    }
                } else if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Icon(painterResource(R.drawable.ic_delete), contentDescription = null, tint = Color.Red)
                }
            }
        },
        onDismiss = {
            when (state.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onSwipeRight()
                    scope.launch {
                        state.snapTo(SwipeToDismissBoxValue.Settled)
                    }
                }
                SwipeToDismissBoxValue.EndToStart -> onSwipeLeft()
                else -> {}
            }
        },
        modifier = modifier,
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = note.title,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = note.content,
                    fontSize = 12.sp
                )
            }
        }
    }
}