package com.example.cleannote.feature.note.presentation.notes.components

import android.content.ClipData.Item
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleannote.R
import com.example.cleannote.feature.note.domain.util.NoteOrder
import com.example.cleannote.feature.note.domain.util.OrderType
import com.example.cleannote.feature.note.presentation.notes.NotesEvent
import com.example.cleannote.feature.note.presentation.notes.NotesViewModel
import com.example.cleannote.navigation.CleanNoteScreens
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = CleanNoteScreens.AddEditNoteScreen.name) },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "ADd Note")
            }
        },
        scaffoldState = scaffoldState
    ) { pv ->
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(16.dp)
        ) {
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                
                Text(
                    text = "Your notes",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_sort),
                    contentDescription = "Sort",
                    modifier = Modifier.clickable {
                        viewModel.onEvent(NotesEvent.ToggleOrderUi)
                    }
                )
            }

            AnimatedVisibility(
                visible = state.isOderUiVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    OrderSection(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        noteOrder = state.noteOrder
                    ) {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    CleanNoteScreens.AddEditNoteScreen.name +
                                            "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                        note = note,
                    ) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                            val result = scaffoldState.snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )

                            if(result == SnackbarResult.ActionPerformed) {
                                viewModel.onEvent(NotesEvent.RestoreNote)
                            }
                        }
                    }
                }
            }
        }
    }
}