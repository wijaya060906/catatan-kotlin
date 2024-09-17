package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Note
import com.example.myapplication.repository.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val allNotes: LiveData<List<Note>> = repository.getAllNotes()

    fun insertNote(note: Note) = repository.insertNote(note)

    fun updateNote(note: Note) = repository.updateNote(note)

    fun deleteNote(note: Note) = repository.deleteNote(note)
}