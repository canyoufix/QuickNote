package com.canyoufix.quicknote.data.entities

sealed class NoteFilter {
    object Default : NoteFilter()

    object New : NoteFilter()

    object Old : NoteFilter()
}