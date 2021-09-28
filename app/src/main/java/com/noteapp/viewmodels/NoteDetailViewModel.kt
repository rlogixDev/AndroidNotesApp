package com.noteapp.viewmodels

import androidx.lifecycle.ViewModel
import com.noteapp.noteappservices.NoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel
@Inject constructor(
    private val noteService: NoteService,
) : ViewModel(){

    fun setmCurrentPhotoPath(value: String) {
        noteService.setmCurrentPhotoPath(value)
    }

    fun getmCurrentPhotoPath(): String {
        return noteService.getmCurrentPhotoPath()
    }
}