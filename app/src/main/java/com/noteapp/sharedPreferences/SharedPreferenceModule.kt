package com.noteapp.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.noteapp.sharedPreferences.ISharedPreferenceManager
import com.noteapp.sharedPreferences.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrainingStorageModule {
    @Provides
    internal fun bind(impl: SharedPreferenceManager): ISharedPreferenceManager = impl
}


@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceStorageModule {
    @Provides
    @SharedPreferenceStorageScope
    @Singleton
    internal fun providesSharedPreferenceStorage(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("Notes", Context.MODE_PRIVATE)
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SharedPreferenceStorageScope
