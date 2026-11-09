package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxGreenSuccess
import com.example.ui.theme.TaxPanelDark
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxRedLive
import com.example.ui.theme.TaxTextMuted
import com.example.ui.theme.TaxTextPrimary
import com.example.ui.theme.TaxTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    userProfile: UserProfileEntity?,
    transactions: List<WalletTransactionEntity>,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenHistorySheet: () -> Unit,
    onSelectTransaction: (WalletTransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val walletBalance = userProfile?.walletBalance ?: 0.0
    val prizeBalance = userProfile?.prizeBalance ?: 0.0

    var filterType by remember { mutableStateOf("all") }
    val filters = listOf("all", "deposit", "tournament", "topup", "withdrawal")

    val filteredTransactions = transactions.filter { tx ->
        when (filterType) {
            "all" -> true
            "deposit" -> tx.type == "deposit"
            "tournament" -> tx.type.contains("tournament")
            "topup" -> tx.type == "topup_purchase"
            "withdrawal" -> tx.type == "withdrawal"
            else -> true
        }
    }

    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TaxBgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = "Gaming Wallet",
                    color = TaxTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Manage your tournament funds, deposit via bKash/Nagad/Rocket, and withdraw winnings.",
                    color = TaxTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Wallet Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("MAIN WALLET BALANCE", color = TaxTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format(Locale.US, "৳%.2f", walletBalance),
                                color = TaxCyanAccent,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("PRIZE WINNINGS", color = TaxTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format(Locale.US, "৳%.2f", prizeBalance),
                                color = TaxGreenSuccess,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenDeposit,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("wallet_add_balance_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Balance", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = onOpenWithdraw,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("wallet_withdraw_btn"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaxGreenSuccess)
                        ) {
                            Icon(Icons.Default.CallMade, contentDescription = null, tint = TaxGreenSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Withdraw", color = TaxGreenSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Transaction History Header & Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Transaction History",
                        color = TaxTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Full Statement ↗",
                    color = TaxCyanAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onOpenHistorySheet() }
                        .padding(4.dp)
                        .testTag("wallet_view_full_statement_btn")
                )
            }
        }

        // Filters
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { f ->
                    val isSelected = filterType == f
                    val label = when (f) {
                        "all" -> "All"
                        "deposit" -> "Deposits"
                        "tournament" -> "Tournaments"
                        "topup" -> "Top-Ups"
                        "withdrawal" -> "Withdrawals"
                        else -> f
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                            .clickable { filterType = f }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TaxTextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Transaction List
        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found.", color = TaxTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredTransactions) { tx ->
                val isPositive = tx.amount > 0
                val icon = if (isPositive) Icons.Default.CallReceived else Icons.Default.CallMade
                val color = if (isPositive) TaxGreenSuccess else TaxTextPrimary

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectTransaction(tx) },
                    colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isPositive) TaxGreenSuccess.copy(alpha = 0.15f) else TaxPanelElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isPositive) TaxGreenSuccess else TaxCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.note.ifBlank { tx.type.replace('_', ' ').uppercase() },
                                color = TaxTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = dateFormatter.format(Date(tx.timestamp)),
                                    color = TaxTextMuted,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when (tx.status) {
                                                "completed" -> TaxGreenSuccess.copy(alpha = 0.2f)
                                                "pending" -> TaxCyanAccent.copy(alpha = 0.2f)
                                                else -> TaxRedLive.copy(alpha = 0.2f)
                                            }
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tx.status.uppercase(),
                                        color = when (tx.status) {
                                            "completed" -> TaxGreenSuccess
                                            "pending" -> TaxCyanAccent
                                            else -> TaxRedLive
                                        },
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = (if (isPositive) "+৳" else "-৳") + String.format(Locale.US, "%.2f", kotlin.math.abs(tx.amount)),
                            color = color,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
