package com.example.cleannote.feature.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleannote.ui.theme.*
import javax.annotation.Nonnull

@Entity(tableName = "tbl_note")
data class Note(

    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String): Exception(message)
