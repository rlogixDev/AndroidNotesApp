package com.noteapp.noteappservices.di

import com.noteapp.noteappservices.INoteService
import com.noteapp.noteappservices.NoteService
import com.noteapp.sharedPreferences.ISharedPreferenceManager
import com.noteapp.sharedPreferences.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object NoteModule {
    @Provides
    internal fun bind(impl: NoteService): INoteService = impl
}