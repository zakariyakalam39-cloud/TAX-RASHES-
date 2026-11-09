package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TournamentEntity::class,
        TopUpProductEntity::class,
        WalletTransactionEntity::class,
        PaymentMethodEntity::class,
        DepositRequestEntity::class,
        WithdrawalRequestEntity::class,
        RegistrationEntity::class,
        SupportTicketEntity::class,
        NotificationEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TaxRashesDatabase : RoomDatabase() {
    abstract fun dao(): TaxRashesDao

    companion object {
        @Volatile
        private var INSTANCE: TaxRashesDatabase? = null

        fun getDatabase(context: Context): TaxRashesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaxRashesDatabase::class.java,
                    "tax_rashes_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate starter data on first creation
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedDatabase(database.dao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(dao: TaxRashesDao) {
            // Seed User Profile
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "T4X_SniperBD",
                    email = "player@taxrashes.com",
                    phone = "01789123456",
                    inGameUid = "294820184",
                    walletBalance = 350.0,
                    prizeBalance = 150.0,
                    matchesJoined = 18,
                    matchesWon = 7
                )
            )

            // Seed Tournaments
            val tournaments = listOf(
                TournamentEntity(
                    id = "tour_101",
                    title = "Bermuda Scrims Grand Solo #42",
                    game = "Free Fire",
                    type = "Solo",
                    entryFee = 35.0,
                    prizePool = 1200.0,
                    schedule = "Today, 9:00 PM (BST)",
                    roomReleaseAt = "8:45 PM",
                    roomId = "8920194",
                    roomPassword = "tax99",
                    status = "upcoming",
                    maxSlots = 48,
                    filledSlots = 36,
                    mapName = "Bermuda",
                    rules = "Only Mobile players allowed. Emulator strictly prohibited."
                ),
                TournamentEntity(
                    id = "tour_102",
                    title = "Purgatory Duo Championship Showdown",
                    game = "Free Fire",
                    type = "Duo",
                    entryFee = 60.0,
                    prizePool = 2400.0,
                    schedule = "Live Now",
                    roomReleaseAt = "Released",
                    roomId = "7718291",
                    roomPassword = "rash8",
                    status = "live",
                    maxSlots = 24,
                    filledSlots = 24,
                    mapName = "Purgatory",
                    rules = "Both teammates must register. Room will start at 9:15 PM sharp."
                ),
                TournamentEntity(
                    id = "tour_103",
                    title = "Kalahari Squad Rush Knockout",
                    game = "Free Fire",
                    type = "Squad",
                    entryFee = 100.0,
                    prizePool = 3500.0,
                    schedule = "Tomorrow, 8:30 PM (BST)",
                    roomReleaseAt = "8:15 PM",
                    roomId = "",
                    roomPassword = "",
                    status = "upcoming",
                    maxSlots = 12,
                    filledSlots = 8,
                    mapName = "Kalahari",
                    rules = "Squad leader must submit roster player UIDs before match start."
                ),
                TournamentEntity(
                    id = "tour_104",
                    title = "Clash Squad 4v4 High Stakes Cup",
                    game = "Free Fire",
                    type = "Squad",
                    entryFee = 150.0,
                    prizePool = 5000.0,
                    schedule = "Yesterday, 10:00 PM",
                    roomReleaseAt = "Closed",
                    roomId = "6619024",
                    roomPassword = "win4",
                    status = "completed",
                    maxSlots = 16,
                    filledSlots = 16,
                    mapName = "Bermuda Remastered",
                    rules = "Tournament concluded. Winner: Team TAX_Viper (Prize ৳3000)."
                ),
                TournamentEntity(
                    id = "tour_105",
                    title = "Erangel Masters Squad Championship",
                    game = "PUBG Mobile",
                    type = "Squad",
                    entryFee = 80.0,
                    prizePool = 3200.0,
                    schedule = "Tonight, 10:30 PM (BST)",
                    roomReleaseAt = "10:15 PM",
                    roomId = "",
                    roomPassword = "",
                    status = "upcoming",
                    maxSlots = 25,
                    filledSlots = 19,
                    mapName = "Erangel",
                    rules = "Classic Squad match. Mobile only, emulator detection enabled."
                ),
                TournamentEntity(
                    id = "tour_106",
                    title = "Sanhok Bootcamp Solo Free Scrim",
                    game = "PUBG Mobile",
                    type = "Solo",
                    entryFee = 0.0,
                    prizePool = 600.0,
                    schedule = "Live Now",
                    roomReleaseAt = "Released",
                    roomId = "5519283",
                    roomPassword = "pubg1",
                    status = "live",
                    maxSlots = 100,
                    filledSlots = 94,
                    mapName = "Sanhok",
                    rules = "Free entry for all players! Top 3 share prize pool."
                ),
                TournamentEntity(
                    id = "tour_107",
                    title = "Crash 5v5 S&D Tactical Cup",
                    game = "Call of Duty: Mobile",
                    type = "Squad",
                    entryFee = 120.0,
                    prizePool = 4000.0,
                    schedule = "Tomorrow, 9:00 PM",
                    roomReleaseAt = "8:45 PM",
                    roomId = "",
                    roomPassword = "",
                    status = "upcoming",
                    maxSlots = 16,
                    filledSlots = 11,
                    mapName = "Crash",
                    rules = "Search & Destroy competitive rules. Operators restricted."
                ),
                TournamentEntity(
                    id = "tour_108",
                    title = "Isolated Battle Royale Duo Sprint",
                    game = "Call of Duty: Mobile",
                    type = "Duo",
                    entryFee = 45.0,
                    prizePool = 1600.0,
                    schedule = "Yesterday, 8:00 PM",
                    roomReleaseAt = "Closed",
                    roomId = "4418291",
                    roomPassword = "codm8",
                    status = "completed",
                    maxSlots = 50,
                    filledSlots = 50,
                    mapName = "Isolated",
                    rules = "Match completed. Winner: Team Ghost_BD (Prize ৳1000)."
                )
            )
            dao.insertTournaments(tournaments)

            // Seed Top-Up Products
            val products = listOf(
                TopUpProductEntity(
                    id = "prod_1",
                    title = "115 Diamonds",
                    diamonds = 115,
                    bonusDiamonds = 10,
                    price = 85.0,
                    description = "Instant in-game top-up directly to your Free Fire Player UID.",
                    category = "Diamonds"
                ),
                TopUpProductEntity(
                    id = "prod_2",
                    title = "240 Diamonds Bundle",
                    diamonds = 240,
                    bonusDiamonds = 25,
                    price = 170.0,
                    description = "Best value weekly diamond package with bonus diamonds.",
                    category = "Diamonds"
                ),
                TopUpProductEntity(
                    id = "prod_3",
                    title = "610 Diamonds + Bonus",
                    diamonds = 610,
                    bonusDiamonds = 60,
                    price = 425.0,
                    description = "Popular bundle for Elite Pass unlock and legendary crate spins.",
                    category = "Diamonds"
                ),
                TopUpProductEntity(
                    id = "prod_4",
                    title = "1240 Diamonds Mega Pack",
                    diamonds = 1240,
                    bonusDiamonds = 150,
                    price = 840.0,
                    description = "Pro streamer bundle with maximum discount and VIP bonus.",
                    category = "Diamonds"
                ),
                TopUpProductEntity(
                    id = "prod_5",
                    title = "Weekly Membership Pass",
                    diamonds = 450,
                    bonusDiamonds = 0,
                    price = 195.0,
                    description = "Receive instant diamonds + daily claimable diamond rewards for 7 days.",
                    category = "Membership"
                ),
                TopUpProductEntity(
                    id = "prod_6",
                    title = "Monthly Membership Pass",
                    diamonds = 2600,
                    bonusDiamonds = 0,
                    price = 790.0,
                    description = "30-day VIP pass with special badge and daily bonus diamonds.",
                    category = "Membership"
                ),
                TopUpProductEntity(
                    id = "prod_7",
                    title = "Level Up Pass",
                    diamonds = 800,
                    bonusDiamonds = 0,
                    price = 180.0,
                    description = "One-time purchase that unlocks 800+ diamonds as your account levels up.",
                    category = "LevelUp"
                )
            )
            dao.insertProducts(products)

            // Seed Payment Methods (bKash, Nagad, Rocket)
            val paymentMethods = listOf(
                PaymentMethodEntity(
                    method = "bKash",
                    number = "01789-234567",
                    label = "Personal (Send Money)",
                    instructions = "Go to bKash app -> Send Money to the number above -> Enter your TAX RASHES UID as reference -> Copy & paste Transaction ID below."
                ),
                PaymentMethodEntity(
                    method = "Nagad",
                    number = "01812-345678",
                    label = "Personal (Send Money)",
                    instructions = "Open Nagad app -> Choose Send Money -> Send exact amount -> Note down Txn ID and submit form."
                ),
                PaymentMethodEntity(
                    method = "Rocket",
                    number = "01999-876543-2",
                    label = "Personal",
                    instructions = "Dial *322# or use Rocket app -> Send Money to the rocket 12-digit number."
                )
            )
            dao.insertPaymentMethods(paymentMethods)

            // Seed Initial Transactions
            val transactions = listOf(
                WalletTransactionEntity(
                    type = "deposit",
                    amount = 250.0,
                    status = "completed",
                    note = "bKash Deposit (Txn: 9KJ83H4Z)"
                ),
                WalletTransactionEntity(
                    type = "tournament_prize",
                    amount = 150.0,
                    status = "completed",
                    note = "1st Place Winner - Solo Scrims #39"
                ),
                WalletTransactionEntity(
                    type = "tournament_fee",
                    amount = -50.0,
                    status = "completed",
                    note = "Entry fee: Duo Championship #40"
                )
            )
            transactions.forEach { dao.insertTransaction(it) }

            // Seed Initial Notifications
            val notifications = listOf(
                NotificationEntity(
                    title = "Welcome to TAX RASHES!",
                    message = "Your esports account is active. Join daily tournaments, top-up diamonds, and win real prize money.",
                    type = "general",
                    isRead = false
                ),
                NotificationEntity(
                    title = "Room Code Released!",
                    message = "Tournament 'Purgatory Duo Championship Showdown' is now LIVE. Room ID: 7718291, Pass: rash8.",
                    type = "room_alert",
                    isRead = false
                ),
                NotificationEntity(
                    title = "Deposit Approved",
                    message = "৳250.00 has been credited to your wallet balance via bKash.",
                    type = "wallet",
                    isRead = true
                )
            )
            notifications.forEach { dao.insertNotification(it) }
        }
    }
}
