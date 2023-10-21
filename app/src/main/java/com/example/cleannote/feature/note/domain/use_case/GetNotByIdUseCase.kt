package com.example.cleannote.feature.note.domain.use_case

import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.repository.NoteRepository

class GetNotByIdUseCase constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int) : Note? {
        return repository.getNoteById(id)
    }
}