package com.example.taskthree.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ContactEntity::class], version = 1)
abstract class ContactDB : RoomDatabase() {
    abstract fun contactDao(): ContactDAO

}