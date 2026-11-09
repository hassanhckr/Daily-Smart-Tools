package com.example.data.repository

import com.example.data.local.NoteDao
import com.example.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)

    suspend fun getNoteById(id: Long): NoteEntity? = noteDao.getNoteById(id)

    suspend fun insert(note: NoteEntity): Long = noteDao.insertNote(note)

    suspend fun update(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun delete(note: NoteEntity) = noteDao.deleteNote(note)

    suspend fun deleteById(id: Long) = noteDao.deleteNoteById(id)
}
