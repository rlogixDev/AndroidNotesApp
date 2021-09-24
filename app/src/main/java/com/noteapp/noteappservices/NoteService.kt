package com.noteapp.noteappservices

import javax.inject.Inject
import javax.inject.Singleton

interface INoteService {
    public fun setmCurrentPhotoPath(value: String)
    public fun getmCurrentPhotoPath(): String
}

@Singleton
class NoteService
@Inject constructor(): INoteService {
    private var mCurrentPhotoPath: String = ""
    override fun setmCurrentPhotoPath(value: String) {
        mCurrentPhotoPath = value
    }

    override fun getmCurrentPhotoPath(): String {
        return mCurrentPhotoPath
    }
}