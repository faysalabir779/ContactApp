package com.example.contactapp.data.di

import android.app.Application
import androidx.room.Room
import com.example.contactapp.data.database.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun providerDatabase(application: Application): ContactDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ContactDatabase::class.java,
            "ContactApp.db"
        ).build()
    }
}