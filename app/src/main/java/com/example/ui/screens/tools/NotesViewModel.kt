package com.example.ui.screens.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NoteEntity
import com.example.data.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<NoteEntity> = emptyList(),
    val searchQuery: String = "",
    val activeNote: NoteEntity? = null,
    val isEditing: Boolean = false
)

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _activeNote = MutableStateFlow<NoteEntity?>(null)
    val activeNote: StateFlow<NoteEntity?> = _activeNote

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    val notesFlow: StateFlow<List<NoteEntity>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.allNotes
        } else {
            repository.searchNotes(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun openNoteForEdit(note: NoteEntity) {
        _activeNote.value = note
        _isEditing.value = true
    }

    fun openNewNote() {
        _activeNote.value = NoteEntity(title = "", content = "")
        _isEditing.value = true
    }

    fun closeEditor() {
        _activeNote.value = null
        _isEditing.value = false
    }

    fun saveNote(title: String, content: String, colorTag: Long) {
        viewModelScope.launch {
            val current = _activeNote.value
            if (current != null && current.id != 0L) {
                repository.update(
                    current.copy(
                        title = title.trim(),
                        content = content.trim(),
                        colorTag = colorTag,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } else {
                repository.insert(
                    NoteEntity(
                        title = title.trim(),
                        content = content.trim(),
                        colorTag = colorTag,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            closeEditor()
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
            if (_activeNote.value?.id == note.id) {
                closeEditor()
            }
        }
    }
}
