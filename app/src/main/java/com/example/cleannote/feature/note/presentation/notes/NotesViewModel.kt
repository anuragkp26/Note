package com.example.cleannote.feature.note.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.use_case.NoteUseCases
import com.example.cleannote.feature.note.domain.util.NoteOrder
import com.example.cleannote.feature.note.domain.util.OrderType
import com.example.cleannote.feature.note.presentation.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _State = mutableStateOf(NoteState())
    val state: State<NoteState> = _State

    private var recentlyDeletedNote: Note? = null

    private var getNoteJob: Job? = null

    init {
        onEvent(NotesEvent.Order(NoteOrder.Date(OrderType.Descending)))
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun onEvent(notesEvent: NotesEvent) {
        when (notesEvent) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote = notesEvent.note
                    noteUseCases.deleteNoteUseCase(notesEvent.note)
                }
            }
            is NotesEvent.Order -> {

                Log.e("NOTELIST::", "NotesViewModel: NotesEvent.Order", )
                if (state.value.noteOrder::class == notesEvent.noteOrder::class &&
                    state.value.noteOrder.orderType == notesEvent.noteOrder.orderType
                ) {
                    return
                }

                Log.e("NOTELIST::", "NotesViewModel: NotesEvent.Order 2", )

                getNoteJob?.cancel()

                getNoteJob = noteUseCases.getNotesUseCase(notesEvent.noteOrder).onEach {
                    _State.value = state.value.copy(
                        notes = it,
                        noteOrder = notesEvent.noteOrder
                    )
                }.launchIn(viewModelScope)

            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote?.let {
                        noteUseCases.addNoteUseCase(it)
                        recentlyDeletedNote = null
                    }
                }
            }
            is NotesEvent.ToggleOrderUi -> {
                _State.value = state.value.copy(
                    isOderUiVisible = !state.value.isOderUiVisible
                )
            }
        }.exhaustive
    }

}

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderUi : NotesEvent()
}