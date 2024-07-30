package com.example.contactapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    fun getContactsSortByName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact_table ORDER BY dateOfCreation ASC")
    fun getContactsSortByDate(): Flow<List<Contact>>
}