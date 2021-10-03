package com.noteapp.viewmodels

import androidx.lifecycle.ViewModel
import com.noteapp.dataclass.Notes
import com.noteapp.noteappservices.NoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel
@Inject constructor(
    private val noteService: NoteService,
) : ViewModel(){

    fun setmCurrentPhotoPath(value: String) = noteService.setmCurrentPhotoPath(value)
    fun getmCurrentPhotoPath(): String = noteService.getmCurrentPhotoPath()
    fun setNoteDetail(notes:Notes?) = noteService.setNoteDetail(notes)
    fun setIsNotesEditable(isEditable: Boolean) = noteService.setIsNotesEditable(isEditable)
    val noteDetailFlow: StateFlow<Notes?> = noteService.noteDetailFlow
    val isNotesEditable: StateFlow<Boolean> = noteService.isNotesEditable
}