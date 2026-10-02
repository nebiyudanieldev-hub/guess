package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Difficulty
import com.example.data.model.Question
import com.example.ui.components.ClueView
import com.example.ui.theme.AmberPoints
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessEmerald

@Composable
fun QuizScreen(
    question: Question,
    questionIndex: Int,
    totalQuestions: Int,
    currentScore: Int,
    timeRemainingSec: Float,
    selectedOption: String?,
    isAnswerRevealed: Boolean,
    isAnswerCorrect: Boolean,
    pointsEarnedThisStep: Int,
    timeBonusEarned: Int,
    isSoundEnabled: Boolean = true,
    onToggleSound: () -> Unit = {},
    onOptionSelected: (String) -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showQuitDialog = true
    }

    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = { Text("Quit Game?") },
            text = { Text("Your current quiz progress in this round will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        onExitGame()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorCrimson)
                ) {
                    Text("Quit")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showQuitDialog = false }) {
                    Text("Resume")
                }
            }
        )
    }

    val progressValue by animateFloatAsState(
        targetValue = (questionIndex + 1).toFloat() / totalQuestions,
        label = "quiz_progress"
    )

    val timerProgress by animateFloatAsState(
        targetValue = (timeRemainingSec / 10f).coerceIn(0f, 1f),
        label = "timer_progress"
    )

    val timerColor by animateColorAsState(
        targetValue = when {
            timeRemainingSec > 5f -> IndigoPrimary
            timeRemainingSec > 2.5f -> AmberPoints
            else -> ErrorCrimson
        },
        label = "timer_color"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("quiz_screen")
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Top Header: Close button, Question indicator, Score Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showQuitDialog = true },
                modifier = Modifier.testTag("quiz_exit_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quit Quiz",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier.testTag("quiz_sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = if (isSoundEnabled) "Mute Sound" else "Unmute Sound",
                        tint = if (isSoundEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Question counter
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${questionIndex + 1}/$totalQuestions",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Score Counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(AmberPoints.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Score",
                    tint = AmberPoints,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$currentScore",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = AmberPoints
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Bar for Round
        LinearProgressIndicator(
            progress = { progressValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = IndigoPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Timer Bar & Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Timer",
                    tint = timerColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${String.format("%.1f", timeRemainingSec)}s",
                    fontWeight = FontWeight.Bold,
                    color = timerColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Difficulty chip
            val diffColor = when (question.difficulty) {
                Difficulty.EASY -> SuccessEmerald
                Difficulty.MEDIUM -> AmberPoints
                Difficulty.HARD -> ErrorCrimson
            }
            Box(
                modifier = Modifier
                    .background(diffColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = question.difficulty.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = diffColor
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { timerProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = timerColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Clue Area
        ClueView(question = question)

        Spacer(modifier = Modifier.height(14.dp))

        // Question Text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answer Feedback Banner (CORRECT / WRONG)
        AnimatedVisibility(
            visible = isAnswerRevealed,
            enter = fadeIn() + slideInVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("answer_feedback_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAnswerCorrect) SuccessEmerald.copy(alpha = 0.18f) else ErrorCrimson.copy(alpha = 0.18f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(if (isAnswerCorrect) SuccessEmerald else ErrorCrimson, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAnswerCorrect) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isAnswerCorrect) "✓ CORRECT!" else "✕ WRONG",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = if (isAnswerCorrect) SuccessEmerald else ErrorCrimson
                        )
                    }

                    if (isAnswerCorrect) {
                        Text(
                            text = "+$pointsEarnedThisStep pts ${if (timeBonusEarned > 0) "(+$timeBonusEarned ⚡)" else ""}",
                            fontWeight = FontWeight.Bold,
                            color = SuccessEmerald,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "0 pts",
                            fontWeight = FontWeight.Bold,
                            color = ErrorCrimson,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // 4 Large Answer Options
        val options = question.getOptions()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                val optionLetter = when (index) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    else -> "D"
                }

                val isThisOptionSelected = selectedOption == option
                val isThisOptionTheCorrectAnswer = option.equals(question.answer, ignoreCase = true)

                val backgroundColor = when {
                    !isAnswerRevealed -> MaterialTheme.colorScheme.surface
                    isThisOptionTheCorrectAnswer -> SuccessEmerald.copy(alpha = 0.2f)
                    isThisOptionSelected && !isAnswerCorrect -> ErrorCrimson.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                }

                val borderColor = when {
                    !isAnswerRevealed -> MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    isThisOptionTheCorrectAnswer -> SuccessEmerald
                    isThisOptionSelected && !isAnswerCorrect -> ErrorCrimson
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                }

                val letterBgColor = when {
                    !isAnswerRevealed -> MaterialTheme.colorScheme.surfaceVariant
                    isThisOptionTheCorrectAnswer -> SuccessEmerald
                    isThisOptionSelected && !isAnswerCorrect -> ErrorCrimson
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                val letterTextColor = when {
                    !isAnswerRevealed -> MaterialTheme.colorScheme.onSurfaceVariant
                    isThisOptionTheCorrectAnswer -> Color.White
                    isThisOptionSelected && !isAnswerCorrect -> Color.White
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(backgroundColor)
                        .border(
                            width = if (isAnswerRevealed && (isThisOptionTheCorrectAnswer || isThisOptionSelected)) 2.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(enabled = !isAnswerRevealed) {
                            onOptionSelected(option)
                        }
                        .padding(horizontal = 16.dp)
                        .testTag("option_button_$optionLetter"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(letterBgColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = letterTextColor
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (isAnswerRevealed) {
                            if (isThisOptionTheCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Correct",
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else if (isThisOptionSelected && !isAnswerCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Wrong",
                                    tint = ErrorCrimson,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Explanation text after answer
        AnimatedVisibility(
            visible = isAnswerRevealed && question.explanation.isNotBlank(),
            enter = fadeIn()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "💡 ${question.explanation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
