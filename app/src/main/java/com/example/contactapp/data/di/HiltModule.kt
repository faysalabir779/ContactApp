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
        ).fallbackToDestructiveMigration().build()

        //.fallbackToDestructiveMigration(), database er schema change
        // hoile eta use korte hoy. eta use korle ager shob data r schema delete hoye jay.
        //ei project e prothome image cilo na. but pore add kora hoice tai eta use korte hoice jeheto schema change hoice
    }
}