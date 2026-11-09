package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SupportTicketEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxGoldPrize
import com.example.ui.theme.TaxGreenSuccess
import com.example.ui.theme.TaxPanelDark
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxTextMuted
import com.example.ui.theme.TaxTextPrimary
import com.example.ui.theme.TaxTextSecondary
import java.util.Locale

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    supportTickets: List<SupportTicketEntity>,
    onUpdateProfile: (name: String, phone: String, inGameUid: String) -> Unit,
    onOpenSupport: () -> Unit,
    onOpenAdminSheet: () -> Unit,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }

    var editName by remember(userProfile) { mutableStateOf(userProfile?.name ?: "") }
    var editPhone by remember(userProfile) { mutableStateOf(userProfile?.phone ?: "") }
    var editUid by remember(userProfile) { mutableStateOf(userProfile?.inGameUid ?: "") }

    val matchesJoined = userProfile?.matchesJoined ?: 0
    val matchesWon = userProfile?.matchesWon ?: 0
    val winRate = if (matchesJoined > 0) (matchesWon.toFloat() / matchesJoined.toFloat() * 100).toInt() else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TaxBgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Player Profile",
                        color = TaxTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Your gaming identity and tournament record.",
                        color = TaxTextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(
                    onClick = { isEditing = !isEditing },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(TaxPanelElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = TaxCyanAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(TaxBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userProfile?.name ?: "Player",
                                color = TaxTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userProfile?.email ?: "player@taxrashes.com",
                                color = TaxTextMuted,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TaxPanelElevated)
                                    .clickable {
                                        userProfile?.inGameUid?.let { uid ->
                                            copyToClipboard(context, "Player UID", uid) {
                                                onToast("Free Fire UID copied!")
                                            }
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "UID: ${userProfile?.inGameUid ?: "Not set"}",
                                    color = TaxCyanAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(12.dp))
                            }
                        }
                    }

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("In-Game Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TaxCyanAccent,
                                unfocusedBorderColor = TaxBorder,
                                focusedTextColor = TaxTextPrimary,
                                unfocusedTextColor = TaxTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TaxCyanAccent,
                                unfocusedBorderColor = TaxBorder,
                                focusedTextColor = TaxTextPrimary,
                                unfocusedTextColor = TaxTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editUid,
                            onValueChange = { editUid = it },
                            label = { Text("Free Fire Player UID") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TaxCyanAccent,
                                unfocusedBorderColor = TaxBorder,
                                focusedTextColor = TaxTextPrimary,
                                unfocusedTextColor = TaxTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                onUpdateProfile(editName, editPhone, editUid)
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                        ) {
                            Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Stats Card
        item {
            Text("Career Esports Record", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Matches
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$matchesJoined", color = TaxTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Matches Joined", color = TaxTextMuted, fontSize = 10.sp)
                    }
                }

                // Wins
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = TaxGoldPrize, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$matchesWon", color = TaxGoldPrize, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("1st Place Wins", color = TaxTextMuted, fontSize = 10.sp)
                    }
                }

                // Win Rate
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("%", color = TaxGreenSuccess, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("$winRate%", color = TaxGreenSuccess, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Win Rate", color = TaxTextMuted, fontSize = 10.sp)
                    }
                }
            }
        }

        // Support Tickets Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Customer Support", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Button(
                            onClick = onOpenSupport,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                        ) {
                            Text("New Ticket", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (supportTickets.isEmpty()) {
                        Text(
                            text = "Have questions regarding tournament rooms or payment verification? Submit a support ticket.",
                            color = TaxTextSecondary,
                            fontSize = 12.sp
                        )
                    } else {
                        supportTickets.take(3).forEach { ticket ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TaxPanelElevated)
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(ticket.subject, color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(ticket.message, color = TaxTextSecondary, fontSize = 11.sp, maxLines = 1)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(TaxGreenSuccess.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(ticket.status.uppercase(), color = TaxGreenSuccess, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        // Admin & Demo Tools Panel Trigger
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onOpenAdminSheet() }
                    .testTag("profile_admin_tools_card"),
                colors = CardDefaults.cardColors(containerColor = TaxPanelElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, TaxCyanAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(TaxCyanAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Admin & Demo Tools",
                            color = TaxCyanAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Instant approve deposits, release room codes, or inject balance.",
                            color = TaxTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TaxCyanAccent)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Open", color = Color(0xFF050914), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
