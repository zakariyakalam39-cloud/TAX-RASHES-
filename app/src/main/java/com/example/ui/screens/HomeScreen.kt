package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TopUpProductEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
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
import com.example.ui.viewmodel.AppTab
import java.util.Locale

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    tournaments: List<TournamentEntity>,
    products: List<TopUpProductEntity>,
    onNavigateTab: (AppTab) -> Unit,
    onJoinTournament: (TournamentEntity) -> Unit,
    onViewRoom: (TournamentEntity) -> Unit,
    onOrderProduct: (TopUpProductEntity) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val walletBalance = userProfile?.walletBalance ?: 0.0
    val prizeBalance = userProfile?.prizeBalance ?: 0.0

    // Balance visibility toggle
    var isBalanceVisible by remember { mutableStateOf(true) }

    // Dashboard Tournament Category Filter (All, Live Now, Solo, Duo, Squad)
    var selectedTournamentFilter by remember { mutableStateOf("all") }
    val tournamentCategories = listOf("all", "live", "Solo", "Duo", "Squad")

    // In-Dashboard Top-Up Server Region Selector
    var selectedServerRegion by remember { mutableStateOf("Bangladesh") }
    val serverRegions = listOf("Bangladesh", "India", "Singapore", "Global")

    // Filter tournaments for dashboard list
    val filteredTournaments = tournaments.filter { t ->
        when (selectedTournamentFilter) {
            "all" -> true
            "live" -> t.status.equals("live", ignoreCase = true)
            "Solo" -> t.type.equals("Solo", ignoreCase = true)
            "Duo" -> t.type.equals("Duo", ignoreCase = true)
            "Squad" -> t.type.equals("Squad", ignoreCase = true)
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TaxBgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // ==========================================
        // 1. MINIMALIST GRADIENT WALLET BALANCE CARD
        // ==========================================
        item {
            MinimalistWalletPillCard(
                walletBalance = walletBalance,
                prizeBalance = prizeBalance,
                isBalanceVisible = isBalanceVisible,
                onToggleVisibility = { isBalanceVisible = !isBalanceVisible },
                onAddFunds = onOpenDeposit,
                onWithdraw = onOpenWithdraw,
                onOpenHistory = onOpenHistory
            )
        }

        // ============================================================
        // 2. TOURNAMENTS SECTION (CAROUSEL + CATEGORY TABS + LIST)
        // ============================================================
        // Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(TaxBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = TaxCyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Competitive Tournaments",
                        color = TaxTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "All Matches",
                    color = TaxCyanAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateTab(AppTab.MATCHES) }
                        .padding(4.dp)
                )
            }
        }

        // Live Match Spotlight / Featured Carousel (Requirement 1)
        item {
            val featuredList = tournaments.filter { it.status == "live" || it.status == "upcoming" }
            if (featuredList.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featuredList) { tour ->
                        FeaturedTournamentCarouselCard(
                            tournament = tour,
                            onJoinTournament = onJoinTournament,
                            onViewRoom = onViewRoom
                        )
                    }
                }
            }
        }

        // Categorized Filter Tabs (Requirement 2)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tournamentCategories) { cat ->
                    val isSelected = selectedTournamentFilter == cat
                    val label = when (cat) {
                        "all" -> "All Scrims"
                        "live" -> "⚡ Live Now"
                        else -> cat
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) TaxBluePrimary else TaxPanelElevated
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) TaxCyanAccent else TaxBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedTournamentFilter = cat }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("dashboard_cat_$cat")
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TaxTextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Compact Tournament List with 1-Tap Actions (Requirement 3)
        if (filteredTournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(TaxPanelDark)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No matches currently found for this category.",
                        color = TaxTextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredTournaments) { tour ->
                CompactTournamentCard(
                    tournament = tour,
                    onJoin = { onJoinTournament(tour) },
                    onViewRoom = { onViewRoom(tour) }
                )
            }
        }

        // ============================================================
        // 3. FULL TOP-UP SHOP SHOWCASE SECTION
        // ============================================================
        item {
            Spacer(modifier = Modifier.height(4.dp))
            DashboardShopHeader(
                selectedServer = selectedServerRegion,
                servers = serverRegions,
                onServerSelected = { selectedServerRegion = it },
                onViewFullShop = { onNavigateTab(AppTab.TOPUP) }
            )
        }

        // Product Cards Grid (in pairs)
        val productPairs = products.chunked(2)
        items(productPairs) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { prod ->
                    Box(modifier = Modifier.weight(1f)) {
                        DashboardShopProductCard(
                            product = prod,
                            onOrder = { onOrderProduct(prod) }
                        )
                    }
                }
                // If odd number of products, balance row
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// -------------------------------------------------------------------------------------------------
// Sub-Components
// -------------------------------------------------------------------------------------------------

@Composable
fun MinimalistWalletPillCard(
    walletBalance: Double,
    prizeBalance: Double,
    isBalanceVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onAddFunds: () -> Unit,
    onWithdraw: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0C1935),
                            Color(0xFF091428),
                            Color(0xFF070F20)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TaxBluePrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = TaxCyanAccent,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WALLET & PRIZES",
                            color = TaxTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Eye visibility toggle & Activity statement button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Activity ↗",
                            color = TaxCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TaxPanelElevated)
                                .clickable { onOpenHistory() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("dashboard_view_activity_btn")
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onToggleVisibility,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Balance",
                                tint = TaxTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = if (isBalanceVisible) String.format(Locale.US, "৳%.2f", walletBalance) else "৳ ••••••",
                            color = TaxCyanAccent,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Available Balance",
                            color = TaxTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Action buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onAddFunds,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("dashboard_add_funds_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Deposit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onWithdraw,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaxGreenSuccess),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("dashboard_withdraw_btn")
                        ) {
                            Icon(Icons.Default.CallMade, contentDescription = null, tint = TaxGreenSuccess, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Withdraw", color = TaxGreenSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Prize balance sub-pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TaxPanelElevated.copy(alpha = 0.6f))
                        .border(1.dp, TaxBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = TaxGoldPrize,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Match Prize Winnings:",
                                color = TaxTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = if (isBalanceVisible) String.format(Locale.US, "৳%.2f", prizeBalance) else "৳ •••",
                            color = TaxGreenSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedTournamentCarouselCard(
    tournament: TournamentEntity,
    onJoinTournament: (TournamentEntity) -> Unit,
    onViewRoom: (TournamentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLive = tournament.status.equals("live", ignoreCase = true)

    Card(
        modifier = modifier
            .width(260.dp)
            .clip(RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isLive) 1.5.dp else 1.dp,
            color = if (isLive) TaxRedLive else TaxBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isLive) TaxRedLive else TaxBluePrimary)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isLive) "LIVE NOW" else "FEATURED",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "${tournament.type} · ${tournament.mapName}",
                    color = TaxTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tournament.title,
                color = TaxTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = tournament.schedule,
                color = TaxTextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Slots progress
            val progress = if (tournament.maxSlots > 0) tournament.filledSlots.toFloat() / tournament.maxSlots.toFloat() else 0f
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Slots", color = TaxTextMuted, fontSize = 10.sp)
                Text("${tournament.filledSlots}/${tournament.maxSlots}", color = TaxCyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                    Text("Prize Pool", color = TaxTextMuted, fontSize = 9.sp)
                    Text(
                        text = String.format(Locale.US, "৳%.2f", tournament.prizePool),
                        color = TaxGreenSuccess,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                if (isLive || tournament.roomId.isNotBlank()) {
                    Button(
                        onClick = { onViewRoom(tournament) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TaxRedLive),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.MeetingRoom, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Room", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { onJoinTournament(tournament) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Join ৳${tournament.entryFee.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CompactTournamentCard(
    tournament: TournamentEntity,
    onJoin: () -> Unit,
    onViewRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLive = tournament.status.equals("live", ignoreCase = true)
    val isCompleted = tournament.status.equals("completed", ignoreCase = true)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isLive) 1.5.dp else 1.dp,
            color = if (isLive) TaxRedLive else TaxBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    isLive -> TaxRedLive
                                    isCompleted -> TaxGreenSuccess.copy(alpha = 0.2f)
                                    else -> TaxBluePrimary.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = tournament.status.uppercase(),
                            color = when {
                                isLive -> Color.White
                                isCompleted -> TaxGreenSuccess
                                else -> TaxCyanAccent
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${tournament.type} · ${tournament.mapName}",
                        color = TaxTextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = tournament.title,
                    color = TaxTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "${tournament.schedule} · Slots: ${tournament.filledSlots}/${tournament.maxSlots}",
                    color = TaxTextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 1.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Prize: ${String.format(Locale.US, "৳%.2f", tournament.prizePool)}",
                        color = TaxGreenSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Entry: ৳${tournament.entryFee.toInt()}",
                        color = TaxTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (isLive || tournament.roomId.isNotBlank()) {
                Button(
                    onClick = onViewRoom,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxRedLive),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("compact_view_room_${tournament.id}")
                ) {
                    Text("Room", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else if (!isCompleted) {
                Button(
                    onClick = onJoin,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TaxBluePrimary),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("compact_join_${tournament.id}")
                ) {
                    Text("Register", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                OutlinedButton(
                    onClick = onViewRoom,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Result", color = TaxTextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun DashboardShopHeader(
    selectedServer: String,
    servers: List<String>,
    onServerSelected: (String) -> Unit,
    onViewFullShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(TaxCyanAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = TaxCyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Instant Diamond Top-Up Shop",
                    color = TaxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Full Shop",
                color = TaxCyanAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onViewFullShop() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Select your game server & order diamonds directly to your Player UID without account passwords.",
            color = TaxTextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Region selector chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            servers.forEach { srv ->
                val isSelected = selectedServer == srv
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TaxBluePrimary else TaxPanelElevated)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) TaxCyanAccent else TaxBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onServerSelected(srv) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = srv,
                        color = if (isSelected) Color.White else TaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardShopProductCard(
    product: TopUpProductEntity,
    onOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOrder() },
        colors = CardDefaults.cardColors(containerColor = TaxPanelDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TaxPanelElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Diamond,
                        contentDescription = null,
                        tint = TaxCyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (product.bonusDiamonds > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TaxGreenSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "+${product.bonusDiamonds} Free",
                            color = TaxGreenSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                color = TaxTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = "${product.diamonds} Diamonds",
                color = TaxTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.US, "৳%.2f", product.price),
                    color = TaxCyanAccent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TaxBluePrimary)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Buy",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
