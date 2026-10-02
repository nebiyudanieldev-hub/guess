package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuestionType {
    IMAGE,
    LOGO,
    FLAG,
    SILHOUETTE,
    BLURRED,
    EMOJI,
    MULTIPLE_CHOICE
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: String,
    val questionText: String,
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE,
    val visualClue: String = "",
    val answer: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val explanation: String = "",
    val difficulty: Difficulty = Difficulty.EASY,
    val points: Int = 100,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getOptions(): List<String> = listOf(optionA, optionB, optionC, optionD)
}
