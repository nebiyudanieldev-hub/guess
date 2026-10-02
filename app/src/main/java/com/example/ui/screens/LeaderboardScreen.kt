package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPoints
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.VioletAccent
import com.example.ui.viewmodel.LeaderboardEntry
import com.example.ui.viewmodel.LeaderboardTab

val AVATAR_EMOJIS = listOf("🦊", "🦁", "🐼", "🚀", "⚡", "👑", "🔥", "🦄")

@Composable
fun LeaderboardScreen(
    currentTab: LeaderboardTab,
    entries: List<LeaderboardEntry>,
    onTabSelected: (LeaderboardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val topThree = entries.take(3)
    val remainingEntries = entries.drop(3)
    val currentUserEntry = entries.firstOrNull { it.isCurrentUser }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen")
    ) {
        // Title Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Compete with guessers worldwide",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tabs: Daily | Weekly | All Time
        TabRow(
            selectedTabIndex = currentTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = IndigoPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                    color = IndigoPrimary
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Tab(
                selected = currentTab == LeaderboardTab.DAILY,
                onClick = { onTabSelected(LeaderboardTab.DAILY) },
                text = { Text("Daily", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_leaderboard_daily")
            )
            Tab(
                selected = currentTab == LeaderboardTab.WEEKLY,
                onClick = { onTabSelected(LeaderboardTab.WEEKLY) },
                text = { Text("Weekly", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_leaderboard_weekly")
            )
            Tab(
                selected = currentTab == LeaderboardTab.ALL_TIME,
                onClick = { onTabSelected(LeaderboardTab.ALL_TIME) },
                text = { Text("All Time", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_leaderboard_all_time")
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Podium (Ranks 1, 2, 3)
            if (topThree.isNotEmpty()) {
                item {
                    PodiumView(topThree = topThree)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // Highlight Current User Standing
            if (currentUserEntry != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("current_user_leaderboard_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = IndigoPrimary.copy(alpha = 0.12f)
                        ),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(IndigoPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${currentUserEntry.rank}",
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = currentUserEntry.name,
                                            fontWeight = FontWeight.Black,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(AmberPoints, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "YOU",
                                                color = Color.Black,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = currentUserEntry.levelName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = "${currentUserEntry.points} pts",
                                fontWeight = FontWeight.Black,
                                color = IndigoPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Leaderboard remaining rows (Rank 4+)
            items(remainingEntries) { entry ->
                LeaderboardRow(entry = entry)
            }
        }
    }
}

@Composable
fun PodiumView(topThree: List<LeaderboardEntry>) {
    val rank1 = topThree.getOrNull(0)
    val rank2 = topThree.getOrNull(1)
    val rank3 = topThree.getOrNull(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd Place (Silver)
        if (rank2 != null) {
            PodiumColumn(
                entry = rank2,
                crownEmoji = "🥈",
                badgeColor = Color(0xFF94A3B8),
                height = 110.dp
            )
        }

        // 1st Place (Gold)
        if (rank1 != null) {
            PodiumColumn(
                entry = rank1,
                crownEmoji = "👑",
                badgeColor = AmberPoints,
                height = 136.dp
            )
        }

        // 3rd Place (Bronze)
        if (rank3 != null) {
            PodiumColumn(
                entry = rank3,
                crownEmoji = "🥉",
                badgeColor = Color(0xFFD97706),
                height = 96.dp
            )
        }
    }
}

@Composable
fun PodiumColumn(
    entry: LeaderboardEntry,
    crownEmoji: String,
    badgeColor: Color,
    height: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Text(text = crownEmoji, fontSize = 22.sp)
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(badgeColor.copy(alpha = 0.2f))
                .border(2.dp, badgeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = AVATAR_EMOJIS.getOrElse(entry.avatarIndex) { "👤" },
                fontSize = 26.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = entry.name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
        Text(
            text = "${entry.points}",
            fontWeight = FontWeight.Black,
            color = badgeColor,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(84.dp)
                .height(height)
                .background(badgeColor.copy(alpha = 0.15f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .border(1.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${entry.rank}",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = badgeColor
            )
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isCurrentUser) IndigoPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${entry.rank}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp),
                    fontSize = 14.sp
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AVATAR_EMOJIS.getOrElse(entry.avatarIndex) { "👤" },
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.name,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (entry.isCurrentUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(You)",
                                color = IndigoPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = entry.levelName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${entry.points} pts",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
