package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.domain.Note
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NoteCard(
    note: Note,
){
    val dateString = remember(note.created_at) {
        val noteDate = Calendar.getInstance().apply { timeInMillis = note.created_at }
        val today = Calendar.getInstance()

        val isToday = noteDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                noteDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)

        // Only time
        val format = if (isToday) {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            // Full date
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        }

        format.format(Date(note.created_at))
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),

    ) {
        Column(
            modifier = Modifier.padding(16.dp)

        ) {
            Text(
                text = note.title.ifBlank { stringResource(R.string.no_title) },
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.content.ifBlank { stringResource(R.string.no_content) },
                fontSize = 12.sp
            )
            Text(
                text = dateString,
                fontSize = 12.sp
            )
        }
    }
}