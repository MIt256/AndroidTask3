package com.example.taskthree.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(indices = [Index(value = ["name"], unique = true)])
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String?,
    val phone: String?,
    val email: String?
)
