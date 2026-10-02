package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_configs")
data class LevelConfig(
    @PrimaryKey
    val level: Int,
    val name: String,
    val minPoints: Int
)
