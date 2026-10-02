package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.ui.theme.AmberPoints
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletAccent

@Composable
fun ClueView(
    question: Question,
    modifier: Modifier = Modifier
) {
    var isBlurredRevealed by remember(question.id) { mutableStateOf(false) }
    val blurRadius by animateFloatAsState(
        targetValue = if (question.questionType == QuestionType.BLURRED && !isBlurredRevealed) 16f else 0f,
        label = "blur"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                )
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                RoundedCornerShape(20.dp)
            )
            .testTag("clue_container"),
        contentAlignment = Alignment.Center
    ) {
        when (question.questionType) {
            QuestionType.FLAG -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "🏳️" },
                        fontSize = 68.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "FLAG GUESS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            QuestionType.EMOJI -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "❓ + 💡" },
                        fontSize = 38.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "EMOJI PUZZLE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberPoints,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            QuestionType.LOGO -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "🏢" },
                        fontSize = 58.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "LOGO CLUE",
                        style = MaterialTheme.typography.labelSmall,
                        color = VioletAccent,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            QuestionType.SILHOUETTE -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "👤" },
                        fontSize = 60.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "SILHOUETTE GUESS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            QuestionType.BLURRED -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isBlurredRevealed = !isBlurredRevealed },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "🔍" },
                        fontSize = 54.sp,
                        modifier = Modifier.blur(blurRadius.dp)
                    )
                    if (!isBlurredRevealed) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Tap to reveal",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Tap to sharpen",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            QuestionType.IMAGE -> {
                if (question.visualClue.startsWith("http")) {
                    AsyncImage(
                        model = question.visualClue,
                        contentDescription = "Question Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = question.visualClue.ifBlank { "🖼️" },
                            fontSize = 60.sp
                        )
                        Text(
                            text = "IMAGE GUESS",
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                }
            }

            QuestionType.MULTIPLE_CHOICE -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = question.visualClue.ifBlank { "💡" },
                        fontSize = 52.sp
                    )
                    Text(
                        text = "QUICK TRIVIA",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }
    }
}
