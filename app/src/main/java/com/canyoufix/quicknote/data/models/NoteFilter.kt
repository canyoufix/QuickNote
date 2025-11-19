package com.canyoufix.quicknote.data.models

sealed class NoteFilter {

    object Default : NoteFilter()

    object New : NoteFilter()

    object Old : NoteFilter()
}