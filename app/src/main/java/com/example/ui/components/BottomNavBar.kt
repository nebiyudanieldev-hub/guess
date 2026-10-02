package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.ScreenTab

@Composable
fun GuessBottomNavBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == ScreenTab.HOME,
            onClick = { onTabSelected(ScreenTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontWeight = if (currentTab == ScreenTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = IndigoPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.PLAY,
            onClick = { onTabSelected(ScreenTab.PLAY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.PLAY) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                    contentDescription = "Play"
                )
            },
            label = {
                Text(
                    text = "Play",
                    fontWeight = if (currentTab == ScreenTab.PLAY) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = IndigoPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_play")
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.LEADERBOARD,
            onClick = { onTabSelected(ScreenTab.LEADERBOARD) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.LEADERBOARD) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                    contentDescription = "Leaderboard"
                )
            },
            label = {
                Text(
                    text = "Leaderboard",
                    fontWeight = if (currentTab == ScreenTab.LEADERBOARD) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = IndigoPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_leaderboard")
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.PROFILE,
            onClick = { onTabSelected(ScreenTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontWeight = if (currentTab == ScreenTab.PROFILE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IndigoPrimary,
                indicatorColor = IndigoPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
