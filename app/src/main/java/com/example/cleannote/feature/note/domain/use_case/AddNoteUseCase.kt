package com.example.cleannote.feature.note.domain.use_case

import com.example.cleannote.feature.note.domain.model.InvalidNoteException
import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.repository.NoteRepository

class AddNoteUseCase constructor(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {

        if(note.title.isBlank()) {
            throw InvalidNoteException("Title can't be empty")
        }
        if(note.content.isBlank()) {
            throw InvalidNoteException("Content can't be empty")
        }

        repository.addNote(note)
    }
}