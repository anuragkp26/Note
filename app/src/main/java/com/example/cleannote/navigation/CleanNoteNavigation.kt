package com.example.cleannote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleannote.feature.note.presentation.addEditNote.components.AddEditNoteScreen
import com.example.cleannote.feature.note.presentation.notes.components.NoteScreen

@Composable
fun CleanNoteNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CleanNoteScreens.NoteScreen.name) {

        composable(route = CleanNoteScreens.NoteScreen.name) {
            NoteScreen(navController = navController)
        }
        
        composable(
            route = CleanNoteScreens.AddEditNoteScreen.name +
                    "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { navBackStack ->
            AddEditNoteScreen(
                navController = navController,
                noteColor = navBackStack.arguments?.getInt("noteColor") ?: -1
            )
        }
    }
}