package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Category
import com.example.ui.components.GuessBottomNavBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.PlayModeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GuessViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GuessAppRoot()
            }
        }
    }
}

@Composable
fun GuessAppRoot(
    viewModel: GuessViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val allCategoriesForAdmin by viewModel.allCategoriesForAdmin.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val levelConfigs by viewModel.levelConfigs.collectAsStateWithLifecycle()
    val adminStats by viewModel.adminStats.collectAsStateWithLifecycle()

    val isGameActive by viewModel.isGameActive.collectAsStateWithLifecycle()
    val isGameOver by viewModel.isGameOver.collectAsStateWithLifecycle()
    val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val sessionScore by viewModel.sessionScore.collectAsStateWithLifecycle()
    val timeRemainingSec by viewModel.timeRemainingSec.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val isAnswerRevealed by viewModel.isAnswerRevealed.collectAsStateWithLifecycle()
    val isAnswerCorrect by viewModel.isAnswerCorrect.collectAsStateWithLifecycle()
    val pointsEarnedThisStep by viewModel.pointsEarnedThisStep.collectAsStateWithLifecycle()
    val timeBonusEarned by viewModel.timeBonusEarned.collectAsStateWithLifecycle()
    val latestResult by viewModel.latestResult.collectAsStateWithLifecycle()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsStateWithLifecycle()

    val leaderboardTab by viewModel.leaderboardTab.collectAsStateWithLifecycle()
    val leaderboardEntries = viewModel.getLeaderboardEntries(leaderboardTab)
    val userTotalPoints = userProfile?.totalPoints ?: 0
    val levelName = viewModel.getLevelNameForPoints(userTotalPoints)
    val nextLevelInfo = viewModel.getNextLevelInfo(userTotalPoints)
    val achievements = viewModel.getAchievements()

    when {
        // Result Screen
        isGameOver && latestResult != null -> {
            ResultScreen(
                result = latestResult!!,
                onPlayAgainClick = {
                    if (latestResult!!.mode == "DAILY") {
                        viewModel.startDailyGuess()
                    } else {
                        viewModel.startQuickGuess()
                    }
                },
                onLeaderboardClick = {
                    viewModel.exitGameToHome()
                    viewModel.setScreenTab(ScreenTab.LEADERBOARD)
                },
                onHomeClick = {
                    viewModel.exitGameToHome()
                }
            )
        }

        // Active Quiz Gameplay Screen
        isGameActive && quizQuestions.isNotEmpty() -> {
            val currentQ = quizQuestions.getOrNull(currentQuestionIndex) ?: quizQuestions.first()
            QuizScreen(
                question = currentQ,
                questionIndex = currentQuestionIndex,
                totalQuestions = quizQuestions.size,
                currentScore = sessionScore,
                timeRemainingSec = timeRemainingSec,
                selectedOption = selectedOption,
                isAnswerRevealed = isAnswerRevealed,
                isAnswerCorrect = isAnswerCorrect,
                pointsEarnedThisStep = pointsEarnedThisStep,
                timeBonusEarned = timeBonusEarned,
                isSoundEnabled = isSoundEnabled,
                onToggleSound = { viewModel.toggleSound() },
                onOptionSelected = { option ->
                    viewModel.onAnswerSelected(option)
                },
                onExitGame = {
                    viewModel.exitGameToHome()
                }
            )
        }

        // Admin Screen
        currentTab == ScreenTab.ADMIN -> {
            AdminScreen(
                stats = adminStats,
                categories = allCategoriesForAdmin,
                questions = allQuestions,
                levelConfigs = levelConfigs,
                onAddQuestion = { catId, qText, qType, clue, ans, opA, opB, opC, opD, expl, diff, pts ->
                    viewModel.addQuestion(catId, qText, qType, clue, ans, opA, opB, opC, opD, expl, diff, pts)
                },
                onUpdateQuestion = { q -> viewModel.updateQuestion(q) },
                onDeleteQuestion = { q -> viewModel.deleteQuestion(q) },
                onToggleQuestionActive = { q -> viewModel.toggleQuestionActive(q) },
                onAddCategory = { id, name, emoji, desc -> viewModel.addCategory(id, name, emoji, desc) },
                onUpdateCategory = { cat -> viewModel.updateCategory(cat) },
                onDeleteCategory = { cat -> viewModel.deleteCategory(cat) },
                onToggleCategoryActive = { cat -> viewModel.toggleCategoryActive(cat) },
                onUpdateLevelConfig = { lvl, name, pts -> viewModel.updateLevelConfig(lvl, name, pts) },
                onBack = { viewModel.setScreenTab(ScreenTab.PROFILE) }
            )
        }

        // Primary App Navigation Scaffold (Home, Play, Leaderboard, Profile)
        else -> {
            BackHandler(enabled = currentTab != ScreenTab.HOME) {
                viewModel.setScreenTab(ScreenTab.HOME)
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    GuessBottomNavBar(
                        currentTab = currentTab,
                        onTabSelected = { tab -> viewModel.setScreenTab(tab) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (currentTab) {
                        ScreenTab.HOME -> {
                            HomeScreen(
                                userProfile = userProfile,
                                categories = categories,
                                onPlayNowClick = { viewModel.startQuickGuess() },
                                onDailyChallengeClick = { viewModel.startDailyGuess() },
                                onCategoryClick = { category -> viewModel.startCategoryGame(category) },
                                onNavigateTab = { tab -> viewModel.setScreenTab(tab) }
                            )
                        }

                        ScreenTab.PLAY -> {
                            PlayModeScreen(
                                categories = categories,
                                onQuickGuessClick = { viewModel.startQuickGuess() },
                                onDailyGuessClick = { viewModel.startDailyGuess() },
                                onCategoryClick = { category -> viewModel.startCategoryGame(category) }
                            )
                        }

                        ScreenTab.LEADERBOARD -> {
                            LeaderboardScreen(
                                currentTab = leaderboardTab,
                                entries = leaderboardEntries,
                                onTabSelected = { tab -> viewModel.setLeaderboardTab(tab) }
                            )
                        }

                        ScreenTab.PROFILE -> {
                            ProfileScreen(
                                userProfile = userProfile,
                                levelName = levelName,
                                nextLevelInfo = nextLevelInfo,
                                achievements = achievements,
                                isSoundEnabled = isSoundEnabled,
                                onToggleSound = { viewModel.toggleSound() },
                                onUpdateUsername = { newName -> viewModel.updateUsername(newName) },
                                onUpdateAvatar = { newIndex -> viewModel.updateAvatar(newIndex) },
                                onRegisterAccount = { user, contact -> viewModel.registerAccount(user, contact) },
                                onOpenAdminDashboard = { viewModel.setScreenTab(ScreenTab.ADMIN) }
                            )
                        }

                        ScreenTab.ADMIN -> {
                            // Handled in outer when branch
                        }
                    }
                }
            }
        }
    }
}
