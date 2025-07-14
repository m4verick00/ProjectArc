package com.arclogbook.di

import android.content.Context
import androidx.room.Room
import com.arclogbook.data.ArcLogbookDatabase
import com.arclogbook.data.LogEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArcLogbookDatabase =
        Room.databaseBuilder(
            context,
            ArcLogbookDatabase::class.java,
            "arclogbook_db"
        ).build()

    @Provides
    fun provideLogEntryDao(db: ArcLogbookDatabase): LogEntryDao = db.logEntryDao()
}
