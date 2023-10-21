package com.example.cleannote.feature.note.data.data_source

import androidx.room.*
import com.example.cleannote.feature.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM tbl_note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM tbl_note WHERE id= :id")
    suspend fun getNoteById(id: Int) : Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note : Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}