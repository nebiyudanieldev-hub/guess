package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DatabaseInitializer
import com.example.data.local.GuessDatabase
import com.example.data.model.Achievement
import com.example.data.model.AchievementManager
import com.example.data.model.Category
import com.example.data.model.Difficulty
import com.example.data.model.GameHistory
import com.example.data.model.LevelConfig
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.data.model.UserProfile
import com.example.data.repository.GuessRepository
import com.example.util.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    PLAY,
    LEADERBOARD,
    PROFILE,
    ADMIN
}

enum class LeaderboardTab {
    DAILY,
    WEEKLY,
    ALL_TIME
}

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val avatarIndex: Int,
    val points: Int,
    val levelName: String,
    val isCurrentUser: Boolean = false
)

data class GameResultSummary(
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val streak: Int,
    val timeTakenSeconds: Int,
    val categoryName: String,
    val mode: String
)

data class AdminStats(
    val totalUsers: Int = 124,
    val activeUsers: Int = 89,
    val gamesPlayed: Int = 0,
    val questionsAnswered: Int = 0,
    val mostPopularCategory: String = "Countries"
)

class GuessViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GuessDatabase.getDatabase(application)
    private val repository = GuessRepository(
        categoryDao = database.categoryDao(),
        questionDao = database.questionDao(),
        userProfileDao = database.userProfileDao(),
        gameHistoryDao = database.gameHistoryDao(),
        levelConfigDao = database.levelConfigDao()
    )

    private val soundManager = SoundManager.getInstance(application)
    val isSoundEnabled: StateFlow<Boolean> = soundManager.isSoundEnabled

    fun toggleSound(): Boolean = soundManager.toggleSound()
    fun playClickSound() = soundManager.playClick()

    // Current navigation tab
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Database flows
    val categories: StateFlow<List<Category>> = repository.getActiveCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategoriesForAdmin: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allQuestions: StateFlow<List<Question>> = repository.getAllQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val levelConfigs: StateFlow<List<LevelConfig>> = repository.getAllLevelConfigs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameHistory: StateFlow<List<GameHistory>> = repository.getAllGameHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Stats
    private val _adminStats = MutableStateFlow(AdminStats())
    val adminStats: StateFlow<AdminStats> = _adminStats.asStateFlow()

    // Game Session State
    private val _isGameActive = MutableStateFlow(false)
    val isGameActive: StateFlow<Boolean> = _isGameActive.asStateFlow()

    private val _gameMode = MutableStateFlow("QUICK")
    val gameMode: StateFlow<String> = _gameMode.asStateFlow()

    private val _activeCategory = MutableStateFlow<Category?>(null)
    val activeCategory: StateFlow<Category?> = _activeCategory.asStateFlow()

    private val _quizQuestions = MutableStateFlow<List<Question>>(emptyList())
    val quizQuestions: StateFlow<List<Question>> = _quizQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _sessionScore = MutableStateFlow(0)
    val sessionScore: StateFlow<Int> = _sessionScore.asStateFlow()

    private val _sessionCorrectCount = MutableStateFlow(0)
    val sessionCorrectCount: StateFlow<Int> = _sessionCorrectCount.asStateFlow()

    private val _sessionWrongCount = MutableStateFlow(0)
    val sessionWrongCount: StateFlow<Int> = _sessionWrongCount.asStateFlow()

    private val _timeRemainingSec = MutableStateFlow(10f)
    val timeRemainingSec: StateFlow<Float> = _timeRemainingSec.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption.asStateFlow()

    private val _isAnswerRevealed = MutableStateFlow(false)
    val isAnswerRevealed: StateFlow<Boolean> = _isAnswerRevealed.asStateFlow()

    private val _isAnswerCorrect = MutableStateFlow(false)
    val isAnswerCorrect: StateFlow<Boolean> = _isAnswerCorrect.asStateFlow()

    private val _pointsEarnedThisStep = MutableStateFlow(0)
    val pointsEarnedThisStep: StateFlow<Int> = _pointsEarnedThisStep.asStateFlow()

    private val _timeBonusEarned = MutableStateFlow(0)
    val timeBonusEarned: StateFlow<Int> = _timeBonusEarned.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _latestResult = MutableStateFlow<GameResultSummary?>(null)
    val latestResult: StateFlow<GameResultSummary?> = _latestResult.asStateFlow()

    // Leaderboard active tab
    private val _leaderboardTab = MutableStateFlow(LeaderboardTab.ALL_TIME)
    val leaderboardTab: StateFlow<LeaderboardTab> = _leaderboardTab.asStateFlow()

    private var timerJob: Job? = null
    private var gameStartTimeMs: Long = 0L

    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
            refreshAdminStats()
        }
    }

    fun setScreenTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setLeaderboardTab(tab: LeaderboardTab) {
        _leaderboardTab.value = tab
    }

    // GAMEPLAY LAUNCHERS
    fun startQuickGuess() {
        viewModelScope.launch {
            val questions = repository.getQuickGuessQuestions(10)
            launchGameSession(questions, mode = "QUICK", category = null)
        }
    }

    fun startDailyGuess() {
        viewModelScope.launch {
            val questions = repository.getDailyGuessQuestions(10)
            launchGameSession(questions, mode = "DAILY", category = null)
        }
    }

    fun startCategoryGame(category: Category) {
        viewModelScope.launch {
            val questions = repository.getCategoryQuestions(category.id, 10)
            launchGameSession(questions, mode = "CATEGORY", category = category)
        }
    }

    private fun launchGameSession(questions: List<Question>, mode: String, category: Category?) {
        if (questions.isEmpty()) return
        _quizQuestions.value = questions
        _currentQuestionIndex.value = 0
        _sessionScore.value = 0
        _sessionCorrectCount.value = 0
        _sessionWrongCount.value = 0
        _gameMode.value = mode
        _activeCategory.value = category
        _selectedOption.value = null
        _isAnswerRevealed.value = false
        _isAnswerCorrect.value = false
        _pointsEarnedThisStep.value = 0
        _timeBonusEarned.value = 0
        _isGameOver.value = false
        _isGameActive.value = true
        gameStartTimeMs = System.currentTimeMillis()

        startTimerForCurrentQuestion()
    }

    private fun startTimerForCurrentQuestion() {
        timerJob?.cancel()
        _timeRemainingSec.value = 10f
        _selectedOption.value = null
        _isAnswerRevealed.value = false
        _pointsEarnedThisStep.value = 0
        _timeBonusEarned.value = 0

        timerJob = viewModelScope.launch {
            val totalSteps = 100
            val stepDelay = 100L // 100ms * 100 = 10 seconds
            for (step in totalSteps downTo 1) {
                delay(stepDelay)
                _timeRemainingSec.value = step / 10f
                if (step == 30 || step == 20 || step == 10) {
                    soundManager.playTimer()
                }
            }
            // Time expired
            if (!_isAnswerRevealed.value) {
                onAnswerSelected("")
            }
        }
    }

    fun onAnswerSelected(option: String) {
        if (_isAnswerRevealed.value) return
        timerJob?.cancel()

        val currentQuestion = _quizQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        val isCorrect = option.equals(currentQuestion.answer, ignoreCase = true)
        val remaining = _timeRemainingSec.value

        _selectedOption.value = option
        _isAnswerRevealed.value = true
        _isAnswerCorrect.value = isCorrect

        if (isCorrect) {
            soundManager.playCorrect()
            val basePoints = currentQuestion.points
            val timeBonus = (remaining * 3).toInt().coerceIn(0, 30)
            val totalAward = basePoints + timeBonus

            _pointsEarnedThisStep.value = totalAward
            _timeBonusEarned.value = timeBonus
            _sessionScore.value += totalAward
            _sessionCorrectCount.value += 1
        } else {
            soundManager.playWrong()
            _pointsEarnedThisStep.value = 0
            _timeBonusEarned.value = 0
            _sessionWrongCount.value += 1
        }

        // Auto move to next question after 1.3 seconds
        viewModelScope.launch {
            delay(1300)
            nextQuestionOrFinish()
        }
    }

    fun nextQuestionOrFinish() {
        val nextIndex = _currentQuestionIndex.value + 1
        if (nextIndex < _quizQuestions.value.size) {
            _currentQuestionIndex.value = nextIndex
            startTimerForCurrentQuestion()
        } else {
            finishGame()
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        soundManager.playVictory()
        val totalTimeSec = ((System.currentTimeMillis() - gameStartTimeMs) / 1000).toInt().coerceAtLeast(1)
        val categoryName = _activeCategory.value?.name ?: if (_gameMode.value == "DAILY") "Daily Challenge" else "Quick Guess"

        val summary = GameResultSummary(
            score = _sessionScore.value,
            correctAnswers = _sessionCorrectCount.value,
            totalQuestions = _quizQuestions.value.size,
            streak = (userProfile.value?.currentStreak ?: 1),
            timeTakenSeconds = totalTimeSec,
            categoryName = categoryName,
            mode = _gameMode.value
        )
        _latestResult.value = summary
        _isGameOver.value = true

        viewModelScope.launch {
            val updated = repository.recordGameCompletion(
                mode = _gameMode.value,
                categoryName = categoryName,
                score = _sessionScore.value,
                correctCount = _sessionCorrectCount.value,
                totalQuestions = _quizQuestions.value.size,
                timeTakenSeconds = totalTimeSec
            )
            _latestResult.value = summary.copy(streak = updated.currentStreak)
            refreshAdminStats()
        }
    }

    fun exitGameToHome() {
        timerJob?.cancel()
        _isGameActive.value = false
        _isGameOver.value = false
        _currentTab.value = ScreenTab.HOME
    }

    // USER PROFILE ACTIONS
    fun updateUsername(newName: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: return@launch
            val updated = current.copy(username = newName.trim())
            repository.updateUserProfile(updated)
        }
    }

    fun updateAvatar(newIndex: Int) {
        viewModelScope.launch {
            val current = userProfile.value ?: return@launch
            val updated = current.copy(avatarIndex = newIndex)
            repository.updateUserProfile(updated)
        }
    }

    fun registerAccount(username: String, emailOrPhone: String) {
        viewModelScope.launch {
            val current = userProfile.value ?: return@launch
            val updated = current.copy(
                username = username.ifBlank { current.username },
                emailOrPhone = emailOrPhone,
                isGuest = false
            )
            repository.updateUserProfile(updated)
        }
    }

    // ADMIN ACTIONS
    fun addQuestion(
        categoryId: String,
        questionText: String,
        questionType: QuestionType,
        visualClue: String,
        answer: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        explanation: String,
        difficulty: Difficulty,
        points: Int
    ) {
        viewModelScope.launch {
            val newQ = Question(
                categoryId = categoryId,
                questionText = questionText,
                questionType = questionType,
                visualClue = visualClue,
                answer = answer,
                optionA = optionA,
                optionB = optionB,
                optionC = optionC,
                optionD = optionD,
                explanation = explanation,
                difficulty = difficulty,
                points = points,
                isActive = true
            )
            repository.insertQuestion(newQ)
            refreshAdminStats()
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            repository.updateQuestion(question)
        }
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            repository.deleteQuestion(question)
            refreshAdminStats()
        }
    }

    fun toggleQuestionActive(question: Question) {
        viewModelScope.launch {
            repository.updateQuestion(question.copy(isActive = !question.isActive))
        }
    }

    fun addCategory(id: String, name: String, iconEmoji: String, description: String) {
        viewModelScope.launch {
            val newCat = Category(
                id = id.lowercase().replace(" ", "_"),
                name = name,
                iconEmoji = iconEmoji.ifBlank { "⭐" },
                description = description,
                displayOrder = 99,
                isActive = true
            )
            repository.insertCategory(newCat)
            refreshAdminStats()
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            refreshAdminStats()
        }
    }

    fun toggleCategoryActive(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category.copy(isActive = !category.isActive))
        }
    }

    fun updateLevelConfig(level: Int, name: String, minPoints: Int) {
        viewModelScope.launch {
            repository.updateLevelConfig(LevelConfig(level, name, minPoints))
        }
    }

    private suspend fun refreshAdminStats() {
        val totalGames = repository.getTotalGamesPlayed()
        val popular = repository.getMostPopularCategory()?.categoryName ?: "Countries"
        val qCount = repository.getQuestionCount()
        _adminStats.value = AdminStats(
            totalUsers = 128,
            activeUsers = 94,
            gamesPlayed = totalGames,
            questionsAnswered = (totalGames * 10).coerceAtLeast(qCount),
            mostPopularCategory = popular
        )
    }

    // LEADERBOARD COMPUTATION
    fun getLeaderboardEntries(tab: LeaderboardTab): List<LeaderboardEntry> {
        val currentUser = userProfile.value ?: UserProfile()
        val userScore = when (tab) {
            LeaderboardTab.DAILY -> if (currentUser.dailyChallengeScore > 0) currentUser.dailyChallengeScore else (currentUser.totalPoints % 950 + 200)
            LeaderboardTab.WEEKLY -> (currentUser.totalPoints * 0.7).toInt().coerceAtLeast(450)
            LeaderboardTab.ALL_TIME -> currentUser.totalPoints
        }
        val userLevelName = getLevelNameForPoints(currentUser.totalPoints)

        // Predefined competitors with diverse scores
        val baseCompetitors = when (tab) {
            LeaderboardTab.DAILY -> listOf(
                LeaderboardEntry(1, "AbebeRunner", 2, 980, "Master"),
                LeaderboardEntry(2, "QuizQueen_Sara", 3, 940, "Expert"),
                LeaderboardEntry(3, "SpeedyGonzales", 1, 910, "Expert"),
                LeaderboardEntry(4, "TechSavvy99", 4, 880, "Expert"),
                LeaderboardEntry(5, "NairobiMaster", 5, 840, "Smart"),
                LeaderboardEntry(6, "FlagHunter", 0, 810, "Smart"),
                LeaderboardEntry(7, "RedDevil_Fan", 6, 760, "Smart"),
                LeaderboardEntry(8, "AlphaGeek", 7, 710, "Curious"),
                LeaderboardEntry(9, "TokyoDrifter", 2, 650, "Curious"),
                LeaderboardEntry(10, "TriviaTitan", 3, 600, "Curious")
            )
            LeaderboardTab.WEEKLY -> listOf(
                LeaderboardEntry(1, "QuizQueen_Sara", 3, 5840, "Master"),
                LeaderboardEntry(2, "AbebeRunner", 2, 5420, "Master"),
                LeaderboardEntry(3, "TechSavvy99", 4, 4980, "Expert"),
                LeaderboardEntry(4, "FlagHunter", 0, 4610, "Expert"),
                LeaderboardEntry(5, "SpeedyGonzales", 1, 4200, "Expert"),
                LeaderboardEntry(6, "NairobiMaster", 5, 3890, "Expert"),
                LeaderboardEntry(7, "RedDevil_Fan", 6, 3450, "Smart"),
                LeaderboardEntry(8, "TokyoDrifter", 2, 2980, "Smart"),
                LeaderboardEntry(9, "AlphaGeek", 7, 2410, "Smart"),
                LeaderboardEntry(10, "TriviaTitan", 3, 1950, "Curious")
            )
            LeaderboardTab.ALL_TIME -> listOf(
                LeaderboardEntry(1, "QuizQueen_Sara", 3, 14250, "Master"),
                LeaderboardEntry(2, "AbebeRunner", 2, 12890, "Master"),
                LeaderboardEntry(3, "TechSavvy99", 4, 11400, "Master"),
                LeaderboardEntry(4, "SpeedyGonzales", 1, 9850, "Master"),
                LeaderboardEntry(5, "FlagHunter", 0, 8920, "Master"),
                LeaderboardEntry(6, "NairobiMaster", 5, 7840, "Master"),
                LeaderboardEntry(7, "RedDevil_Fan", 6, 6590, "Expert"),
                LeaderboardEntry(8, "TokyoDrifter", 2, 5320, "Expert"),
                LeaderboardEntry(9, "AlphaGeek", 7, 4410, "Expert"),
                LeaderboardEntry(10, "TriviaTitan", 3, 3620, "Expert")
            )
        }

        // Insert current user into ranked list
        val userEntry = LeaderboardEntry(
            rank = 0,
            name = currentUser.username.ifBlank { "You" },
            avatarIndex = currentUser.avatarIndex,
            points = userScore,
            levelName = userLevelName,
            isCurrentUser = true
        )

        val combined = (baseCompetitors + userEntry).sortedByDescending { it.points }
        return combined.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }

    fun getLevelNameForPoints(points: Int): String {
        val configs = levelConfigs.value
        if (configs.isEmpty()) {
            return when {
                points >= 7000 -> "Master"
                points >= 3500 -> "Expert"
                points >= 1500 -> "Smart"
                points >= 500 -> "Curious"
                else -> "Beginner"
            }
        }
        val match = configs.sortedByDescending { it.minPoints }.firstOrNull { points >= it.minPoints }
        return match?.name ?: "Beginner"
    }

    fun getNextLevelInfo(points: Int): Pair<String, Float> {
        val configs: List<LevelConfig> = levelConfigs.value.sortedBy { it.minPoints }
        if (configs.isEmpty()) {
            val thresholds = listOf(0, 500, 1500, 3500, 7000)
            val names = listOf("Curious", "Smart", "Expert", "Master", "Legend")
            for (i in 0 until thresholds.size - 1) {
                val currentThreshold = thresholds[i]
                val nextThreshold = thresholds[i + 1]
                if (points in currentThreshold until nextThreshold) {
                    val progress = (points - currentThreshold).toFloat() / (nextThreshold - currentThreshold)
                    return Pair(names[i], progress)
                }
            }
            return Pair("Max Level reached!", 1f)
        }

        for (i in 0 until configs.size - 1) {
            val currentLvl = configs[i]
            val nextLvl = configs[i + 1]
            if (points in currentLvl.minPoints until nextLvl.minPoints) {
                val progress = (points - currentLvl.minPoints).toFloat() / (nextLvl.minPoints - currentLvl.minPoints)
                return Pair(nextLvl.name, progress)
            }
        }
        return Pair("Max Level reached!", 1f)
    }

    fun getAchievements(): List<Achievement> {
        val profile = userProfile.value ?: UserProfile()
        val history = gameHistory.value
        return AchievementManager.evaluateAchievements(profile, history)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
