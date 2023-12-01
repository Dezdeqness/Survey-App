package com.dezdeqness.survey.di.modules

import com.dezdeqness.survey.core.CoroutineDispatcherProvider
import com.dezdeqness.survey.core.CoroutineDispatcherProviderImpl
import com.dezdeqness.survey.core.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutinesDispatcher(): CoroutineDispatcherProvider =
        CoroutineDispatcherProviderImpl()

    @Provides
    @Singleton
    fun provideTimeProvides() = TimeProvider()

}
