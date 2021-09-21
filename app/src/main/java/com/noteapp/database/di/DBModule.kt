package com.noteapp.database.di

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.noteapp.database.DatabaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NoteDBModule {
    @Provides
    @NoteDBScope
    internal fun bind(helper: DatabaseHelper): SQLiteDatabase = helper.writableDatabase
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NoteDBScope