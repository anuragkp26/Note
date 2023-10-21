package com.example.cleannote.feature.note.presentation.addEditNote


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleannote.feature.note.domain.model.InvalidNoteException
import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.use_case.NoteUseCases
import com.example.cleannote.feature.note.presentation.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        NoteTextFieldClass(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFieldClass> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldClass(
            hint = "Enter description..."
        )
    )
    val noteContent: State<NoteTextFieldClass> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    //for one time event
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1){
                viewModelScope.launch() {
                    noteUseCases.getNotByIdUseCase(noteId)?.also { note ->
                        currentNoteId = note.id

                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }

        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun onEvent(addEditEvent: AddEditEvent) {
        when (addEditEvent) {
            is AddEditEvent.AddNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                id = currentNoteId,
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = _noteColor.value
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(e.message ?: "Failed to save note")
                        )
                    }

                }
            }
            is AddEditEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !addEditEvent.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !addEditEvent.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = addEditEvent.content
                )
            }
            is AddEditEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = addEditEvent.title
                )
            }
            is AddEditEvent.SelectedColor -> {
                _noteColor.value = addEditEvent.color
            }
        }.exhaustive
    }

}

sealed class AddEditEvent {
    data class EnteredTitle(val title: String) : AddEditEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditEvent()
    data class EnteredContent(val content: String) : AddEditEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditEvent()
    data class SelectedColor(val color: Int) : AddEditEvent()
    object AddNote : AddEditEvent()
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object SaveNote : UiEvent()
}