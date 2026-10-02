package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.Category
import com.example.data.model.GameHistory
import com.example.data.model.LevelConfig
import com.example.data.model.Question
import com.example.data.model.UserProfile

@Database(
    entities = [
        Category::class,
        Question::class,
        UserProfile::class,
        GameHistory::class,
        LevelConfig::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GuessDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun questionDao(): QuestionDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun gameHistoryDao(): GameHistoryDao
    abstract fun levelConfigDao(): LevelConfigDao

    companion object {
        @Volatile
        private var INSTANCE: GuessDatabase? = null

        fun getDatabase(context: Context): GuessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GuessDatabase::class.java,
                    "guess_game.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
