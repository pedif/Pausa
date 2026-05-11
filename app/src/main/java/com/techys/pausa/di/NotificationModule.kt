package com.techys.pausa.di

import com.techys.core.notification.NotificationActionContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Singleton
    @Binds
    abstract fun bindNotificationActionContract(contractActionImpl: NotificationContractActionImpl): NotificationActionContract
}