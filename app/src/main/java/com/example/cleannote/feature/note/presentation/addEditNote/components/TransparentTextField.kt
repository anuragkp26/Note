package com.example.cleannote.feature.note.presentation.addEditNote.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TransparentTextField(
    _modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    textStyle: TextStyle = TextStyle(),
    isSingleLine: Boolean = false,
    onTextChanged : (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
) {


    Box(modifier = _modifier) {
        TextField(
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(text = hint) },
            singleLine = isSingleLine,
            textStyle = textStyle,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                textColor = Color.Black,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChanged(it)
                }
        )
    }

}