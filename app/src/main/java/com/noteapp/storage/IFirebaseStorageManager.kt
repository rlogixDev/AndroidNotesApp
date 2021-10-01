package com.noteapp.storage

import android.net.Uri
import com.noteapp.dataclass.Notes
import com.noteapp.dataclass.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IFirebaseStorageManager {
    public suspend fun writeNewUser(user: User): Flow<DBResult>
    public suspend fun createNote(note: Notes): Flow<DBResult>
    public suspend fun getUserNotes(): Flow<DBReadResult>
    public suspend fun deleteNote(note: Notes): Flow<DBResult>
    public suspend fun updateNote(note: Notes): Flow<DBResult>
    public suspend fun uploadImage(file: File, filenName: String): Flow<DBResult>
    public suspend fun getImagePath(filenName: String): String
}


public enum class DBResult {
    IN_PROGRESS, SUCCESS, FAIL
}

public sealed class DBReadResult {
    public object InProgress: DBReadResult()
    public object Fail      : DBReadResult()
    public data class Success(val result: Map<String, Any>): DBReadResult()
}