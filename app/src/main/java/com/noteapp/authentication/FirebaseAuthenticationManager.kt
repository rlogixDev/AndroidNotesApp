package com.noteapp.authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthenticationManager
@Inject constructor():IFirebaseAuthenticationManager
{
    private val auth = Firebase.auth
    override suspend fun createAccount(emailId: String, password: String): Flow<Result> {

        val resultFlow: MutableStateFlow<Result> = MutableStateFlow(Result.IN_PROGRSS)
        auth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener { authresult->
            when{
                authresult.isSuccessful->resultFlow.value = Result.SUCCESS
                else->resultFlow.value = Result.FAIL
            }
        }
        return resultFlow
    }

    override suspend fun login(emailId: String, password: String): Flow<Result> {
        val resultFlow: MutableStateFlow<Result> = MutableStateFlow(Result.IN_PROGRSS)
        auth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener { authresult->
            when{
                authresult.isSuccessful->resultFlow.value = Result.SUCCESS
                else->resultFlow.value = Result.FAIL
            }
        }
        return resultFlow
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid?:""
    }
}