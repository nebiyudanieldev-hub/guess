package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mode: String, // "QUICK", "DAILY", "CATEGORY"
    val categoryName: String,
    val score: Int,
    val correctCount: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
