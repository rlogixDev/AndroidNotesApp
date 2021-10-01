package com.noteapp.storage

import android.net.Uri
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.dataclass.Notes
import com.noteapp.dataclass.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageManager
@Inject constructor(
    private val firebaseAuthenticationManager: IFirebaseAuthenticationManager
): IFirebaseStorageManager{
    val database = Firebase.database.reference
    val storageRef = FirebaseStorage.getInstance().reference
    companion object {
        public const val DB_USER  = "users"
        public const val DB_NOTES = "notes"
        public const val DB_IMAGE = "images"
    }
    override suspend fun writeNewUser(user: User): Flow<DBResult> {
        val result: MutableStateFlow<DBResult> = MutableStateFlow(DBResult.IN_PROGRESS)
        database.child(DB_USER)
            .child(firebaseAuthenticationManager.getUserId())
            .setValue(user)
            .addOnSuccessListener {
                result.value = DBResult.SUCCESS
            }.addOnFailureListener {
                result.value = DBResult.FAIL
            }
        return result
    }

    override suspend fun createNote(note: Notes): Flow<DBResult> {
        val result: MutableStateFlow<DBResult> = MutableStateFlow(DBResult.IN_PROGRESS)
        database.child(DB_NOTES).child(firebaseAuthenticationManager.getUserId())
            .push()
            .setValue(note)
            .addOnSuccessListener {
                result.value = DBResult.SUCCESS
            }.addOnFailureListener {
                result.value = DBResult.FAIL
            }
        return result
    }

    override suspend fun getUserNotes(): Flow<DBReadResult> {
        val result: MutableStateFlow<DBReadResult> = MutableStateFlow(DBReadResult.InProgress)
        database
            .child(DB_NOTES)
            .child(firebaseAuthenticationManager.getUserId())
            .get()
            .addOnSuccessListener { snapShot->
                try {
                    result.value = DBReadResult.Success(snapShot.getValue() as Map<String, Notes>)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .addOnFailureListener {
                result.value = DBReadResult.Fail
            }

        return result
    }

    override suspend fun deleteNote(note: Notes): Flow<DBResult> {
        val result: MutableStateFlow<DBResult> = MutableStateFlow(DBResult.IN_PROGRESS)
        database
            .child(DB_NOTES)
            .child(firebaseAuthenticationManager.getUserId())
            .child(note.id)
            .removeValue()
            .addOnSuccessListener {
                result.value = DBResult.SUCCESS
            }.addOnFailureListener {
                result.value = DBResult.FAIL
            }
        return result
    }

    override suspend fun updateNote(note: Notes): Flow<DBResult> {
        val result: MutableStateFlow<DBResult> = MutableStateFlow(DBResult.IN_PROGRESS)
        database
            .child(DB_NOTES)
            .child(firebaseAuthenticationManager.getUserId())
            .child(note.id)
            .setValue(note)
            .addOnSuccessListener {
                result.value = DBResult.SUCCESS
            }.addOnFailureListener {
                result.value = DBResult.FAIL
            }
        return result
    }

    override suspend fun uploadImage(file: File, fileName: String): Flow<DBResult> {
        val result: MutableStateFlow<DBResult> = MutableStateFlow(DBResult.IN_PROGRESS)
        val stream = FileInputStream(file)
        storageRef.child(DB_IMAGE)
            .child(fileName)
            .putStream(stream)
            .addOnSuccessListener {
                result.value = DBResult.SUCCESS
            }
            .addOnFailureListener {
                result.value = DBResult.FAIL
            }
        return result
    }

    override suspend fun getImagePath(fileName: String): String {
        val uri: Uri =   storageRef.child(DB_IMAGE)
            .child(fileName).downloadUrl.result
        return uri.path.toString()
    }
}