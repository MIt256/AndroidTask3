package com.example.taskthree.room

import androidx.room.*

@Dao
interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contactEntity: ContactEntity)

    @Query("SELECT * FROM ContactEntity WHERE phone NOT NULL")
    fun getContacts(): List<ContactEntity>

    @Query("SELECT first_name,family_name FROM ContactEntity WHERE phone = :phoneNumber")
    fun getNameByNumber(phoneNumber:String?):Name
}