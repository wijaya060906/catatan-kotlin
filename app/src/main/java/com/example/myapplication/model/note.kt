package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,  // Providing default value to make creating new notes easier
    val title: String,
    val description: String,
    val picture: ByteArray? = null // Nullable field for an image
)
