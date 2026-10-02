package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.Difficulty
import com.example.data.model.QuestionType

class Converters {
    @TypeConverter
    fun fromQuestionType(value: QuestionType): String = value.name

    @TypeConverter
    fun toQuestionType(value: String): QuestionType {
        return try {
            QuestionType.valueOf(value)
        } catch (e: Exception) {
            QuestionType.MULTIPLE_CHOICE
        }
    }

    @TypeConverter
    fun fromDifficulty(value: Difficulty): String = value.name

    @TypeConverter
    fun toDifficulty(value: String): Difficulty {
        return try {
            Difficulty.valueOf(value)
        } catch (e: Exception) {
            Difficulty.EASY
        }
    }
}
