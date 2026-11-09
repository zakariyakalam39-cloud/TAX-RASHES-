package com.example.data.repository

import com.example.data.local.TaxRashesDao
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
import kotlinx.coroutines.flow.firstOrNull

class TaxRashesRepository(private val dao: TaxRashesDao) {

    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val allTournaments: Flow<List<TournamentEntity>> = dao.getAllTournaments()
    val allProducts: Flow<List<TopUpProductEntity>> = dao.getAllProducts()
    val allTransactions: Flow<List<WalletTransactionEntity>> = dao.getAllTransactions()
    val paymentMethods: Flow<List<PaymentMethodEntity>> = dao.getAllPaymentMethods()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val allSupportTickets: Flow<List<SupportTicketEntity>> = dao.getAllSupportTickets()
    val allDeposits: Flow<List<DepositRequestEntity>> = dao.getAllDeposits()
    val allWithdrawals: Flow<List<WithdrawalRequestEntity>> = dao.getAllWithdrawals()
    val allRegistrations: Flow<List<RegistrationEntity>> = dao.getAllRegistrations()

    suspend fun registerForTournament(
        tournament: TournamentEntity,
        playerUid: String,
        playerName: String,
        teamName: String
    ): Result<Unit> {
        val profile = dao.getUserProfile().firstOrNull()
            ?: return Result.failure(Exception("User profile not found"))

        if (profile.walletBalance < tournament.entryFee) {
            return Result.failure(Exception("Insufficient wallet balance. Please add funds."))
        }

        // Deduct fee
        val newBalance = profile.walletBalance - tournament.entryFee
        dao.updateWalletBalance(newBalance)
        dao.incrementMatchesJoined()
        dao.incrementFilledSlots(tournament.id)

        // Save registration
        dao.insertRegistration(
            RegistrationEntity(
                tournamentId = tournament.id,
                tournamentTitle = tournament.title,
                playerUid = playerUid,
                playerName = playerName,
                teamName = teamName
            )
        )

        // Record transaction
        dao.insertTransaction(
            WalletTransactionEntity(
                type = "tournament_fee",
                amount = -tournament.entryFee,
                status = "completed",
                note = "Registered: ${tournament.title}"
            )
        )

        // Add confirmation notification
        dao.insertNotification(
            NotificationEntity(
                title = "Registered Successfully!",
                message = "You have registered for '${tournament.title}'. Check room details before match time.",
                type = "match"
            )
        )

        return Result.success(Unit)
    }

    suspend fun buyTopUpProduct(
        product: TopUpProductEntity,
        playerUid: String,
        serverRegion: String
    ): Result<Unit> {
        val profile = dao.getUserProfile().firstOrNull()
            ?: return Result.failure(Exception("User profile not found"))

        if (profile.walletBalance < product.price) {
            return Result.failure(Exception("Insufficient wallet balance. Please deposit funds to continue."))
        }

        // Deduct cost
        val newBalance = profile.walletBalance - product.price
        dao.updateWalletBalance(newBalance)

        // Record transaction
        dao.insertTransaction(
            WalletTransactionEntity(
                type = "topup_purchase",
                amount = -product.price,
                status = "completed",
                note = "${product.title} (UID: $playerUid - $serverRegion)"
            )
        )

        // Add notification
        dao.insertNotification(
            NotificationEntity(
                title = "Top-Up Order Placed",
                message = "${product.diamonds + product.bonusDiamonds} Diamonds sent to UID $playerUid ($serverRegion).",
                type = "wallet"
            )
        )

        return Result.success(Unit)
    }

    suspend fun requestDeposit(
        method: String,
        amount: Double,
        transactionId: String,
        senderNumber: String
    ): Result<Unit> {
        if (amount <= 0) return Result.failure(Exception("Invalid amount"))
        if (transactionId.isBlank()) return Result.failure(Exception("Transaction ID is required"))

        dao.insertDeposit(
            DepositRequestEntity(
                method = method,
                amount = amount,
                transactionId = transactionId.trim(),
                senderNumber = senderNumber.trim(),
                status = "pending"
            )
        )

        dao.insertTransaction(
            WalletTransactionEntity(
                type = "deposit",
                amount = amount,
                status = "pending",
                note = "$method Deposit Pending (Txn: $transactionId)"
            )
        )

        dao.insertNotification(
            NotificationEntity(
                title = "Deposit Submitted",
                message = "৳$amount deposit via $method submitted for admin verification.",
                type = "wallet"
            )
        )

        return Result.success(Unit)
    }

    suspend fun requestWithdrawal(
        method: String,
        amount: Double,
        destinationNumber: String
    ): Result<Unit> {
        val profile = dao.getUserProfile().firstOrNull()
            ?: return Result.failure(Exception("User profile not found"))

        if (amount <= 0) return Result.failure(Exception("Invalid amount"))
        if (amount > profile.prizeBalance) {
            return Result.failure(Exception("Withdrawal amount exceeds available prize balance (৳${profile.prizeBalance})."))
        }
        if (destinationNumber.isBlank()) return Result.failure(Exception("Phone number is required"))

        // Deduct prize balance immediately
        dao.updatePrizeBalance(profile.prizeBalance - amount)

        dao.insertWithdrawal(
            WithdrawalRequestEntity(
                method = method,
                amount = amount,
                destinationNumber = destinationNumber.trim(),
                status = "pending"
            )
        )

        dao.insertTransaction(
            WalletTransactionEntity(
                type = "withdrawal",
                amount = -amount,
                status = "pending",
                note = "$method Withdrawal to $destinationNumber"
            )
        )

        dao.insertNotification(
            NotificationEntity(
                title = "Withdrawal Requested",
                message = "৳$amount withdrawal request sent to $destinationNumber ($method).",
                type = "wallet"
            )
        )

        return Result.success(Unit)
    }

    suspend fun updateProfile(
        name: String,
        phone: String,
        inGameUid: String
    ) {
        val current = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        dao.insertOrUpdateProfile(
            current.copy(
                name = name.trim(),
                phone = phone.trim(),
                inGameUid = inGameUid.trim()
            )
        )
    }

    suspend fun submitSupportTicket(subject: String, message: String) {
        dao.insertSupportTicket(
            SupportTicketEntity(
                subject = subject.trim(),
                message = message.trim(),
                status = "open"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "Support Ticket Created",
                message = "Your ticket '$subject' was submitted to TAX RASHES helpdesk.",
                type = "general"
            )
        )
    }

    suspend fun markNotificationAsRead(id: Long) {
        dao.markNotificationAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        dao.markAllNotificationsAsRead()
    }

    // --- Admin / Moderator Simulation Tools ---
    suspend fun adminApproveDeposit(depositId: Long) {
        val deposits = dao.getAllDeposits().firstOrNull() ?: emptyList()
        val target = deposits.find { it.id == depositId } ?: return

        dao.updateDepositStatus(depositId, "approved")

        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.updateWalletBalance(profile.walletBalance + target.amount)

        dao.insertTransaction(
            WalletTransactionEntity(
                type = "deposit",
                amount = target.amount,
                status = "completed",
                note = "Approved: ${target.method} Deposit (Txn: ${target.transactionId})"
            )
        )

        dao.insertNotification(
            NotificationEntity(
                title = "Deposit Approved! ৳${target.amount}",
                message = "Your ${target.method} deposit of ৳${target.amount} was approved and credited.",
                type = "wallet"
            )
        )
    }

    suspend fun adminRejectDeposit(depositId: Long) {
        dao.updateDepositStatus(depositId, "rejected")
    }

    suspend fun adminApproveWithdrawal(withdrawalId: Long) {
        dao.updateWithdrawalStatus(withdrawalId, "approved")
        dao.insertNotification(
            NotificationEntity(
                title = "Withdrawal Dispatched!",
                message = "Your withdrawal has been sent to your mobile wallet.",
                type = "wallet"
            )
        )
    }

    suspend fun adminUpdateTournamentStatus(
        tournamentId: String,
        status: String,
        roomId: String = "",
        roomPass: String = ""
    ) {
        dao.updateTournamentStatus(tournamentId, status)
        if (roomId.isNotBlank()) {
            dao.updateTournamentRoom(tournamentId, roomId, roomPass)
            dao.insertNotification(
                NotificationEntity(
                    title = "Room Code Released!",
                    message = "Tournament ID $tournamentId room credentials: Room ID: $roomId, Pass: $roomPass.",
                    type = "room_alert"
                )
            )
        }
    }

    suspend fun adminAddDemoBalance(amount: Double) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.updateWalletBalance(profile.walletBalance + amount)
        dao.insertTransaction(
            WalletTransactionEntity(
                type = "deposit",
                amount = amount,
                status = "completed",
                note = "Admin Demo Balance Added"
            )
        )
    }
}
