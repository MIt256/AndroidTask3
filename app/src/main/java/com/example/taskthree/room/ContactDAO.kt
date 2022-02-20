package com.example.taskthree.room

import androidx.room.*
import com.example.taskthree.Contact

@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contactEntity: ContactEntity)

    @Query("SELECT * FROM ContactEntity")
    fun getContacts(): List<ContactEntity>
}