package com.example.cleannote.feature.note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleannote.feature.note.domain.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        val DATABASE_NAME = "Note_Room_Database"
    }
}