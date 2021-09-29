package com.noteapp.storage.di

import com.noteapp.storage.FirebaseStorageManager
import com.noteapp.storage.IFirebaseStorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseStorageModule {
    @Provides
    internal fun bind(impl: FirebaseStorageManager): IFirebaseStorageManager = impl
}