package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.Dao.NoteDao
import com.example.myapplication.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    fun insertNote(note: Note) = noteDao.insertNote(note)

    fun updateNote(note: Note) = noteDao.updateNote(note)

    fun deleteNote(note: Note) = noteDao.deleteNote(note)

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()
}