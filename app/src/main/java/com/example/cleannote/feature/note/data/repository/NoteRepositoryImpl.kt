package com.example.cleannote.feature.note.data.repository

import com.example.cleannote.feature.note.data.data_source.NoteDao
import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl constructor(
    private val noteDao: NoteDao
) : NoteRepository {


    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id = id)
    }

    override suspend fun addNote(note: Note) {
        noteDao.addNote(note = note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note = note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note = note)
    }
}