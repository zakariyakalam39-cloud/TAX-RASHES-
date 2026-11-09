package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val game: String = "Free Fire",
    val type: String, // Solo, Duo, Squad
    val entryFee: Double,
    val prizePool: Double,
    val schedule: String,
    val roomReleaseAt: String = "",
    val roomId: String = "",
    val roomPassword: String = "",
    val status: String = "upcoming", // upcoming, live, completed
    val maxSlots: Int = 48,
    val filledSlots: Int = 0,
    val mapName: String = "Bermuda",
    val rules: String = "No emulator, No hacks. Room ID/Password released 15 mins before match.",
    val enabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "topup_products")
data class TopUpProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val diamonds: Int,
    val bonusDiamonds: Int = 0,
    val price: Double,
    val description: String = "",
    val category: String = "Diamonds", // Diamonds, Membership, LevelUp
    val enabled: Boolean = true
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // deposit, withdrawal, tournament_fee, tournament_prize, topup_purchase
    val amount: Double,
    val status: String = "completed", // completed, pending, rejected
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey val method: String, // bKash, Nagad, Rocket
    val number: String,
    val label: String, // Personal / Merchant
    val instructions: String,
    val enabled: Boolean = true
)

@Entity(tableName = "deposit_requests")
data class DepositRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val method: String,
    val amount: Double,
    val transactionId: String,
    val senderNumber: String,
    val status: String = "pending", // pending, approved, rejected
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawal_requests")
data class WithdrawalRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val method: String,
    val amount: Double,
    val destinationNumber: String,
    val status: String = "pending", // pending, approved, rejected
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "registrations")
data class RegistrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tournamentId: String,
    val tournamentTitle: String,
    val playerUid: String,
    val playerName: String,
    val teamName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val message: String,
    val reply: String = "",
    val status: String = "open", // open, answered, closed
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String = "general", // room_alert, wallet, match
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "ProGamer_BD",
    val email: String = "player@taxrashes.com",
    val phone: String = "+880 1712 345678",
    val inGameUid: String = "2891481023",
    val walletBalance: Double = 350.0,
    val prizeBalance: Double = 120.0,
    val matchesJoined: Int = 14,
    val matchesWon: Int = 5
)
