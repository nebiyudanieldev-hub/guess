package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LevelConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelConfigDao {
    @Query("SELECT * FROM level_configs ORDER BY level ASC")
    fun getAllLevelConfigs(): Flow<List<LevelConfig>>

    @Query("SELECT * FROM level_configs ORDER BY level ASC")
    suspend fun getAllLevelConfigsOnce(): List<LevelConfig>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(levels: List<LevelConfig>)

    @Update
    suspend fun updateLevel(level: LevelConfig)
}
