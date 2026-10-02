package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.DatabaseInitializer
import com.example.data.local.GameHistoryDao
import com.example.data.local.LevelConfigDao
import com.example.data.local.MostPlayedCategory
import com.example.data.local.QuestionDao
import com.example.data.local.UserProfileDao
import com.example.data.model.Category
import com.example.data.model.GameHistory
import com.example.data.model.LevelConfig
import com.example.data.model.Question
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GuessRepository(
    private val categoryDao: CategoryDao,
    private val questionDao: QuestionDao,
    private val userProfileDao: UserProfileDao,
    private val gameHistoryDao: GameHistoryDao,
    private val levelConfigDao: LevelConfigDao
) {

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val catCount = categoryDao.getCategoryCount()
        if (catCount == 0) {
            categoryDao.insertAll(DatabaseInitializer.defaultCategories)
        }

        val questionCount = questionDao.getQuestionCount()
        if (questionCount == 0) {
            questionDao.insertAll(DatabaseInitializer.getInitialQuestions())
        }

        val profile = userProfileDao.getUserProfileOnce()
        if (profile == null) {
            userProfileDao.insertProfile(
                UserProfile(
                    id = 1,
                    username = "SuperGuesser",
                    emailOrPhone = "",
                    avatarIndex = 0,
                    isGuest = true,
                    totalPoints = 420,
                    bestScore = 650,
                    currentStreak = 2,
                    longestStreak = 4,
                    gamesPlayed = 3,
                    correctAnswers = 24,
                    totalQuestionsAnswered = 30,
                    lastPlayedDate = getYesterdayDateString()
                )
            )
        }

        val levels = levelConfigDao.getAllLevelConfigsOnce()
        if (levels.isEmpty()) {
            levelConfigDao.insertAll(DatabaseInitializer.defaultLevels)
        }
    }

    // Categories
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    fun getActiveCategories(): Flow<List<Category>> = categoryDao.getActiveCategories()
    suspend fun insertCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(category)
    }
    suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.updateCategory(category)
    }
    suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategory(category)
    }

    // Questions
    fun getAllQuestions(): Flow<List<Question>> = questionDao.getAllQuestions()
    suspend fun getQuickGuessQuestions(limit: Int = 10): List<Question> = withContext(Dispatchers.IO) {
        val questions = questionDao.getRandomQuestions(limit)
        if (questions.isNotEmpty()) questions else DatabaseInitializer.getInitialQuestions().take(limit)
    }

    suspend fun getCategoryQuestions(categoryId: String, limit: Int = 10): List<Question> = withContext(Dispatchers.IO) {
        val questions = questionDao.getQuestionsByCategoryRandom(categoryId, limit)
        if (questions.isNotEmpty()) questions else questionDao.getRandomQuestions(limit)
    }

    suspend fun getDailyGuessQuestions(limit: Int = 10): List<Question> = withContext(Dispatchers.IO) {
        val all = questionDao.getAllActiveQuestionsOrdered()
        if (all.isEmpty()) return@withContext DatabaseInitializer.getInitialQuestions().take(limit)

        // Seed deterministically based on date (day of year + year)
        val cal = Calendar.getInstance()
        val seed = cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR)
        val rng = java.util.Random(seed.toLong())
        all.shuffled(rng).take(limit)
    }

    suspend fun insertQuestion(question: Question): Long = withContext(Dispatchers.IO) {
        questionDao.insertQuestion(question)
    }
    suspend fun updateQuestion(question: Question) = withContext(Dispatchers.IO) {
        questionDao.updateQuestion(question)
    }
    suspend fun deleteQuestion(question: Question) = withContext(Dispatchers.IO) {
        questionDao.deleteQuestion(question)
    }

    // User Profile & Streaks
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun updateUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userProfileDao.updateProfile(profile)
    }

    suspend fun recordGameCompletion(
        mode: String,
        categoryName: String,
        score: Int,
        correctCount: Int,
        totalQuestions: Int,
        timeTakenSeconds: Int
    ): UserProfile = withContext(Dispatchers.IO) {
        val currentProfile = userProfileDao.getUserProfileOnce() ?: UserProfile()
        val today = getTodayDateString()
        val yesterday = getYesterdayDateString()

        val newStreak = when {
            currentProfile.lastPlayedDate == today -> currentProfile.currentStreak
            currentProfile.lastPlayedDate == yesterday -> currentProfile.currentStreak + 1
            else -> 1
        }
        val longestStreak = maxOf(currentProfile.longestStreak, newStreak)

        val updatedProfile = currentProfile.copy(
            totalPoints = currentProfile.totalPoints + score,
            bestScore = maxOf(currentProfile.bestScore, score),
            currentStreak = newStreak,
            longestStreak = longestStreak,
            gamesPlayed = currentProfile.gamesPlayed + 1,
            correctAnswers = currentProfile.correctAnswers + correctCount,
            totalQuestionsAnswered = currentProfile.totalQuestionsAnswered + totalQuestions,
            lastPlayedDate = today,
            dailyChallengePlayedDate = if (mode == "DAILY") today else currentProfile.dailyChallengePlayedDate,
            dailyChallengeScore = if (mode == "DAILY") score else currentProfile.dailyChallengeScore
        )
        userProfileDao.updateProfile(updatedProfile)

        gameHistoryDao.insertGameHistory(
            GameHistory(
                mode = mode,
                categoryName = categoryName,
                score = score,
                correctCount = correctCount,
                totalQuestions = totalQuestions,
                timeTakenSeconds = timeTakenSeconds
            )
        )

        updatedProfile
    }

    // Level Configs
    fun getAllLevelConfigs(): Flow<List<LevelConfig>> = levelConfigDao.getAllLevelConfigs()
    suspend fun updateLevelConfig(levelConfig: LevelConfig) = withContext(Dispatchers.IO) {
        levelConfigDao.updateLevel(levelConfig)
    }

    // Game History & Analytics for Admin
    fun getAllGameHistory(): Flow<List<GameHistory>> = gameHistoryDao.getAllGameHistory()
    suspend fun getTotalGamesPlayed(): Int = withContext(Dispatchers.IO) { gameHistoryDao.getTotalGamesPlayed() }
    suspend fun getMostPopularCategory(): MostPlayedCategory? = withContext(Dispatchers.IO) {
        gameHistoryDao.getMostPopularCategory()
    }
    suspend fun getQuestionCount(): Int = withContext(Dispatchers.IO) { questionDao.getQuestionCount() }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
