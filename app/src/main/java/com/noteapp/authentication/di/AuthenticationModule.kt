package com.noteapp.authentication.di

import com.noteapp.authentication.FirebaseAuthenticationManager
import com.noteapp.authentication.IFirebaseAuthenticationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Provides
    internal fun bind(impl: FirebaseAuthenticationManager): IFirebaseAuthenticationManager = impl
}