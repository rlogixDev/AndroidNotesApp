package com.noteapp.noteappservices

import com.noteapp.dataclass.Notes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface INoteService {
    public fun setmCurrentPhotoPath(value: String)
    public fun getmCurrentPhotoPath(): String
    public fun setNoteDetail(notes:Notes?)
    public fun setIsNotesEditable(isEditable:Boolean)
    public val noteDetailFlow: StateFlow<Notes?>
    public val isNotesEditable: StateFlow<Boolean>
}

@Singleton
class NoteService
@Inject constructor(): INoteService {
    private var mCurrentPhotoPath: String = ""
    private val _noteDetailFlow:MutableStateFlow<Notes?> = MutableStateFlow(null)
    override val noteDetailFlow: StateFlow<Notes?>
        get() = _noteDetailFlow
    private val _isNotesEditable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isNotesEditable: StateFlow<Boolean>
        get() = _isNotesEditable

    override fun setmCurrentPhotoPath(value: String) {
        mCurrentPhotoPath = value
    }

    override fun getmCurrentPhotoPath(): String {
        return mCurrentPhotoPath
    }

    override fun setNoteDetail(notes: Notes?) {
        _noteDetailFlow.value = notes
    }

    override fun setIsNotesEditable(isEditable: Boolean) {
        _isNotesEditable.value = isEditable
    }
}