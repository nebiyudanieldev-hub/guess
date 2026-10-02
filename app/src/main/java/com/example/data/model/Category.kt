package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String,
    val displayOrder: Int = 0,
    val isActive: Boolean = true
)
