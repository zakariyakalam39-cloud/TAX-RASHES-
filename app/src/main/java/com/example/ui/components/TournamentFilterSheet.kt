package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
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
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxPanelDark
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxTextMuted
import com.example.ui.theme.TaxTextPrimary
import com.example.ui.theme.TaxTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentFilterSheet(
    sheetState: SheetState,
    selectedGame: String,
    selectedStatus: String,
    selectedFeeRange: String,
    selectedType: String,
    onGameChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onFeeRangeChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onResetAll: () -> Unit,
    onDismiss: () -> Unit,
    matchCount: Int
) {
    val games = listOf(
        "all" to "All Games",
        "Free Fire" to "Free Fire",
        "PUBG Mobile" to "PUBG Mobile",
        "Call of Duty: Mobile" to "COD Mobile"
    )

    val statuses = listOf(
        "all" to "All Status",
        "live" to "⚡ Active (Live)",
        "upcoming" to "Upcoming",
        "completed" to "Finished"
    )

    val feeRanges = listOf(
        "all" to "All Fees",
        "free" to "Free (৳0)",
        "under_50" to "Under ৳50",
        "50_100" to "৳50 - ৳100",
        "above_100" to "৳100+"
    )

    val types = listOf(
        "all" to "All Modes",
        "Solo" to "Solo",
        "Duo" to "Duo",
        "Squad" to "Squad"
    )

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
                            Icons.Default.FilterList,
                            contentDescription = null,
                            tint = TaxCyanAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Filter Tournaments",
                            color = TaxTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Customize game, fee range & status",
                            color = TaxTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. GAME TITLE FILTER
            Text("Game Title", color = TaxCyanAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(games) { (key, label) ->
                    val isSelected = selectedGame == key
                    FilterChipItem(
                        label = label,
                        isSelected = isSelected,
                        onClick = { onGameChange(key) },
                        testTag = "sheet_filter_game_$key"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. TOURNAMENT STATUS FILTER
            Text("Tournament Status", color = TaxCyanAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(statuses) { (key, label) ->
                    val isSelected = selectedStatus == key
                    FilterChipItem(
                        label = label,
                        isSelected = isSelected,
                        onClick = { onStatusChange(key) },
                        testTag = "sheet_filter_status_$key"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. ENTRY FEE RANGE FILTER
            Text("Entry Fee Range", color = TaxCyanAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(feeRanges) { (key, label) ->
                    val isSelected = selectedFeeRange == key
                    FilterChipItem(
                        label = label,
                        isSelected = isSelected,
                        onClick = { onFeeRangeChange(key) },
                        testTag = "sheet_filter_fee_$key"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. TEAM FORMAT FILTER
            Text("Team Format", color = TaxCyanAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(types) { (key, label) ->
                    val isSelected = selectedType == key
                    FilterChipItem(
                        label = label,
                        isSelected = isSelected,
                        onClick = { onTypeChange(key) },
                        testTag = "sheet_filter_type_$key"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onResetAll,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("filter_sheet_reset_btn"),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                ) {
                    Text("Reset All", color = TaxTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .testTag("filter_sheet_apply_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary)
                ) {
                    Text("Show $matchCount Matches", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
            .border(
                width = 1.dp,
                color = if (isSelected) TaxCyanAccent else TaxBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else TaxTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
