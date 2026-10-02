package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Achievement
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberPoints
import com.example.ui.theme.FlameStreak
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.VioletAccent

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    levelName: String,
    nextLevelInfo: Pair<String, Float>,
    achievements: List<Achievement>,
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onUpdateUsername: (String) -> Unit,
    onUpdateAvatar: (Int) -> Unit,
    onRegisterAccount: (String, String) -> Unit,
    onOpenAdminDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = userProfile ?: UserProfile()
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showAvatarPicker by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    var editNameInput by remember(profile.username) { mutableStateOf(profile.username) }

    var regUsername by remember(profile.username) { mutableStateOf(profile.username) }
    var regContact by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Change Username") },
            text = {
                OutlinedTextField(
                    value = editNameInput,
                    onValueChange = { editNameInput = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_edit_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editNameInput.isNotBlank()) {
                            onUpdateUsername(editNameInput.trim())
                        }
                        showEditNameDialog = false
                    },
                    modifier = Modifier.testTag("username_save_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAvatarPicker) {
        AlertDialog(
            onDismissRequest = { showAvatarPicker = false },
            title = { Text("Select Avatar") },
            text = {
                Column {
                    Text("Choose your player avatar emoji:")
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(AVATAR_EMOJIS.indices.toList()) { index ->
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (profile.avatarIndex == index) IndigoPrimary.copy(alpha = 0.2f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .border(
                                        width = if (profile.avatarIndex == index) 2.dp else 1.dp,
                                        color = if (profile.avatarIndex == index) IndigoPrimary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        onUpdateAvatar(index)
                                        showAvatarPicker = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = AVATAR_EMOJIS[index], fontSize = 28.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showAvatarPicker = false }) {
                    Text("Done")
                }
            }
        )
    }

    if (showRegisterDialog) {
        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            title = { Text("Create Free Account") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Save your high scores, sync streak and rank on leaderboards!",
                        style = MaterialTheme.typography.bodySmall
                    )
                    OutlinedTextField(
                        value = regUsername,
                        onValueChange = { regUsername = it },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = regContact,
                        onValueChange = { regContact = it },
                        label = { Text("Email or Phone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        label = { Text("Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (regUsername.isNotBlank()) {
                            onRegisterAccount(regUsername.trim(), regContact.trim())
                        }
                        showRegisterDialog = false
                    },
                    modifier = Modifier.testTag("account_register_submit")
                ) {
                    Text("Register")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showRegisterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // User Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_header_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(IndigoPrimary.copy(alpha = 0.15f))
                                    .border(2.dp, IndigoPrimary, CircleShape)
                                    .clickable { showAvatarPicker = true }
                                    .testTag("avatar_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = AVATAR_EMOJIS.getOrElse(profile.avatarIndex) { "👤" },
                                    fontSize = 36.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = profile.username,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black
                                    )
                                    IconButton(
                                        onClick = { showEditNameDialog = true },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit name",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Level: $levelName",
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = if (profile.isGuest) "Guest Account" else "Registered: ${profile.emailOrPhone}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (profile.isGuest) {
                            Button(
                                onClick = { showRegisterDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberPoints, contentColor = Color.Black),
                                modifier = Modifier.testTag("register_account_button")
                            ) {
                                Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Level Progress Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Next Rank: ${nextLevelInfo.first}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${(nextLevelInfo.second * 100).toInt()}%",
                                style = MaterialTheme.typography.labelMedium,
                                color = IndigoPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { nextLevelInfo.second },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = IndigoPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        // Stats Grid
        item {
            Text(
                text = "Performance Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Points",
                        value = "${profile.totalPoints}",
                        icon = "⭐",
                        color = AmberPoints,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Best Score",
                        value = "${profile.bestScore}",
                        icon = "🎯",
                        color = VioletAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Current Streak",
                        value = "${profile.currentStreak} Days",
                        icon = "🔥",
                        color = FlameStreak,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Longest Streak",
                        value = "${profile.longestStreak} Days",
                        icon = "⚡",
                        color = FlameStreak,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Games Played",
                        value = "${profile.gamesPlayed}",
                        icon = "🎮",
                        color = IndigoPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Accuracy",
                        value = "${profile.accuracyPercentage}%",
                        icon = "📈",
                        color = SuccessEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Achievements Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${achievements.count { it.isUnlocked }} / ${achievements.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = IndigoPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                achievements.forEach { ach ->
                    AchievementRow(achievement = ach)
                }
            }
        }

        // Sound Effects & Audio Settings
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sound_settings_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    if (isSoundEnabled) SuccessEmerald.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "Sound Effects",
                                tint = if (isSoundEnabled) SuccessEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sound Effects",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = if (isSoundEnabled) "Enabled (Chimes, timer ticks & victory)" else "Muted (Silent mode)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = isSoundEnabled,
                        onCheckedChange = { onToggleSound() },
                        modifier = Modifier.testTag("sound_toggle_switch")
                    )
                }
            }
        }

        // Admin Dashboard Entry
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAdminDashboard() }
                    .testTag("open_admin_button"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(IndigoPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin",
                                tint = IndigoPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Admin Dashboard",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Manage questions, categories, and review stats",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = "OPEN",
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AchievementRow(achievement: Achievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievement_${achievement.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (achievement.isUnlocked) AmberPoints.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = achievement.iconEmoji, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = achievement.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (achievement.isUnlocked) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Unlocked",
                                tint = SuccessEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = achievement.progressText,
                style = MaterialTheme.typography.labelSmall,
                color = if (achievement.isUnlocked) SuccessEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
