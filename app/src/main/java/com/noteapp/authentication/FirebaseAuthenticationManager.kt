package com.noteapp.authentication

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.noteapp.SignInFragment
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

    override suspend fun forgotPassword(emailId: String): Flow<Result> {
        val resultFlow: MutableStateFlow<Result> = MutableStateFlow(Result.IN_PROGRSS)
        auth.sendPasswordResetEmail(emailId).addOnCompleteListener { authresult->
            when{
                authresult.isSuccessful->resultFlow.value = Result.SUCCESS
                else->resultFlow.value = Result.FAIL
            }
        }
        return resultFlow
    }

    override suspend fun firebaseAuthWithGoogle(idToken: String):  Flow<Result> {
        val resultFlow: MutableStateFlow<Result> = MutableStateFlow(Result.IN_PROGRSS)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    resultFlow.value = Result.SUCCESS
                } else {
                    // If sign in fails, display a message to the user.
                    resultFlow.value = Result.FAIL
                }
            }
        return resultFlow
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid?:""
    }

    override fun getUserToken(): String {
        return auth.currentUser?.tenantId?:""
    }
}