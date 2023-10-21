package com.example.cleannote.feature.note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleannote.feature.note.presentation.notes.components.NoteScreen
import com.example.cleannote.navigation.CleanNoteNavigation
import com.example.cleannote.ui.theme.CleanNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           CleanNote {
                CleanNoteNavigation()
           }
        }
    }
}

@Composable
fun CleanNote( content: @Composable () -> Unit) {
    CleanNoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CleanNoteTheme {
        CleanNoteNavigation()
    }
}