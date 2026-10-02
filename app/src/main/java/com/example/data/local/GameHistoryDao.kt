package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.GameHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllGameHistory(): Flow<List<GameHistory>>

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentGameHistory(limit: Int): List<GameHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameHistory(history: GameHistory): Long

    @Query("SELECT COUNT(*) FROM game_history")
    suspend fun getTotalGamesPlayed(): Int

    @Query("SELECT SUM(totalQuestions) FROM game_history")
    suspend fun getTotalQuestionsAnswered(): Int?

    @Query("SELECT categoryName, COUNT(*) as count FROM game_history GROUP BY categoryName ORDER BY count DESC LIMIT 1")
    suspend fun getMostPopularCategory(): MostPlayedCategory?
}

data class MostPlayedCategory(
    val categoryName: String,
    val count: Int
)
