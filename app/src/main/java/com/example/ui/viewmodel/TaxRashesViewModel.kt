package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TaxRashesDatabase
import com.example.data.model.DepositRequestEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.RegistrationEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.TopUpProductEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.data.repository.TaxRashesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    HOME,
    MATCHES,
    TOPUP,
    WALLET,
    PROFILE
}

class TaxRashesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaxRashesRepository
    private val adminPrefs = application.getSharedPreferences("tax_rashes_admin_prefs", Context.MODE_PRIVATE)

    init {
        val database = TaxRashesDatabase.getDatabase(application)
        repository = TaxRashesRepository(database.dao())
        viewModelScope.launch {
            TaxRashesDatabase.seedDatabase(database.dao())
        }
    }

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTournaments: StateFlow<List<TournamentEntity>> = repository.allTournaments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<TopUpProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<WalletTransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paymentMethods: StateFlow<List<PaymentMethodEntity>> = repository.paymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadCount: StateFlow<Int> = repository.allNotifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allDeposits: StateFlow<List<DepositRequestEntity>> = repository.allDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWithdrawals: StateFlow<List<WithdrawalRequestEntity>> = repository.allWithdrawals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRegistrations: StateFlow<List<RegistrationEntity>> = repository.allRegistrations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSupportTickets: StateFlow<List<SupportTicketEntity>> = repository.allSupportTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Filters
    private val _matchStatusFilter = MutableStateFlow("all") // all, upcoming, live, completed
    val matchStatusFilter: StateFlow<String> = _matchStatusFilter.asStateFlow()

    private val _matchTypeFilter = MutableStateFlow("all") // all, solo, duo, squad
    val matchTypeFilter: StateFlow<String> = _matchTypeFilter.asStateFlow()

    private val _tournamentGameFilter = MutableStateFlow("all") // all, Free Fire, PUBG Mobile, Call of Duty: Mobile
    val tournamentGameFilter: StateFlow<String> = _tournamentGameFilter.asStateFlow()

    private val _tournamentFeeRangeFilter = MutableStateFlow("all") // all, free, under_50, 50_100, above_100
    val tournamentFeeRangeFilter: StateFlow<String> = _tournamentFeeRangeFilter.asStateFlow()

    private val _tournamentSearchQuery = MutableStateFlow("")
    val tournamentSearchQuery: StateFlow<String> = _tournamentSearchQuery.asStateFlow()

    fun setMatchStatusFilter(status: String) {
        _matchStatusFilter.value = status
    }

    fun setMatchTypeFilter(type: String) {
        _matchTypeFilter.value = type
    }

    fun setTournamentGameFilter(game: String) {
        _tournamentGameFilter.value = game
    }

    fun setTournamentFeeRangeFilter(range: String) {
        _tournamentFeeRangeFilter.value = range
    }

    fun setTournamentSearchQuery(query: String) {
        _tournamentSearchQuery.value = query
    }

    fun resetTournamentFilters() {
        _tournamentGameFilter.value = "all"
        _tournamentFeeRangeFilter.value = "all"
        _matchStatusFilter.value = "all"
        _matchTypeFilter.value = "all"
        _tournamentSearchQuery.value = ""
    }

    private val _productCategory = MutableStateFlow("all")
    val productCategory: StateFlow<String> = _productCategory.asStateFlow()

    fun setProductCategory(cat: String) {
        _productCategory.value = cat
    }

    // Modal / Dialog States
    private val _selectedTournamentForJoin = MutableStateFlow<TournamentEntity?>(null)
    val selectedTournamentForJoin: StateFlow<TournamentEntity?> = _selectedTournamentForJoin.asStateFlow()

    private val _selectedTournamentForRoom = MutableStateFlow<TournamentEntity?>(null)
    val selectedTournamentForRoom: StateFlow<TournamentEntity?> = _selectedTournamentForRoom.asStateFlow()

    private val _selectedProductForOrder = MutableStateFlow<TopUpProductEntity?>(null)
    val selectedProductForOrder: StateFlow<TopUpProductEntity?> = _selectedProductForOrder.asStateFlow()

    private val _showDepositDialog = MutableStateFlow(false)
    val showDepositDialog: StateFlow<Boolean> = _showDepositDialog.asStateFlow()

    private val _showWithdrawDialog = MutableStateFlow(false)
    val showWithdrawDialog: StateFlow<Boolean> = _showWithdrawDialog.asStateFlow()

    private val _showSupportDialog = MutableStateFlow(false)
    val showSupportDialog: StateFlow<Boolean> = _showSupportDialog.asStateFlow()

    private val _showAdminSheet = MutableStateFlow(false)
    val showAdminSheet: StateFlow<Boolean> = _showAdminSheet.asStateFlow()

    // Admin Authentication State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _showAdminLoginDialog = MutableStateFlow(false)
    val showAdminLoginDialog: StateFlow<Boolean> = _showAdminLoginDialog.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    private val _showChangeAdminPasswordDialog = MutableStateFlow(false)
    val showChangeAdminPasswordDialog: StateFlow<Boolean> = _showChangeAdminPasswordDialog.asStateFlow()

    private val _changePasswordError = MutableStateFlow<String?>(null)
    val changePasswordError: StateFlow<String?> = _changePasswordError.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _showTransactionHistorySheet = MutableStateFlow(false)
    val showTransactionHistorySheet: StateFlow<Boolean> = _showTransactionHistorySheet.asStateFlow()

    private val _selectedTransactionReceipt = MutableStateFlow<WalletTransactionEntity?>(null)
    val selectedTransactionReceipt: StateFlow<WalletTransactionEntity?> = _selectedTransactionReceipt.asStateFlow()

    // Toasts / Snackbars
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun showToast(msg: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(msg)
        }
    }

    // Admin Password Management
    fun getStoredAdminPassword(): String {
        return adminPrefs.getString("admin_password", "admin123") ?: "admin123"
    }

    fun requestAdminAccess() {
        if (_isAdminLoggedIn.value) {
            _showAdminSheet.value = true
        } else {
            _adminLoginError.value = null
            _showAdminLoginDialog.value = true
        }
    }

    fun closeAdminLoginDialog() {
        _showAdminLoginDialog.value = false
        _adminLoginError.value = null
    }

    fun attemptAdminLogin(password: String): Boolean {
        val storedPass = getStoredAdminPassword()
        // Accept stored password, or 'admin' as convenience alias if still on default
        val isMatch = (password == storedPass) || (storedPass == "admin123" && password == "admin")
        return if (isMatch) {
            _isAdminLoggedIn.value = true
            _adminLoginError.value = null
            _showAdminLoginDialog.value = false
            _showAdminSheet.value = true
            showToast("Welcome Administrator! Access granted.")
            true
        } else {
            _adminLoginError.value = "Incorrect admin password! (Default: admin123)"
            showToast("Authentication failed: Incorrect password")
            false
        }
    }

    fun adminLogout() {
        _isAdminLoggedIn.value = false
        _showAdminSheet.value = false
        showToast("Admin session locked.")
    }

    fun setShowChangeAdminPasswordDialog(show: Boolean) {
        _showChangeAdminPasswordDialog.value = show
        _changePasswordError.value = null
    }

    fun changeAdminPassword(currentPass: String, newPass: String, confirmPass: String): Boolean {
        val stored = getStoredAdminPassword()
        val currentValid = (currentPass == stored) || (stored == "admin123" && currentPass == "admin")
        if (!currentValid) {
            _changePasswordError.value = "Current password does not match"
            return false
        }
        if (newPass.length < 4) {
            _changePasswordError.value = "New password must be at least 4 characters"
            return false
        }
        if (newPass != confirmPass) {
            _changePasswordError.value = "New passwords do not match"
            return false
        }
        adminPrefs.edit().putString("admin_password", newPass).apply()
        _showChangeAdminPasswordDialog.value = false
        _changePasswordError.value = null
        showToast("Admin password updated successfully!")
        return true
    }

    // Dialog control functions
    fun openJoinTournament(tournament: TournamentEntity) {
        _selectedTournamentForJoin.value = tournament
    }

    fun closeJoinTournament() {
        _selectedTournamentForJoin.value = null
    }

    fun openRoomDetails(tournament: TournamentEntity) {
        _selectedTournamentForRoom.value = tournament
    }

    fun closeRoomDetails() {
        _selectedTournamentForRoom.value = null
    }

    fun openTopUpOrder(product: TopUpProductEntity) {
        _selectedProductForOrder.value = product
    }

    fun closeTopUpOrder() {
        _selectedProductForOrder.value = null
    }

    fun setDepositDialogVisible(visible: Boolean) {
        _showDepositDialog.value = visible
    }

    fun setWithdrawDialogVisible(visible: Boolean) {
        _showWithdrawDialog.value = visible
    }

    fun setSupportDialogVisible(visible: Boolean) {
        _showSupportDialog.value = visible
    }

    fun setAdminSheetVisible(visible: Boolean) {
        if (visible && !_isAdminLoggedIn.value) {
            requestAdminAccess()
        } else {
            _showAdminSheet.value = visible
        }
    }

    fun setNotificationsSheetVisible(visible: Boolean) {
        _showNotificationsSheet.value = visible
    }

    fun setTransactionHistorySheetVisible(visible: Boolean) {
        _showTransactionHistorySheet.value = visible
    }

    fun openTransactionReceipt(tx: WalletTransactionEntity) {
        _selectedTransactionReceipt.value = tx
    }

    fun closeTransactionReceipt() {
        _selectedTransactionReceipt.value = null
    }

    // Actions
    fun registerForTournament(
        tournament: TournamentEntity,
        playerUid: String,
        playerName: String,
        teamName: String
    ) {
        viewModelScope.launch {
            val result = repository.registerForTournament(
                tournament = tournament,
                playerUid = playerUid,
                playerName = playerName,
                teamName = teamName
            )
            result.onSuccess {
                _selectedTournamentForJoin.value = null
                showToast("Tournament Registration Confirmed! Good luck, Champion!")
            }.onFailure { err ->
                showToast(err.message ?: "Registration failed")
            }
        }
    }

    fun placeTopUpOrder(
        product: TopUpProductEntity,
        playerUid: String,
        serverRegion: String
    ) {
        viewModelScope.launch {
            val result = repository.buyTopUpProduct(
                product = product,
                playerUid = playerUid,
                serverRegion = serverRegion
            )
            result.onSuccess {
                _selectedProductForOrder.value = null
                showToast("Top-Up Order submitted successfully! Diamonds dispatched.")
            }.onFailure { err ->
                showToast(err.message ?: "Top-up failed")
            }
        }
    }

    fun submitDeposit(
        method: String,
        amount: Double,
        transactionId: String,
        senderNumber: String
    ) {
        viewModelScope.launch {
            val result = repository.requestDeposit(
                method = method,
                amount = amount,
                transactionId = transactionId,
                senderNumber = senderNumber
            )
            result.onSuccess {
                _showDepositDialog.value = false
                showToast("Deposit request submitted! Verification takes 5-15 mins.")
            }.onFailure { err ->
                showToast(err.message ?: "Deposit request failed")
            }
        }
    }

    fun submitWithdrawal(
        method: String,
        amount: Double,
        destinationNumber: String
    ) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(
                method = method,
                amount = amount,
                destinationNumber = destinationNumber
            )
            result.onSuccess {
                _showWithdrawDialog.value = false
                showToast("Withdrawal requested! Payout will arrive in your wallet shortly.")
            }.onFailure { err ->
                showToast(err.message ?: "Withdrawal failed")
            }
        }
    }

    fun submitSupportTicket(subject: String, message: String) {
        viewModelScope.launch {
            repository.submitSupportTicket(subject, message)
            _showSupportDialog.value = false
            showToast("Support ticket submitted. Our team will assist you shortly.")
        }
    }

    fun updateProfile(name: String, phone: String, inGameUid: String) {
        viewModelScope.launch {
            repository.updateProfile(name, phone, inGameUid)
            showToast("Player Profile updated successfully!")
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    // Admin demo operations
    fun adminApproveDeposit(id: Long) {
        viewModelScope.launch {
            repository.adminApproveDeposit(id)
            showToast("Deposit #$id approved & wallet balance credited!")
        }
    }

    fun adminRejectDeposit(id: Long) {
        viewModelScope.launch {
            repository.adminRejectDeposit(id)
            showToast("Deposit #$id rejected.")
        }
    }

    fun adminApproveWithdrawal(id: Long) {
        viewModelScope.launch {
            repository.adminApproveWithdrawal(id)
            showToast("Withdrawal #$id approved & marked dispatched!")
        }
    }

    fun adminMakeLive(tournamentId: String, roomId: String, roomPass: String) {
        viewModelScope.launch {
            repository.adminUpdateTournamentStatus(
                tournamentId = tournamentId,
                status = "live",
                roomId = roomId.ifBlank { "7891234" },
                roomPass = roomPass.ifBlank { "tax44" }
            )
            showToast("Tournament is now LIVE! Room codes broadcasted.")
        }
    }

    fun adminCompleteMatch(tournamentId: String) {
        viewModelScope.launch {
            repository.adminUpdateTournamentStatus(tournamentId = tournamentId, status = "completed")
            showToast("Tournament marked as completed.")
        }
    }

    fun adminAddDemoFunds(amount: Double) {
        viewModelScope.launch {
            repository.adminAddDemoBalance(amount)
            showToast("৳$amount added to wallet!")
        }
    }
}
