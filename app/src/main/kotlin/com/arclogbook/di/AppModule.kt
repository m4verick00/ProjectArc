package com.arclogbook.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arclogbook.data.ArcLogbookDatabase
import com.arclogbook.data.LogEntryDao
import com.arclogbook.data.EncryptedNoteDao
import com.arclogbook.data.IpGeoDao
import com.arclogbook.data.TilePackDao
import com.arclogbook.data.MisinfoFlagDao
import com.arclogbook.data.PluginMetadataDao
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.arclogbook.network.IpGeoApi
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
        ).addMigrations(
            object: Migration(3,4){
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS misinfo_flags (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sourceId TEXT NOT NULL, sourceType TEXT NOT NULL, snippet TEXT NOT NULL, modelScore REAL NOT NULL, factScore REAL NOT NULL, combinedScore REAL NOT NULL, verdict TEXT NOT NULL, createdAt INTEGER NOT NULL)")
                }
            },
            object: Migration(4,5){
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS plugin_metadata (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, repoUrl TEXT NOT NULL, name TEXT NOT NULL, description TEXT NOT NULL, version TEXT NOT NULL, installedAt INTEGER NOT NULL, lastUpdatedAt INTEGER NOT NULL, enabled INTEGER NOT NULL, integrityHash TEXT NOT NULL)")
                }
            }
        ).build()

    @Provides
    fun provideLogEntryDao(db: ArcLogbookDatabase): LogEntryDao = db.logEntryDao()
    @Provides
    fun provideEncryptedNoteDao(db: ArcLogbookDatabase): EncryptedNoteDao = db.encryptedNoteDao()
    @Provides
    fun provideIpGeoDao(db: ArcLogbookDatabase): IpGeoDao = db.ipGeoDao()
    @Provides
    fun provideTilePackDao(db: ArcLogbookDatabase): TilePackDao = db.tilePackDao()
    @Provides
    fun provideMisinfoFlagDao(db: ArcLogbookDatabase): MisinfoFlagDao = db.misinfoFlagDao()
    @Provides
    fun providePluginMetadataDao(db: ArcLogbookDatabase): PluginMetadataDao = db.pluginMetadataDao()

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): androidx.work.WorkManager =
        androidx.work.WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideWorkManagerConfig(): androidx.work.Configuration =
        androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4))
            .build()
}
