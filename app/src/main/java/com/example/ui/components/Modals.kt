package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DepositRequestEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.TopUpProductEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.ui.theme.BkashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxDanger
import com.example.ui.theme.TaxGreenSuccess
import com.example.ui.theme.TaxPanelDark
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxRedLive
import com.example.ui.theme.TaxTextMuted
import com.example.ui.theme.TaxTextPrimary
import com.example.ui.theme.TaxTextSecondary
import java.util.Locale

fun copyToClipboard(context: Context, label: String, text: String, onCopied: () -> Unit) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    onCopied()
}

// 1. Join Tournament Dialog
@Composable
fun JoinTournamentDialog(
    tournament: TournamentEntity,
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onConfirm: (playerUid: String, playerName: String, teamName: String) -> Unit
) {
    var playerUid by remember { mutableStateOf(userProfile?.inGameUid ?: "") }
    var playerName by remember { mutableStateOf(userProfile?.name ?: "") }
    var teamName by remember { mutableStateOf("") }
    val currentWallet = userProfile?.walletBalance ?: 0.0
    val canAfford = currentWallet >= tournament.entryFee

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Match Registration",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Text(
                    text = tournament.title,
                    color = TaxCyanAccent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "${tournament.type} · ${tournament.mapName} · ${tournament.schedule}",
                    color = TaxTextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Balance & Fee Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TaxPanelElevated)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Entry Fee", color = TaxTextSecondary, fontSize = 11.sp)
                            Text(
                                String.format(Locale.US, "৳%.2f", tournament.entryFee),
                                color = TaxTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Your Balance", color = TaxTextSecondary, fontSize = 11.sp)
                            Text(
                                String.format(Locale.US, "৳%.2f", currentWallet),
                                color = if (canAfford) TaxGreenSuccess else TaxRedLive,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (!canAfford) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = TaxRedLive, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Insufficient balance. Deposit funds in your Wallet first.",
                            color = TaxRedLive,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = playerUid,
                    onValueChange = { playerUid = it },
                    label = { Text("Free Fire Player UID") },
                    placeholder = { Text("e.g. 2891481023") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("join_player_uid_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("In-Game Name (IGN)") },
                    placeholder = { Text("e.g. T4X_Sniper") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                if (tournament.type.equals("Duo", ignoreCase = true) || tournament.type.equals("Squad", ignoreCase = true)) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = teamName,
                        onValueChange = { teamName = it },
                        label = { Text("Team / Squad Name") },
                        placeholder = { Text("e.g. Team Venom") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TaxCyanAccent,
                            unfocusedBorderColor = TaxBorder,
                            focusedLabelColor = TaxCyanAccent,
                            unfocusedLabelColor = TaxTextSecondary,
                            focusedTextColor = TaxTextPrimary,
                            unfocusedTextColor = TaxTextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (canAfford && playerUid.isNotBlank() && playerName.isNotBlank()) {
                            onConfirm(playerUid, playerName, teamName)
                        }
                    },
                    enabled = canAfford && playerUid.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_join_match_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TaxBluePrimary,
                        disabledContainerColor = TaxPanelElevated
                    )
                ) {
                    Text(
                        text = if (canAfford) "Confirm & Pay ${String.format(Locale.US, "৳%.2f", tournament.entryFee)}" else "Insufficient Balance",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// 2. Room Details Dialog
@Composable
fun RoomDetailsDialog(
    tournament: TournamentEntity,
    onDismiss: () -> Unit,
    onToast: (String) -> Unit
) {
    val context = LocalContext.current
    val hasCredentials = tournament.roomId.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Custom Room Info",
                            color = TaxTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Text(
                    text = tournament.title,
                    color = TaxCyanAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (hasCredentials) {
                    // Room ID
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(TaxPanelElevated)
                            .border(1.dp, TaxBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("CUSTOM ROOM ID", color = TaxTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = tournament.roomId,
                                    color = TaxTextPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    copyToClipboard(context, "Room ID", tournament.roomId) {
                                        onToast("Room ID copied to clipboard!")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Room ID", tint = TaxCyanAccent)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(TaxPanelElevated)
                            .border(1.dp, TaxBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("ROOM PASSWORD", color = TaxTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = tournament.roomPassword,
                                    color = TaxGreenSuccess,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                            IconButton(
                                onClick = {
                                    copyToClipboard(context, "Room Password", tournament.roomPassword) {
                                        onToast("Room Password copied!")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Key, contentDescription = "Copy Room Password", tint = TaxGreenSuccess)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(TaxPanelElevated)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = TaxTextMuted, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Room Credentials Pending",
                                color = TaxTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Room ID and Password are automatically published 15 minutes prior to match schedule (${tournament.roomReleaseAt.ifBlank { "Schedule pending" }}).",
                                color = TaxTextSecondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rules
                Text("Tournament Rules", color = TaxTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(
                    text = tournament.rules,
                    color = TaxTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Text("Got It", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// 3. Top-Up Order Dialog
@Composable
fun TopUpOrderDialog(
    product: TopUpProductEntity,
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onConfirm: (playerUid: String, serverRegion: String) -> Unit
) {
    var playerUid by remember { mutableStateOf(userProfile?.inGameUid ?: "") }
    var selectedServer by remember { mutableStateOf("Bangladesh") }
    val servers = listOf("Bangladesh", "India", "Singapore", "Global")
    val currentWallet = userProfile?.walletBalance ?: 0.0
    val canAfford = currentWallet >= product.price

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Instant Diamond Top-Up",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Text(
                    text = product.title,
                    color = TaxCyanAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = product.description,
                    color = TaxTextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Price Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TaxPanelElevated)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Package Price", color = TaxTextSecondary, fontSize = 11.sp)
                            Text(
                                String.format(Locale.US, "৳%.2f", product.price),
                                color = TaxTextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Available Wallet", color = TaxTextSecondary, fontSize = 11.sp)
                            Text(
                                String.format(Locale.US, "৳%.2f", currentWallet),
                                color = if (canAfford) TaxGreenSuccess else TaxRedLive,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Server / Region", color = TaxTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    servers.forEach { server ->
                        val isSelected = selectedServer == server
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                                .clickable { selectedServer = server }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = server,
                                color = if (isSelected) Color.White else TaxTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = playerUid,
                    onValueChange = { playerUid = it },
                    label = { Text("Enter Free Fire Player UID") },
                    placeholder = { Text("e.g. 294820184") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("topup_player_uid_input"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (canAfford && playerUid.isNotBlank()) {
                            onConfirm(playerUid, selectedServer)
                        }
                    },
                    enabled = canAfford && playerUid.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_topup_order_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TaxBluePrimary,
                        disabledContainerColor = TaxPanelElevated
                    )
                ) {
                    Text(
                        text = if (canAfford) "Pay ${String.format(Locale.US, "৳%.2f", product.price)} Now" else "Deposit Balance First",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// 4. Add Balance (Deposit) Dialog
@Composable
fun AddBalanceDialog(
    paymentMethods: List<PaymentMethodEntity>,
    onDismiss: () -> Unit,
    onSubmit: (method: String, amount: Double, transactionId: String, senderNumber: String) -> Unit,
    onToast: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf(if (paymentMethods.isNotEmpty()) paymentMethods[0].method else "bKash") }
    var amountText by remember { mutableStateOf("100") }
    var transactionId by remember { mutableStateOf("") }
    var senderNumber by remember { mutableStateOf("") }

    val currentMethodInfo = paymentMethods.find { it.method.equals(selectedMethod, ignoreCase = true) }
        ?: PaymentMethodEntity(selectedMethod, "01789-234567", "Personal (Send Money)", "Send money to this number and input Txn ID below.")

    val methodColor = when (selectedMethod.lowercase()) {
        "bkash" -> BkashPink
        "nagad" -> NagadOrange
        else -> RocketPurple
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Wallet Balance",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Payment Method Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("bKash", "Nagad", "Rocket").forEach { m ->
                        val isSelected = selectedMethod.equals(m, ignoreCase = true)
                        val color = when (m.lowercase()) {
                            "bkash" -> BkashPink
                            "nagad" -> NagadOrange
                            else -> RocketPurple
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) color.copy(alpha = 0.25f) else TaxPanelElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) color else TaxBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedMethod = m }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m,
                                color = if (isSelected) color else TaxTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Official Payment Number Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TaxPanelElevated)
                        .border(1.dp, TaxBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$selectedMethod ${currentMethodInfo.label}",
                                color = methodColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentMethodInfo.number,
                                color = TaxTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                        IconButton(
                            onClick = {
                                copyToClipboard(context, "$selectedMethod Number", currentMethodInfo.number) {
                                    onToast("$selectedMethod number copied!")
                                }
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Number", tint = methodColor)
                        }
                    }
                }

                Text(
                    text = currentMethodInfo.instructions,
                    color = TaxTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Amount Chips
                Text("Deposit Amount (৳)", color = TaxTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("50", "100", "200", "500").forEach { preset ->
                        val isSelected = amountText == preset
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                                .clickable { amountText = preset }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "৳$preset",
                                color = if (isSelected) Color.White else TaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (৳)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it },
                    label = { Text("Your $selectedMethod Phone Number") },
                    placeholder = { Text("e.g. 017xxxxxxxx") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = transactionId,
                    onValueChange = { transactionId = it },
                    label = { Text("Transaction ID (TxnID)") },
                    placeholder = { Text("e.g. 9AK8391XZ") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deposit_txnid_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (amount > 0 && transactionId.isNotBlank()) {
                            onSubmit(selectedMethod, amount, transactionId, senderNumber)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_deposit_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Text("Submit Deposit Request", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// 5. Withdraw Dialog
@Composable
fun WithdrawDialog(
    prizeBalance: Double,
    onDismiss: () -> Unit,
    onSubmit: (method: String, amount: Double, destinationNumber: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") }
    var amountText by remember { mutableStateOf("") }
    var destinationNumber by remember { mutableStateOf("") }
    val requestedAmount = amountText.toDoubleOrNull() ?: 0.0
    val isValid = requestedAmount > 0 && requestedAmount <= prizeBalance && destinationNumber.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Withdraw Prize Money",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Available Prize Balance
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TaxPanelElevated)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Available Winnings", color = TaxTextSecondary, fontSize = 11.sp)
                            Text(
                                String.format(Locale.US, "৳%.2f", prizeBalance),
                                color = TaxGreenSuccess,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        OutlinedButton(
                            onClick = { amountText = prizeBalance.toInt().toString() },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaxCyanAccent)
                        ) {
                            Text("Max", color = TaxCyanAccent, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Payout Method", color = TaxTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("bKash", "Nagad", "Rocket").forEach { m ->
                        val isSelected = selectedMethod == m
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                                .clickable { selectedMethod = m }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m,
                                color = if (isSelected) Color.White else TaxTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Withdrawal Amount (৳)") },
                    placeholder = { Text("e.g. 100") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = destinationNumber,
                    onValueChange = { destinationNumber = it },
                    label = { Text("Your $selectedMethod Number") },
                    placeholder = { Text("e.g. 017xxxxxxxx") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (isValid) {
                            onSubmit(selectedMethod, requestedAmount, destinationNumber)
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_withdraw_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TaxBluePrimary,
                        disabledContainerColor = TaxPanelElevated
                    )
                ) {
                    Text("Confirm Withdrawal", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// 6. Support Ticket Dialog
@Composable
fun SupportTicketDialog(
    onDismiss: () -> Unit,
    onSubmit: (subject: String, message: String) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Submit Support Ticket",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    placeholder = { Text("e.g. Deposit Verification Issue") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Description & Details") },
                    placeholder = { Text("Describe how we can help you...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextSecondary,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (subject.isNotBlank() && message.isNotBlank()) {
                            onSubmit(subject, message)
                        }
                    },
                    enabled = subject.isNotBlank() && message.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Text("Submit Ticket", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// 7. Admin / Moderator Demo Bottom Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDemoSheet(
    sheetState: SheetState,
    tournaments: List<TournamentEntity>,
    deposits: List<DepositRequestEntity>,
    withdrawals: List<WithdrawalRequestEntity>,
    onDismiss: () -> Unit,
    onAddFunds: (Double) -> Unit,
    onApproveDeposit: (Long) -> Unit,
    onRejectDeposit: (Long) -> Unit,
    onApproveWithdrawal: (Long) -> Unit,
    onMakeLive: (tournamentId: String, roomId: String, roomPass: String) -> Unit,
    onCompleteMatch: (tournamentId: String) -> Unit,
    onLogout: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TaxPanelDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ADMIN & MODERATOR PANEL",
                        color = TaxCyanAccent,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Real-time controls for tournaments, deposits & wallet",
                        color = TaxTextSecondary,
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Admin Authentication Active Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, TaxBorder, RoundedCornerShape(12.dp))
                    .testTag("admin_session_bar"),
                colors = CardDefaults.cardColors(containerColor = TaxPanelElevated)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(TaxGreenSuccess)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "SESSION: AUTHENTICATED",
                                color = TaxGreenSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "admin@taxrashes.com",
                                color = TaxTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onChangePasswordClick,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder),
                            modifier = Modifier.testTag("admin_change_password_button")
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Password", color = TaxCyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onLogout,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxDanger.copy(alpha = 0.85f)),
                            modifier = Modifier.testTag("admin_logout_button")
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Lock", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Wallet Boost
            Text("Quick Tester Actions", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAddFunds(500.0) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxGreenSuccess)
                ) {
                    Text("+৳500 Wallet", color = Color(0xFF00150E), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { onAddFunds(1000.0) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Text("+৳1000 Wallet", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pending Deposits
            Text(
                text = "Deposit Requests (${deposits.count { it.status == "pending" }} pending)",
                color = TaxTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val pendingDeposits = deposits.filter { it.status == "pending" }
            if (pendingDeposits.isEmpty()) {
                Text("No pending deposits.", color = TaxTextMuted, fontSize = 12.sp)
            } else {
                pendingDeposits.forEach { dep ->
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
                            Column {
                                Text("${dep.method} · ৳${dep.amount}", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("TxnID: ${dep.transactionId}", color = TaxCyanAccent, fontSize = 11.sp)
                                Text("Sender: ${dep.senderNumber}", color = TaxTextMuted, fontSize = 11.sp)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { onApproveDeposit(dep.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TaxGreenSuccess),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Approve", color = Color(0xFF00150E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { onRejectDeposit(dep.id) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Reject", color = TaxRedLive, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pending Withdrawals
            Text(
                text = "Withdrawal Requests (${withdrawals.count { it.status == "pending" }} pending)",
                color = TaxTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val pendingWithdrawals = withdrawals.filter { it.status == "pending" }
            if (pendingWithdrawals.isEmpty()) {
                Text("No pending withdrawals.", color = TaxTextMuted, fontSize = 12.sp)
            } else {
                pendingWithdrawals.forEach { w ->
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
                            Column {
                                Text("${w.method} · ৳${w.amount}", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Dest: ${w.destinationNumber}", color = TaxCyanAccent, fontSize = 11.sp)
                            }
                            Button(
                                onClick = { onApproveWithdrawal(w.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = TaxGreenSuccess),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Dispatched", color = Color(0xFF00150E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tournament Status & Room Release
            Text("Tournament Match Management", color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            tournaments.forEach { t ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TaxPanelElevated)
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(t.title, color = TaxTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Text(
                                text = t.status.uppercase(),
                                color = if (t.status == "live") TaxRedLive else TaxCyanAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Text("${t.type} · Slots ${t.filledSlots}/${t.maxSlots}", color = TaxTextMuted, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (t.status != "live") {
                                Button(
                                    onClick = { onMakeLive(t.id, "8912345", "tax99") },
                                    colors = ButtonDefaults.buttonColors(containerColor = TaxRedLive),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Release Room & Set Live", fontSize = 10.sp, color = Color.White)
                                }
                            }
                            if (t.status != "completed") {
                                Button(
                                    onClick = { onCompleteMatch(t.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Mark Complete", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

// 8. Notifications Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    sheetState: SheetState,
    notifications: List<NotificationEntity>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TaxPanelDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications Inbox",
                    color = TaxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMarkAllRead) {
                        Icon(Icons.Default.DoneAll, contentDescription = "Mark all read", tint = TaxCyanAccent)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No notifications yet.", color = TaxTextMuted, fontSize = 13.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!notif.isRead) TaxPanelElevated else TaxBgDark)
                                .border(
                                    width = 1.dp,
                                    color = if (!notif.isRead) TaxCyanAccent.copy(alpha = 0.5f) else TaxBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notif.title,
                                        color = if (!notif.isRead) TaxCyanAccent else TaxTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (!notif.isRead) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(TaxCyanAccent)
                                        )
                                    }
                                }
                                Text(
                                    text = notif.message,
                                    color = TaxTextSecondary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
