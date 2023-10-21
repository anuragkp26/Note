package com.example.cleannote.feature.note.presentation.addEditNote.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleannote.feature.note.domain.model.Note
import com.example.cleannote.feature.note.presentation.addEditNote.AddEditEvent
import com.example.cleannote.feature.note.presentation.addEditNote.AddEditNoteViewModel
import com.example.cleannote.feature.note.presentation.addEditNote.UiEvent
import com.example.cleannote.feature.note.presentation.util.exhaustive
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val noteBackgroundAnimatable = remember {
        Animatable(Color(if (noteColor != -1) noteColor else viewModel.noteColor.value))
    }

    LaunchedEffect(key1 = true) {

        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.SaveNote -> navController.popBackStack()
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }.exhaustive
        }


    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditEvent.AddNote) },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        },
        scaffoldState = scaffoldState
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Note.noteColors.forEach { color ->

                    val colorInt = color.toArgb()

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(16.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) Color.Black else color,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditEvent.SelectedColor(colorInt))
                            },
                    )
                }
            }

            TransparentTextField(
                text = titleState.text,
                hint = titleState.hint,
                isHintVisible = titleState.isHintVisible,
                isSingleLine = true,
                onTextChanged = { viewModel.onEvent(AddEditEvent.EnteredTitle(it)) },
                onFocusChanged = { viewModel.onEvent(AddEditEvent.ChangeTitleFocus(it)) },
                _modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textStyle = MaterialTheme.typography.h5
            )

            TransparentTextField(
                text = contentState.text,
                hint = contentState.hint,
                isHintVisible = contentState.isHintVisible,
                onTextChanged = { viewModel.onEvent(AddEditEvent.EnteredContent(it)) },
                onFocusChanged = { viewModel.onEvent(AddEditEvent.ChangeContentFocus(it)) },
                _modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                textStyle = MaterialTheme.typography.body1

            )

        }
    }

}