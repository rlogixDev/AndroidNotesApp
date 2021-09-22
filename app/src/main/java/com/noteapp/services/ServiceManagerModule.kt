package com.noteapp.services

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceManagerModule {
    @Provides
    internal fun bind(impl: ServiceManager): ILoginServiceManage = impl
}