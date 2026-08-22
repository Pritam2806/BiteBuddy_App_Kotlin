package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyTopBar
import com.example.bitebuddy.ui.theme.AccentRed
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val updateState by profileViewModel.updateState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BiteBuddyTopBar(
                title = "Profile",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // User Header Card
                item {
                    BiteBuddyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showEditProfileDialog = true },
                        backgroundColor = DarkSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryYellow),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!userProfile?.profileImageUrl.isNullOrBlank()) {
                                    com.example.bitebuddy.ui.components.ProductImageView(
                                        imageUrl = userProfile?.profileImageUrl ?: "",
                                        contentDescription = "User Avatar",
                                        modifier = Modifier.fillMaxSize(),
                                        placeholderEmoji = "👤",
                                        emojiSize = 32
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User Avatar",
                                        tint = OnPrimaryYellow,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userProfile?.name?.ifBlank { "BiteBuddy Customer" } ?: "BiteBuddy Customer",
                                    color = TextWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = userProfile?.email?.ifBlank { "customer@bitebuddy.com" } ?: "customer@bitebuddy.com",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )

                                if (!userProfile?.mobile.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = userProfile?.mobile ?: "",
                                        color = PrimaryYellow,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Edit Profile",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Options Section 1: Account
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            ProfileMenuRow(
                                icon = Icons.Default.LocationOn,
                                title = "My address",
                                subtitle = userProfile?.address?.city?.ifBlank { null },
                                onClick = onNavigateToAddress
                            )

                            ProfileDivider()

                            ProfileMenuRow(
                                icon = Icons.Default.Security,
                                title = "Security",
                                onClick = { /* Security settings */ }
                            )
                        }
                    }
                }

                // Options Section 2: Preferences
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            ProfileMenuRow(
                                icon = Icons.Default.Language,
                                title = "Language",
                                subtitle = "English (US)",
                                onClick = { /* Language switcher */ }
                            )

                            ProfileDivider()

                            // Dark Mode Toggle Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DarkMode,
                                        contentDescription = null,
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = "Dark mode",
                                        color = TextWhite,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Switch(
                                    checked = darkModeEnabled,
                                    onCheckedChange = { darkModeEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = OnPrimaryYellow,
                                        checkedTrackColor = PrimaryYellow,
                                        uncheckedThumbColor = TextSecondary,
                                        uncheckedTrackColor = DarkInput
                                    )
                                )
                            }
                        }
                    }
                }

                // Options Section 3: Legal & Help
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            ProfileMenuRow(
                                icon = Icons.Default.Description,
                                title = "Terms and Conditions",
                                onClick = { /* Terms */ }
                            )

                            ProfileDivider()

                            ProfileMenuRow(
                                icon = Icons.AutoMirrored.Filled.HelpOutline,
                                title = "Help & Support",
                                onClick = { /* Help */ }
                            )
                        }
                    }
                }

                // Logout Button Card
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        ProfileMenuRow(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            title = "Log out",
                            iconColor = AccentRed,
                            titleColor = AccentRed,
                            showChevron = false,
                            onClick = { showLogoutDialog = true }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = DarkSurfaceElevated,
                title = {
                    Text(
                        text = "Sign Out",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to sign out of BiteBuddy?",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            profileViewModel.logout()
                            onLogoutSuccess()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Log Out", color = PrimaryYellow, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Cancel", color = PrimaryYellow, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }

        // Edit Profile Dialog
        if (showEditProfileDialog) {
            com.example.bitebuddy.ui.components.EditProfileDialog(
                initialName = userProfile?.name ?: "",
                initialMobile = userProfile?.mobile ?: "",
                initialImageUrl = userProfile?.profileImageUrl ?: "",
                isLoading = updateState is com.example.bitebuddy.data.model.Resource.Loading,
                onDismiss = {
                    showEditProfileDialog = false
                    profileViewModel.resetUpdateState()
                },
                onSave = { newName, newMobile, newImageUrl ->
                    profileViewModel.updateProfile(
                        name = newName,
                        mobile = newMobile,
                        profileImageUrl = newImageUrl,
                        onComplete = {
                            showEditProfileDialog = false
                            profileViewModel.resetUpdateState()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconColor: androidx.compose.ui.graphics.Color = PrimaryYellow,
    titleColor: androidx.compose.ui.graphics.Color = TextWhite,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(DarkInput)
    )
}

