package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val username: String = "GuesserX",
    val emailOrPhone: String = "",
    val avatarIndex: Int = 0,
    val isGuest: Boolean = true,
    val totalPoints: Int = 0,
    val bestScore: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val gamesPlayed: Int = 0,
    val correctAnswers: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val lastPlayedDate: String = "",
    val dailyChallengePlayedDate: String = "",
    val dailyChallengeScore: Int = 0
) {
    val accuracyPercentage: Int
        get() = if (totalQuestionsAnswered > 0) {
            ((correctAnswers.toDouble() / totalQuestionsAnswered) * 100).toInt()
        } else {
            0
        }
}
