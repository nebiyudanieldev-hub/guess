package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id DESC")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY RANDOM()")
    suspend fun getAllActiveQuestionsRandom(): List<Question>

    @Query("SELECT * FROM questions WHERE isActive = 1 AND categoryId = :categoryId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getQuestionsByCategoryRandom(categoryId: String, limit: Int): List<Question>

    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<Question>

    @Query("SELECT * FROM questions WHERE isActive = 1 ORDER BY id ASC")
    suspend fun getAllActiveQuestionsOrdered(): List<Question>

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId")
    fun getQuestionsByCategory(categoryId: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: Long): Question?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Question>)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Long)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE isActive = 1")
    suspend fun getActiveQuestionCount(): Int

    @Query("SELECT categoryId, COUNT(*) as count FROM questions GROUP BY categoryId ORDER BY count DESC")
    suspend fun getQuestionCountByCategory(): List<CategoryQuestionCount>
}

data class CategoryQuestionCount(
    val categoryId: String,
    val count: Int
)
