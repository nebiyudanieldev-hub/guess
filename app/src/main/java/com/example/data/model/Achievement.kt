package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val progress: Float = 1f,
    val progressText: String = ""
)

object AchievementManager {
    fun evaluateAchievements(
        profile: UserProfile,
        gameHistory: List<GameHistory>
    ): List<Achievement> {
        val totalPoints = profile.totalPoints
        val streak = profile.currentStreak
        val correct = profile.correctAnswers
        val gamesPlayed = profile.gamesPlayed
        val bestScore = profile.bestScore

        val countriesPlayed = gameHistory.count { it.categoryName.contains("Countries", ignoreCase = true) }
        val carsPlayed = gameHistory.count { it.categoryName.contains("Cars", ignoreCase = true) }
        val ethiopiaPlayed = gameHistory.count { it.categoryName.contains("Ethiopia", ignoreCase = true) }

        return listOf(
            Achievement(
                id = "first_guess",
                title = "First Guess",
                description = "Answer your first question correctly.",
                iconEmoji = "🏅",
                isUnlocked = correct >= 1,
                progress = (correct.coerceAtMost(1) / 1f),
                progressText = if (correct >= 1) "Unlocked" else "0/1"
            ),
            Achievement(
                id = "streak_7",
                title = "7-Day Streak",
                description = "Play for seven consecutive days.",
                iconEmoji = "🔥",
                isUnlocked = streak >= 7,
                progress = (streak.coerceAtMost(7) / 7f),
                progressText = "$streak/7 days"
            ),
            Achievement(
                id = "perfect_10",
                title = "Perfect 10",
                description = "Answer all ten questions in a single round correctly.",
                iconEmoji = "🎯",
                isUnlocked = gameHistory.any { it.correctCount >= 10 },
                progress = if (gameHistory.any { it.correctCount >= 10 }) 1f else (bestScore.coerceAtMost(1000) / 1000f),
                progressText = if (gameHistory.any { it.correctCount >= 10 }) "Unlocked" else "Score 10/10"
            ),
            Achievement(
                id = "speed_demon",
                title = "Speed Demon",
                description = "Complete a 10-question round in under 45 seconds.",
                iconEmoji = "⚡",
                isUnlocked = gameHistory.any { it.totalQuestions >= 10 && it.timeTakenSeconds in 1..45 },
                progress = if (gameHistory.any { it.totalQuestions >= 10 && it.timeTakenSeconds in 1..45 }) 1f else 0.5f,
                progressText = if (gameHistory.any { it.totalQuestions >= 10 && it.timeTakenSeconds in 1..45 }) "Unlocked" else "Fast round"
            ),
            Achievement(
                id = "points_1000",
                title = "1,000 Points",
                description = "Accumulate at least 1,000 total points.",
                iconEmoji = "🧠",
                isUnlocked = totalPoints >= 1000,
                progress = (totalPoints.coerceAtMost(1000) / 1000f),
                progressText = "$totalPoints / 1,000 pts"
            ),
            Achievement(
                id = "top_10",
                title = "Top 10",
                description = "Climb to the top 10 on the global leaderboard.",
                iconEmoji = "🏆",
                isUnlocked = totalPoints >= 1200,
                progress = (totalPoints.coerceAtMost(1200) / 1200f),
                progressText = if (totalPoints >= 1200) "Top 10 Rank!" else "Keep scoring"
            ),
            Achievement(
                id = "ethiopian_heritage",
                title = "Ethiopian Heritage",
                description = "Play rounds in the Ethiopia category.",
                iconEmoji = "🇪🇹",
                isUnlocked = ethiopiaPlayed >= 2,
                progress = (ethiopiaPlayed.coerceAtMost(2) / 2f),
                progressText = "$ethiopiaPlayed/2 rounds"
            ),
            Achievement(
                id = "world_explorer",
                title = "World Explorer",
                description = "Test your knowledge in Countries & Flags.",
                iconEmoji = "🌍",
                isUnlocked = countriesPlayed >= 2,
                progress = (countriesPlayed.coerceAtMost(2) / 2f),
                progressText = "$countriesPlayed/2 rounds"
            ),
            Achievement(
                id = "gearhead",
                title = "Gearhead",
                description = "Master the Cars category.",
                iconEmoji = "🚗",
                isUnlocked = carsPlayed >= 2,
                progress = (carsPlayed.coerceAtMost(2) / 2f),
                progressText = "$carsPlayed/2 rounds"
            )
        )
    }
}
