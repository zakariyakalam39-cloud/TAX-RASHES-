package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddBalanceDialog
import com.example.ui.components.AdminDemoSheet
import com.example.ui.components.AdminLoginDialog
import com.example.ui.components.ChangeAdminPasswordDialog
import com.example.ui.components.JoinTournamentDialog
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.RoomDetailsDialog
import com.example.ui.components.SupportTicketDialog
import com.example.ui.components.TaxRashesBottomNav
import com.example.ui.components.TaxRashesTopBar
import com.example.ui.components.TopUpOrderDialog
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TopUpScreen
import com.example.ui.screens.TransactionDetailDialog
import com.example.ui.screens.TransactionHistorySheet
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxRashesTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.TaxRashesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: TaxRashesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaxRashesTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                // State collection
                val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
                val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                val tournaments by viewModel.allTournaments.collectAsStateWithLifecycle()
                val products by viewModel.allProducts.collectAsStateWithLifecycle()
                val transactions by viewModel.allTransactions.collectAsStateWithLifecycle()
                val paymentMethods by viewModel.paymentMethods.collectAsStateWithLifecycle()
                val notifications by viewModel.allNotifications.collectAsStateWithLifecycle()
                val unreadNotifications by viewModel.unreadCount.collectAsStateWithLifecycle()
                val deposits by viewModel.allDeposits.collectAsStateWithLifecycle()
                val withdrawals by viewModel.allWithdrawals.collectAsStateWithLifecycle()
                val supportTickets by viewModel.allSupportTickets.collectAsStateWithLifecycle()

                val statusFilter by viewModel.matchStatusFilter.collectAsStateWithLifecycle()
                val typeFilter by viewModel.matchTypeFilter.collectAsStateWithLifecycle()
                val gameFilter by viewModel.tournamentGameFilter.collectAsStateWithLifecycle()
                val feeRangeFilter by viewModel.tournamentFeeRangeFilter.collectAsStateWithLifecycle()
                val tournamentSearchQuery by viewModel.tournamentSearchQuery.collectAsStateWithLifecycle()
                val productCategory by viewModel.productCategory.collectAsStateWithLifecycle()

                // Dialog states
                val selectedTournamentForJoin by viewModel.selectedTournamentForJoin.collectAsStateWithLifecycle()
                val selectedTournamentForRoom by viewModel.selectedTournamentForRoom.collectAsStateWithLifecycle()
                val selectedProductForOrder by viewModel.selectedProductForOrder.collectAsStateWithLifecycle()
                val showDepositDialog by viewModel.showDepositDialog.collectAsStateWithLifecycle()
                val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsStateWithLifecycle()
                val showSupportDialog by viewModel.showSupportDialog.collectAsStateWithLifecycle()
                val showAdminSheet by viewModel.showAdminSheet.collectAsStateWithLifecycle()
                val showAdminLoginDialog by viewModel.showAdminLoginDialog.collectAsStateWithLifecycle()
                val adminLoginError by viewModel.adminLoginError.collectAsStateWithLifecycle()
                val showChangeAdminPasswordDialog by viewModel.showChangeAdminPasswordDialog.collectAsStateWithLifecycle()
                val changePasswordError by viewModel.changePasswordError.collectAsStateWithLifecycle()
                val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()
                val showTransactionHistorySheet by viewModel.showTransactionHistorySheet.collectAsStateWithLifecycle()
                val selectedTransactionReceipt by viewModel.selectedTransactionReceipt.collectAsStateWithLifecycle()

                val adminSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val notifSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val historySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                // SnackBar observer
                LaunchedEffect(Unit) {
                    viewModel.snackbarMessage.collectLatest { msg ->
                        snackbarHostState.showSnackbar(msg)
                    }
                }

                // Back navigation: if not on Home, back key navigates to Home
                BackHandler(enabled = currentTab != AppTab.HOME) {
                    viewModel.setTab(AppTab.HOME)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = TaxBgDark,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    topBar = {
                        TaxRashesTopBar(
                            walletBalance = userProfile?.walletBalance ?: 0.0,
                            unreadNotifications = unreadNotifications,
                            onWalletClick = { viewModel.setTab(AppTab.WALLET) },
                            onNotificationsClick = { viewModel.setNotificationsSheetVisible(true) },
                            onAdminClick = { viewModel.requestAdminAccess() }
                        )
                    },
                    bottomBar = {
                        TaxRashesBottomNav(
                            currentTab = currentTab,
                            onTabSelected = { tab -> viewModel.setTab(tab) }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            AppTab.HOME -> {
                                HomeScreen(
                                    userProfile = userProfile,
                                    tournaments = tournaments,
                                    products = products,
                                    onNavigateTab = { tab -> viewModel.setTab(tab) },
                                    onJoinTournament = { t -> viewModel.openJoinTournament(t) },
                                    onViewRoom = { t -> viewModel.openRoomDetails(t) },
                                    onOrderProduct = { p -> viewModel.openTopUpOrder(p) },
                                    onOpenDeposit = { viewModel.setDepositDialogVisible(true) },
                                    onOpenWithdraw = { viewModel.setWithdrawDialogVisible(true) },
                                    onOpenHistory = { viewModel.setTransactionHistorySheetVisible(true) }
                                )
                            }

                            AppTab.MATCHES -> {
                                MatchesScreen(
                                    tournaments = tournaments,
                                    searchQuery = tournamentSearchQuery,
                                    onSearchQueryChange = { q -> viewModel.setTournamentSearchQuery(q) },
                                    gameFilter = gameFilter,
                                    onGameFilterChange = { g -> viewModel.setTournamentGameFilter(g) },
                                    statusFilter = statusFilter,
                                    onStatusFilterChange = { s -> viewModel.setMatchStatusFilter(s) },
                                    feeRangeFilter = feeRangeFilter,
                                    onFeeRangeFilterChange = { f -> viewModel.setTournamentFeeRangeFilter(f) },
                                    typeFilter = typeFilter,
                                    onTypeFilterChange = { t -> viewModel.setMatchTypeFilter(t) },
                                    onResetFilters = { viewModel.resetTournamentFilters() },
                                    onJoinTournament = { t -> viewModel.openJoinTournament(t) },
                                    onViewRoom = { t -> viewModel.openRoomDetails(t) }
                                )
                            }

                            AppTab.TOPUP -> {
                                TopUpScreen(
                                    products = products,
                                    categoryFilter = productCategory,
                                    onCategoryFilterChange = { cat -> viewModel.setProductCategory(cat) },
                                    onOrderProduct = { p -> viewModel.openTopUpOrder(p) }
                                )
                            }

                            AppTab.WALLET -> {
                                WalletScreen(
                                    userProfile = userProfile,
                                    transactions = transactions,
                                    onOpenDeposit = { viewModel.setDepositDialogVisible(true) },
                                    onOpenWithdraw = { viewModel.setWithdrawDialogVisible(true) },
                                    onOpenHistorySheet = { viewModel.setTransactionHistorySheetVisible(true) },
                                    onSelectTransaction = { tx -> viewModel.openTransactionReceipt(tx) }
                                )
                            }

                            AppTab.PROFILE -> {
                                ProfileScreen(
                                    userProfile = userProfile,
                                    supportTickets = supportTickets,
                                    onUpdateProfile = { name, phone, inGameUid ->
                                        viewModel.updateProfile(name, phone, inGameUid)
                                    },
                                    onOpenSupport = { viewModel.setSupportDialogVisible(true) },
                                    onOpenAdminSheet = { viewModel.requestAdminAccess() },
                                    onToast = { msg -> viewModel.showToast(msg) }
                                )
                            }
                        }
                    }

                    // --- Modals & Sheets ---
                    selectedTournamentForJoin?.let { tournament ->
                        JoinTournamentDialog(
                            tournament = tournament,
                            userProfile = userProfile,
                            onDismiss = { viewModel.closeJoinTournament() },
                            onConfirm = { uid, ign, team ->
                                viewModel.registerForTournament(tournament, uid, ign, team)
                            }
                        )
                    }

                    selectedTournamentForRoom?.let { tournament ->
                        RoomDetailsDialog(
                            tournament = tournament,
                            onDismiss = { viewModel.closeRoomDetails() },
                            onToast = { msg -> viewModel.showToast(msg) }
                        )
                    }

                    selectedProductForOrder?.let { product ->
                        TopUpOrderDialog(
                            product = product,
                            userProfile = userProfile,
                            onDismiss = { viewModel.closeTopUpOrder() },
                            onConfirm = { uid, server ->
                                viewModel.placeTopUpOrder(product, uid, server)
                            }
                        )
                    }

                    if (showDepositDialog) {
                        AddBalanceDialog(
                            paymentMethods = paymentMethods,
                            onDismiss = { viewModel.setDepositDialogVisible(false) },
                            onSubmit = { method, amount, txnId, sender ->
                                viewModel.submitDeposit(method, amount, txnId, sender)
                            },
                            onToast = { msg -> viewModel.showToast(msg) }
                        )
                    }

                    if (showWithdrawDialog) {
                        WithdrawDialog(
                            prizeBalance = userProfile?.prizeBalance ?: 0.0,
                            onDismiss = { viewModel.setWithdrawDialogVisible(false) },
                            onSubmit = { method, amount, destNumber ->
                                viewModel.submitWithdrawal(method, amount, destNumber)
                            }
                        )
                    }

                    if (showSupportDialog) {
                        SupportTicketDialog(
                            onDismiss = { viewModel.setSupportDialogVisible(false) },
                            onSubmit = { subject, message ->
                                viewModel.submitSupportTicket(subject, message)
                            }
                        )
                    }

                    if (showAdminSheet) {
                        AdminDemoSheet(
                            sheetState = adminSheetState,
                            tournaments = tournaments,
                            deposits = deposits,
                            withdrawals = withdrawals,
                            onDismiss = {
                                coroutineScope.launch { adminSheetState.hide() }
                                viewModel.setAdminSheetVisible(false)
                            },
                            onAddFunds = { amount -> viewModel.adminAddDemoFunds(amount) },
                            onApproveDeposit = { id -> viewModel.adminApproveDeposit(id) },
                            onRejectDeposit = { id -> viewModel.adminRejectDeposit(id) },
                            onApproveWithdrawal = { id -> viewModel.adminApproveWithdrawal(id) },
                            onMakeLive = { id, roomId, roomPass ->
                                viewModel.adminMakeLive(id, roomId, roomPass)
                            },
                            onCompleteMatch = { id -> viewModel.adminCompleteMatch(id) },
                            onLogout = {
                                coroutineScope.launch { adminSheetState.hide() }
                                viewModel.adminLogout()
                            },
                            onChangePasswordClick = {
                                viewModel.setShowChangeAdminPasswordDialog(true)
                            }
                        )
                    }

                    if (showAdminLoginDialog) {
                        AdminLoginDialog(
                            onDismiss = { viewModel.closeAdminLoginDialog() },
                            onSubmitLogin = { password -> viewModel.attemptAdminLogin(password) },
                            errorMessage = adminLoginError
                        )
                    }

                    if (showChangeAdminPasswordDialog) {
                        ChangeAdminPasswordDialog(
                            onDismiss = { viewModel.setShowChangeAdminPasswordDialog(false) },
                            onSubmitChange = { currentPass, newPass, confirmPass ->
                                viewModel.changeAdminPassword(currentPass, newPass, confirmPass)
                            },
                            errorMessage = changePasswordError
                        )
                    }

                    if (showNotificationsSheet) {
                        NotificationsSheet(
                            sheetState = notifSheetState,
                            notifications = notifications,
                            onDismiss = {
                                coroutineScope.launch { notifSheetState.hide() }
                                viewModel.setNotificationsSheetVisible(false)
                            },
                            onMarkAllRead = { viewModel.markAllNotificationsRead() }
                        )
                    }

                    if (showTransactionHistorySheet) {
                        TransactionHistorySheet(
                            sheetState = historySheetState,
                            transactions = transactions,
                            onDismiss = {
                                coroutineScope.launch { historySheetState.hide() }
                                viewModel.setTransactionHistorySheetVisible(false)
                            },
                            onToast = { msg -> viewModel.showToast(msg) }
                        )
                    }

                    selectedTransactionReceipt?.let { tx ->
                        TransactionDetailDialog(
                            transaction = tx,
                            onDismiss = { viewModel.closeTransactionReceipt() },
                            onToast = { msg -> viewModel.showToast(msg) }
                        )
                    }
                }
            }
        }
    }
}
