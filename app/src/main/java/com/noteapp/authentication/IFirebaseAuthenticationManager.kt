package com.noteapp.authentication

import kotlinx.coroutines.flow.Flow

interface IFirebaseAuthenticationManager{
    public suspend fun createAccount(emailId: String, password: String): Flow<Result>
    public suspend fun login(emailId: String, password: String) : Flow<Result>
    public suspend fun forgotPassword(emailId: String): Flow<Result>
    public fun getUserId(): String
}
public enum class Result {
    IN_PROGRSS, SUCCESS, FAIL
}