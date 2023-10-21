package com.example.cleannote.di

import com.example.cleannote.feature.note.domain.repository.NoteRepository
import com.example.cleannote.feature.note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideNoteUseCase( noteRepository: NoteRepository) =
        NoteUseCases(
            getNotesUseCase = GetNotesUseCase(noteRepository),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
            addNoteUseCase = AddNoteUseCase(noteRepository),
            getNotByIdUseCase = GetNotByIdUseCase(noteRepository)
        )
}