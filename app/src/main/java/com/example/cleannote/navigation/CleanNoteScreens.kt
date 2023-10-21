package com.example.cleannote.navigation

enum class CleanNoteScreens {
    NoteScreen,
    AddEditNoteScreen;
    companion object {
        fun fromRoute( route: String? ): CleanNoteScreens =
            when(route?.substringBefore("/")) {
                NoteScreen.name -> NoteScreen
                AddEditNoteScreen.name -> AddEditNoteScreen
                null -> NoteScreen
                else -> throw java.lang.IllegalArgumentException("Route $route is not recognised")

            }
    }
}