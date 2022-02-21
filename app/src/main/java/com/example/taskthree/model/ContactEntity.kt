package com.example.taskthree.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(indices = [Index(value = ["first_name"], unique = true)])
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String?,
    @ColumnInfo(name = "family_name")
    val familyName: String?,
    val phone: String?,
    val email: String?
)
class Name{
    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @ColumnInfo(name = "family_name")
    var familyName: String? = null
}
