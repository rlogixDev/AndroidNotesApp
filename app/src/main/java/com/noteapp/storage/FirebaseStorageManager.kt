package com.noteapp.storage

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.noteapp.authentication.IFirebaseAuthenticationManager
import com.noteapp.dataclass.Notes
import com.noteapp.dataclass.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageManager
@Inject constructor(
    private val firebaseAuthenticationManager: IFirebaseAuthenticationManager
): IFirebaseStorageManager{
    val database = Firebase.database.reference
    companion object {
        public const val DB_USER  = "users"
        public const val DB_NOTES = "notes"
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
                result.value = DBReadResult.Success(snapShot.getValue() as Map<String, Notes>)
            }
            .addOnSuccessListener {
                result.value = DBReadResult.Fail
            }

        return result
    }
}