package com.canyoufix.quicknote.di

import android.content.Context
import androidx.room.Room
import com.canyoufix.quicknote.data.database.QuickNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideJson(): Json {
        return Json {
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): QuickNoteDatabase {
        return Room.databaseBuilder(
            context,
            QuickNoteDatabase::class.java,
            "QuickNoteDatabase",
        ).build()
    }
}