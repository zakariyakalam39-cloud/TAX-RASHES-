package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.WalletTransactionEntity
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.BkashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxGoldPrize
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistorySheet(
    sheetState: SheetState,
    transactions: List<WalletTransactionEntity>,
    onDismiss: () -> Unit,
    onToast: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }
    var selectedTxForReceipt by remember { mutableStateOf<WalletTransactionEntity?>(null) }

    val filters = listOf(
        "all" to "All Activity",
        "deposit" to "Deposits",
        "prize" to "Prizes Won",
        "fee" to "Tournament Fees",
        "topup" to "Top-Up Purchases",
        "withdrawal" to "Withdrawals"
    )

    // Filter transactions based on category and search query
    val filteredList = transactions.filter { tx ->
        val matchesCategory = when (selectedFilter) {
            "all" -> true
            "deposit" -> tx.type == "deposit"
            "prize" -> tx.type == "tournament_prize"
            "fee" -> tx.type == "tournament_fee"
            "topup" -> tx.type == "topup_purchase"
            "withdrawal" -> tx.type == "withdrawal"
            else -> true
        }
        val matchesSearch = if (searchQuery.isBlank()) {
            true
        } else {
            tx.note.contains(searchQuery, ignoreCase = true) ||
                tx.type.contains(searchQuery, ignoreCase = true) ||
                tx.status.contains(searchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    // Totals
    val totalInflow = transactions.filter { it.amount > 0 && it.status == "completed" }.sumOf { it.amount }
    val totalOutflow = transactions.filter { it.amount < 0 && it.status == "completed" }.sumOf { kotlin.math.abs(it.amount) }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TaxPanelDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TaxBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = TaxCyanAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Transaction History",
                            color = TaxTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Local Room logs · ${transactions.size} records",
                            color = TaxTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Summary Stats Pill Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Inflow
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = TaxGreenSuccess, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Total Inflow", color = TaxTextMuted, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "+৳%.2f", totalInflow),
                            color = TaxGreenSuccess,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Outflow
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Total Spent", color = TaxTextMuted, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "-৳%.2f", totalOutflow),
                            color = TaxTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by note, TxnID, or type...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TaxTextMuted, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tx_history_search_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TaxCyanAccent,
                    unfocusedBorderColor = TaxBorder,
                    focusedTextColor = TaxTextPrimary,
                    unfocusedTextColor = TaxTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) TaxCyanAccent else TaxBorder,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedFilter = key }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("tx_filter_$key")
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TaxTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // List of Transactions
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = TaxTextMuted, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No matching transaction logs found.", color = TaxTextSecondary, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredList) { tx ->
                        val isPositive = tx.amount > 0
                        val icon = when (tx.type) {
                            "deposit" -> Icons.Default.ArrowDownward
                            "tournament_prize" -> Icons.Default.EmojiEvents
                            "tournament_fee" -> Icons.Default.SportsEsports
                            "topup_purchase" -> Icons.Default.Diamond
                            "withdrawal" -> Icons.Default.ArrowUpward
                            else -> Icons.Default.ReceiptLong
                        }

                        val iconTint = when {
                            tx.type == "tournament_prize" -> TaxGoldPrize
                            tx.type == "deposit" -> TaxGreenSuccess
                            tx.type == "topup_purchase" -> TaxCyanAccent
                            tx.type == "withdrawal" -> RocketPurple
                            else -> TaxBluePrimary
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedTxForReceipt = tx }
                                .testTag("tx_item_${tx.id}"),
                            colors = CardDefaults.cardColors(containerColor = TaxBgDark),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(iconTint.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = iconTint,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.note.ifBlank { tx.type.replace('_', ' ').uppercase() },
                                        color = TaxTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Text(
                                            text = dateFormatter.format(Date(tx.timestamp)),
                                            color = TaxTextMuted,
                                            fontSize = 10.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
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
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
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

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = (if (isPositive) "+৳" else "-৳") + String.format(Locale.US, "%.2f", kotlin.math.abs(tx.amount)),
                                    color = if (isPositive) TaxGreenSuccess else TaxTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Receipt Detail Modal
    selectedTxForReceipt?.let { tx ->
        TransactionDetailDialog(
            transaction = tx,
            onDismiss = { selectedTxForReceipt = null },
            onToast = onToast
        )
    }
}

@Composable
fun TransactionDetailDialog(
    transaction: WalletTransactionEntity,
    onDismiss: () -> Unit,
    onToast: (String) -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("EEEE, dd MMMM yyyy · hh:mm:ss a", Locale.getDefault()) }
    val isPositive = transaction.amount > 0
    val formattedId = String.format(Locale.US, "TXN-%05d", transaction.id)

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
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaction Receipt",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount Pill Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(TaxPanelElevated)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (if (isPositive) "+৳" else "-৳") + String.format(Locale.US, "%.2f", kotlin.math.abs(transaction.amount)),
                            color = if (isPositive) TaxGreenSuccess else TaxCyanAccent,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (transaction.status) {
                                        "completed" -> TaxGreenSuccess.copy(alpha = 0.2f)
                                        "pending" -> TaxCyanAccent.copy(alpha = 0.2f)
                                        else -> TaxRedLive.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "STATUS: ${transaction.status.uppercase()}",
                                color = when (transaction.status) {
                                    "completed" -> TaxGreenSuccess
                                    "pending" -> TaxCyanAccent
                                    else -> TaxRedLive
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Details List
                ReceiptRow(label = "Reference ID", value = formattedId, isCopyable = true, onCopy = {
                    copyToClipboard(context, "Transaction ID", formattedId) {
                        onToast("Transaction ID copied!")
                    }
                })

                ReceiptRow(label = "Category", value = transaction.type.replace('_', ' ').uppercase())

                ReceiptRow(label = "Details", value = transaction.note.ifBlank { "Gaming activity" })

                ReceiptRow(label = "Timestamp", value = dateFormatter.format(Date(transaction.timestamp)))

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val receiptText = "TAX RASHES Receipt: $formattedId | Amount: ${String.format(Locale.US, "৳%.2f", transaction.amount)} | Note: ${transaction.note} | Status: ${transaction.status}"
                        copyToClipboard(context, "Receipt Details", receiptText) {
                            onToast("Full receipt summary copied!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("copy_full_receipt_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Receipt Details", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(
    label: String,
    value: String,
    isCopyable: Boolean = false,
    onCopy: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = TaxTextMuted, fontSize = 11.sp)
            if (isCopyable) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onCopy() }
                ) {
                    Text(text = value, color = TaxCyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TaxCyanAccent, modifier = Modifier.size(12.dp))
                }
            } else {
                Text(text = value, color = TaxTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
