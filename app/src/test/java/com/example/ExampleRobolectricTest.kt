package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DatabaseInitializer
import com.example.data.model.AchievementManager
import com.example.data.model.GameHistory
import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("GUESS!", appName)
    }

    @Test
    fun `default categories contain 15 items`() {
        val categories = DatabaseInitializer.defaultCategories
        assertEquals(15, categories.size)
        assertTrue(categories.any { it.id == "countries" })
        assertTrue(categories.any { it.id == "ethiopia" })
        assertTrue(categories.any { it.id == "cars" })
    }

    @Test
    fun `default questions contain answers that match one of the options`() {
        val questions = DatabaseInitializer.getInitialQuestions()
        assertTrue(questions.isNotEmpty())
        for (q in questions) {
            val options = q.getOptions()
            assertTrue("Question '${q.questionText}' answer should be in options", options.contains(q.answer))
        }
    }

    @Test
    fun `achievement unlocks properly when conditions met`() {
        val profile = UserProfile(
            totalPoints = 1200,
            currentStreak = 7,
            correctAnswers = 10,
            bestScore = 1000
        )
        val history = listOf(
            GameHistory(
                mode = "DAILY",
                categoryName = "Countries",
                score = 1000,
                correctCount = 10,
                totalQuestions = 10,
                timeTakenSeconds = 35
            )
        )
        val achievements = AchievementManager.evaluateAchievements(profile, history)

        val firstGuess = achievements.first { it.id == "first_guess" }
        val streak7 = achievements.first { it.id == "streak_7" }
        val perfect10 = achievements.first { it.id == "perfect_10" }
        val speedDemon = achievements.first { it.id == "speed_demon" }
        val points1000 = achievements.first { it.id == "points_1000" }

        assertTrue(firstGuess.isUnlocked)
        assertTrue(streak7.isUnlocked)
        assertTrue(perfect10.isUnlocked)
        assertTrue(speedDemon.isUnlocked)
        assertTrue(points1000.isUnlocked)
    }
}
