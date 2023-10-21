package com.example.cleannote.di

import com.example.cleannote.feature.note.data.data_source.NoteDao
import com.example.cleannote.feature.note.data.repository.NoteRepositoryImpl
import com.example.cleannote.feature.note.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao) : NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }
}