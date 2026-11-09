package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TournamentEntity
import com.example.ui.components.TournamentFilterSheet
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
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    tournaments: List<TournamentEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    gameFilter: String,
    onGameFilterChange: (String) -> Unit,
    statusFilter: String,
    onStatusFilterChange: (String) -> Unit,
    feeRangeFilter: String,
    onFeeRangeFilterChange: (String) -> Unit,
    typeFilter: String,
    onTypeFilterChange: (String) -> Unit,
    onResetFilters: () -> Unit,
    onJoinTournament: (TournamentEntity) -> Unit,
    onViewRoom: (TournamentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Filter lists for chips
    val games = listOf("all" to "All Games", "Free Fire" to "Free Fire", "PUBG Mobile" to "PUBG Mobile", "Call of Duty: Mobile" to "COD Mobile")
    val statuses = listOf("all" to "All Status", "live" to "⚡ Active", "upcoming" to "Upcoming", "completed" to "Finished")
    val feeRanges = listOf("all" to "All Fees", "free" to "Free (৳0)", "under_50" to "< ৳50", "50_100" to "৳50-৳100", "above_100" to "৳100+")

    // Compute active filter count
    val activeFilterCount = (if (gameFilter != "all") 1 else 0) +
        (if (statusFilter != "all") 1 else 0) +
        (if (feeRangeFilter != "all") 1 else 0) +
        (if (typeFilter != "all") 1 else 0) +
        (if (searchQuery.isNotBlank()) 1 else 0)

    // Filter tournaments matching all criteria
    val filteredList = tournaments.filter { t ->
        val matchesSearch = if (searchQuery.isBlank()) {
            true
        } else {
            t.title.contains(searchQuery, ignoreCase = true) ||
                t.game.contains(searchQuery, ignoreCase = true) ||
                t.mapName.contains(searchQuery, ignoreCase = true)
        }

        val matchesGame = gameFilter == "all" || t.game.equals(gameFilter, ignoreCase = true)

        val matchesStatus = when (statusFilter) {
            "all" -> true
            "active", "live" -> t.status.equals("live", ignoreCase = true)
            "upcoming" -> t.status.equals("upcoming", ignoreCase = true)
            "finished", "completed" -> t.status.equals("completed", ignoreCase = true)
            else -> true
        }

        val matchesFee = when (feeRangeFilter) {
            "all" -> true
            "free" -> t.entryFee == 0.0
            "under_50" -> t.entryFee > 0.0 && t.entryFee < 50.0
            "50_100" -> t.entryFee in 50.0..100.0
            "above_100" -> t.entryFee > 100.0
            else -> true
        }

        val matchesType = typeFilter == "all" || t.type.equals(typeFilter, ignoreCase = true)

        matchesSearch && matchesGame && matchesStatus && matchesFee && matchesType
    }

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
                    text = "Tournament Hub",
                    color = TaxTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Filter by game, entry fee & status. Join custom matches or enter live room codes.",
                    color = TaxTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Search Bar & Filter Options Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search tournaments or maps...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TaxTextMuted, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = TaxTextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("match_search_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Filter options button with badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (activeFilterCount > 0) TaxBluePrimary else TaxPanelElevated)
                        .border(
                            width = 1.dp,
                            color = if (activeFilterCount > 0) TaxCyanAccent else TaxBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { showFilterSheet = true }
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .testTag("open_filter_sheet_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BadgedBox(
                            badge = {
                                if (activeFilterCount > 0) {
                                    Badge(containerColor = TaxGreenSuccess, contentColor = Color(0xFF00150E)) {
                                        Text("$activeFilterCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = if (activeFilterCount > 0) Color.White else TaxCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 1. GAME TITLE FILTER CHIPS
        item {
            Column {
                Text("GAME TITLE", color = TaxTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(games) { (key, label) ->
                        val isSelected = gameFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TaxCyanAccent else TaxBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onGameFilterChange(key) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("filter_game_$key"),
                            contentAlignment = Alignment.Center
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
        }

        // 2. STATUS & ENTRY FEE FILTER CHIPS
        item {
            Column {
                Text("STATUS & FEE RANGE", color = TaxTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Statuses
                    items(statuses) { (key, label) ->
                        val isSelected = statusFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TaxCyanAccent.copy(alpha = 0.25f) else TaxPanelDark)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TaxCyanAccent else TaxBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onStatusFilterChange(key) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("filter_status_$key")
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) TaxCyanAccent else TaxTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Divider
                    item {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(26.dp)
                                .background(TaxBorder)
                        )
                    }

                    // Fee ranges
                    items(feeRanges) { (key, label) ->
                        val isSelected = feeRangeFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TaxGreenSuccess.copy(alpha = 0.25f) else TaxPanelDark)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) TaxGreenSuccess else TaxBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onFeeRangeFilterChange(key) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("filter_fee_$key")
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) TaxGreenSuccess else TaxTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Active Filters Summary Bar
        if (activeFilterCount > 0) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(TaxPanelElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtered Results (${filteredList.size} of ${tournaments.size} matches)",
                        color = TaxTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Clear All",
                        color = TaxCyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onResetFilters() }
                            .padding(2.dp)
                            .testTag("clear_all_filters_btn")
                    )
                }
            }
        }

        // Tournament Cards
        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(TaxPanelDark)
                        .border(1.dp, TaxBorder, RoundedCornerShape(16.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = TaxTextMuted, modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No tournaments match your current filters.",
                            color = TaxTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Try clearing one or more filters to view active scrims.",
                            color = TaxTextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onResetFilters,
                            colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Reset All Filters", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredList) { t ->
                val isLive = t.status.equals("live", ignoreCase = true)
                val isCompleted = t.status.equals("completed", ignoreCase = true)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isLive) 1.5.dp else 1.dp,
                        color = if (isLive) TaxRedLive else TaxBorder
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when {
                                                isLive -> TaxRedLive
                                                isCompleted -> TaxGreenSuccess.copy(alpha = 0.2f)
                                                else -> TaxBluePrimary.copy(alpha = 0.2f)
                                            }
                                        )
                                        .padding(horizontal = 9.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = when {
                                            isLive -> "ACTIVE / LIVE"
                                            isCompleted -> "FINISHED"
                                            else -> "UPCOMING"
                                        },
                                        color = when {
                                            isLive -> Color.White
                                            isCompleted -> TaxGreenSuccess
                                            else -> TaxCyanAccent
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "${t.type} · ${t.mapName}",
                                    color = TaxTextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Game Title Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TaxPanelElevated)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = t.game,
                                    color = TaxCyanAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = t.title,
                            color = TaxTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Schedule: ${t.schedule}",
                            color = TaxTextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Slots info
                        val progress = if (t.maxSlots > 0) t.filledSlots.toFloat() / t.maxSlots.toFloat() else 0f
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Slots", color = TaxTextMuted, fontSize = 10.sp)
                            Text("${t.filledSlots} / ${t.maxSlots}", color = TaxTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isLive) TaxRedLive else TaxCyanAccent,
                            trackColor = TaxPanelElevated
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Prize Pool", color = TaxTextMuted, fontSize = 10.sp)
                                Text(
                                    String.format(Locale.US, "৳%.2f", t.prizePool),
                                    color = TaxGreenSuccess,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Entry Fee", color = TaxTextMuted, fontSize = 10.sp)
                                Text(
                                    text = if (t.entryFee == 0.0) "FREE" else String.format(Locale.US, "৳%.2f", t.entryFee),
                                    color = if (t.entryFee == 0.0) TaxGreenSuccess else TaxTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (isLive || t.roomId.isNotBlank()) {
                                Button(
                                    onClick = { onViewRoom(t) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TaxRedLive),
                                    modifier = Modifier.testTag("match_view_room_${t.id}")
                                ) {
                                    Icon(Icons.Default.MeetingRoom, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Room Code", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (!isCompleted) {
                                Button(
                                    onClick = { onJoinTournament(t) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                                    modifier = Modifier.testTag("match_join_${t.id}")
                                ) {
                                    Text(
                                        text = if (t.entryFee == 0.0) "Join Free" else "Register",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { onViewRoom(t) },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Finished", color = TaxTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    // Advanced Filter Bottom Sheet
    if (showFilterSheet) {
        TournamentFilterSheet(
            sheetState = sheetState,
            selectedGame = gameFilter,
            selectedStatus = statusFilter,
            selectedFeeRange = feeRangeFilter,
            selectedType = typeFilter,
            onGameChange = onGameFilterChange,
            onStatusChange = onStatusFilterChange,
            onFeeRangeChange = onFeeRangeFilterChange,
            onTypeChange = onTypeFilterChange,
            onResetAll = {
                onResetFilters()
                coroutineScope.launch { sheetState.hide() }
                showFilterSheet = false
            },
            onDismiss = {
                coroutineScope.launch { sheetState.hide() }
                showFilterSheet = false
            },
            matchCount = filteredList.size
        )
    }
}
