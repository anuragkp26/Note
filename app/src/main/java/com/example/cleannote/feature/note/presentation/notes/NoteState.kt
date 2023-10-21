package com.example.cleannote.feature.note.presentation.notes

import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.util.NoteOrder
import com.example.cleannote.feature.note.domain.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Ascending),
    val isOderUiVisible: Boolean = false
)
