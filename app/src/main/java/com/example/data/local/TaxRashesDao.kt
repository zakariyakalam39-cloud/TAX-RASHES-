package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxRashesDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET walletBalance = :newBalance WHERE id = 1")
    suspend fun updateWalletBalance(newBalance: Double)

    @Query("UPDATE user_profile SET prizeBalance = :newPrize WHERE id = 1")
    suspend fun updatePrizeBalance(newPrize: Double)

    @Query("UPDATE user_profile SET matchesJoined = matchesJoined + 1 WHERE id = 1")
    suspend fun incrementMatchesJoined()

    // Tournaments
    @Query("SELECT * FROM tournaments WHERE enabled = 1 ORDER BY createdAt DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getTournamentById(id: String): TournamentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournaments(tournaments: List<TournamentEntity>)

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    @Query("UPDATE tournaments SET status = :status WHERE id = :id")
    suspend fun updateTournamentStatus(id: String, status: String)

    @Query("UPDATE tournaments SET roomId = :roomId, roomPassword = :roomPassword WHERE id = :id")
    suspend fun updateTournamentRoom(id: String, roomId: String, roomPassword: String)

    @Query("UPDATE tournaments SET filledSlots = filledSlots + 1 WHERE id = :id")
    suspend fun incrementFilledSlots(id: String)

    // Registrations
    @Query("SELECT * FROM registrations ORDER BY timestamp DESC")
    fun getAllRegistrations(): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE tournamentId = :tournamentId")
    suspend fun getRegistrationsForTournament(tournamentId: String): List<RegistrationEntity>

    @Query("SELECT COUNT(*) FROM registrations WHERE tournamentId = :tournamentId")
    fun isRegisteredForTournament(tournamentId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: RegistrationEntity)

    // Top-Up Products
    @Query("SELECT * FROM topup_products WHERE enabled = 1 ORDER BY price ASC")
    fun getAllProducts(): Flow<List<TopUpProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<TopUpProductEntity>)

    // Wallet Transactions
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransactionEntity)

    // Payment Methods
    @Query("SELECT * FROM payment_methods WHERE enabled = 1")
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethods(methods: List<PaymentMethodEntity>)

    // Deposit Requests
    @Query("SELECT * FROM deposit_requests ORDER BY timestamp DESC")
    fun getAllDeposits(): Flow<List<DepositRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: DepositRequestEntity): Long

    @Query("UPDATE deposit_requests SET status = :status WHERE id = :id")
    suspend fun updateDepositStatus(id: Long, status: String)

    // Withdrawal Requests
    @Query("SELECT * FROM withdrawal_requests ORDER BY timestamp DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawalRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: WithdrawalRequestEntity): Long

    @Query("UPDATE withdrawal_requests SET status = :status WHERE id = :id")
    suspend fun updateWithdrawalStatus(id: Long, status: String)

    // Support Tickets
    @Query("SELECT * FROM support_tickets ORDER BY timestamp DESC")
    fun getAllSupportTickets(): Flow<List<SupportTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportTicket(ticket: SupportTicketEntity)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()
}
