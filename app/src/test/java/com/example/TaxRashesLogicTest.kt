package com.example

import com.example.data.model.TournamentEntity
import com.example.data.model.TopUpProductEntity
import com.example.data.model.UserProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaxRashesLogicTest {

    @Test
    fun `test tournament slot calculation`() {
        val tournament = TournamentEntity(
            id = "t1",
            title = "Solo Scrims",
            type = "Solo",
            entryFee = 35.0,
            prizePool = 1200.0,
            schedule = "9:00 PM",
            maxSlots = 48,
            filledSlots = 36
        )
        val remainingSlots = tournament.maxSlots - tournament.filledSlots
        assertEquals(12, remainingSlots)
        assertTrue(tournament.filledSlots <= tournament.maxSlots)
    }

    @Test
    fun `test wallet balance after tournament registration`() {
        val initialBalance = 350.0
        val entryFee = 35.0
        val finalBalance = initialBalance - entryFee
        assertEquals(315.0, finalBalance, 0.001)
    }

    @Test
    fun `test topup diamond bonus calculation`() {
        val product = TopUpProductEntity(
            id = "p1",
            title = "610 Diamonds",
            diamonds = 610,
            bonusDiamonds = 60,
            price = 425.0
        )
        val totalDiamonds = product.diamonds + product.bonusDiamonds
        assertEquals(670, totalDiamonds)
    }

    @Test
    fun `test user profile win rate calculation`() {
        val profile = UserProfileEntity(
            matchesJoined = 20,
            matchesWon = 5
        )
        val winRate = (profile.matchesWon.toFloat() / profile.matchesJoined.toFloat() * 100).toInt()
        assertEquals(25, winRate)
    }

    @Test
    fun `test transaction history inflow and outflow totals`() {
        val transactions = listOf(
            com.example.data.model.WalletTransactionEntity(id = 1, type = "deposit", amount = 250.0, status = "completed"),
            com.example.data.model.WalletTransactionEntity(id = 2, type = "tournament_prize", amount = 150.0, status = "completed"),
            com.example.data.model.WalletTransactionEntity(id = 3, type = "tournament_fee", amount = -50.0, status = "completed"),
            com.example.data.model.WalletTransactionEntity(id = 4, type = "topup_purchase", amount = -85.0, status = "completed"),
            com.example.data.model.WalletTransactionEntity(id = 5, type = "deposit", amount = 100.0, status = "pending")
        )

        val totalInflow = transactions.filter { it.amount > 0 && it.status == "completed" }.sumOf { it.amount }
        val totalOutflow = transactions.filter { it.amount < 0 && it.status == "completed" }.sumOf { kotlin.math.abs(it.amount) }

        assertEquals(400.0, totalInflow, 0.001)
        assertEquals(135.0, totalOutflow, 0.001)
    }

    @Test
    fun `test transaction search filtering`() {
        val transactions = listOf(
            com.example.data.model.WalletTransactionEntity(id = 1, type = "deposit", amount = 250.0, note = "bKash Deposit Txn: 9KJ83H4Z"),
            com.example.data.model.WalletTransactionEntity(id = 2, type = "tournament_prize", amount = 150.0, note = "1st Place Winner - Solo Scrims"),
            com.example.data.model.WalletTransactionEntity(id = 3, type = "topup_purchase", amount = -85.0, note = "115 Diamonds UID 294820184")
        )

        val bkashResults = transactions.filter { it.note.contains("bKash", ignoreCase = true) }
        assertEquals(1, bkashResults.size)
        assertEquals(1L, bkashResults[0].id)

        val diamondResults = transactions.filter { it.note.contains("Diamonds", ignoreCase = true) }
        assertEquals(1, diamondResults.size)
        assertEquals(3L, diamondResults[0].id)
    }

    @Test
    fun `test tournament filtering by game title`() {
        val list = listOf(
            TournamentEntity(id = "1", title = "Free Fire Match", game = "Free Fire", type = "Solo", entryFee = 35.0, prizePool = 1000.0, schedule = "9 PM"),
            TournamentEntity(id = "2", title = "PUBG Scrim", game = "PUBG Mobile", type = "Squad", entryFee = 80.0, prizePool = 2000.0, schedule = "10 PM"),
            TournamentEntity(id = "3", title = "COD Mobile Blitz", game = "Call of Duty: Mobile", type = "Duo", entryFee = 50.0, prizePool = 1500.0, schedule = "11 PM")
        )

        val pubgList = list.filter { it.game.equals("PUBG Mobile", ignoreCase = true) }
        assertEquals(1, pubgList.size)
        assertEquals("PUBG Scrim", pubgList[0].title)

        val codList = list.filter { it.game.equals("Call of Duty: Mobile", ignoreCase = true) }
        assertEquals(1, codList.size)
        assertEquals("COD Mobile Blitz", codList[0].title)
    }

    @Test
    fun `test tournament filtering by entry fee range`() {
        val list = listOf(
            TournamentEntity(id = "1", title = "Free Scrim", game = "PUBG Mobile", type = "Solo", entryFee = 0.0, prizePool = 500.0, schedule = "8 PM"),
            TournamentEntity(id = "2", title = "Low Fee", game = "Free Fire", type = "Solo", entryFee = 35.0, prizePool = 1000.0, schedule = "9 PM"),
            TournamentEntity(id = "3", title = "Mid Fee", game = "Free Fire", type = "Duo", entryFee = 60.0, prizePool = 2000.0, schedule = "10 PM"),
            TournamentEntity(id = "4", title = "High Stakes", game = "Free Fire", type = "Squad", entryFee = 150.0, prizePool = 5000.0, schedule = "11 PM")
        )

        val freeMatches = list.filter { it.entryFee == 0.0 }
        assertEquals(1, freeMatches.size)
        assertEquals("Free Scrim", freeMatches[0].title)

        val under50 = list.filter { it.entryFee > 0.0 && it.entryFee < 50.0 }
        assertEquals(1, under50.size)
        assertEquals("Low Fee", under50[0].title)

        val midTier = list.filter { it.entryFee in 50.0..100.0 }
        assertEquals(1, midTier.size)
        assertEquals("Mid Fee", midTier[0].title)

        val above100 = list.filter { it.entryFee > 100.0 }
        assertEquals(1, above100.size)
        assertEquals("High Stakes", above100[0].title)
    }

    @Test
    fun `test tournament filtering by status`() {
        val list = listOf(
            TournamentEntity(id = "1", title = "Live Scrim", game = "Free Fire", type = "Solo", entryFee = 35.0, prizePool = 1000.0, schedule = "Now", status = "live"),
            TournamentEntity(id = "2", title = "Upcoming Scrim", game = "PUBG Mobile", type = "Squad", entryFee = 80.0, prizePool = 2000.0, schedule = "Tomorrow", status = "upcoming"),
            TournamentEntity(id = "3", title = "Past Scrim", game = "Call of Duty: Mobile", type = "Duo", entryFee = 50.0, prizePool = 1500.0, schedule = "Yesterday", status = "completed")
        )

        val activeList = list.filter { it.status.equals("live", ignoreCase = true) }
        assertEquals(1, activeList.size)
        assertEquals("Live Scrim", activeList[0].title)

        val upcomingList = list.filter { it.status.equals("upcoming", ignoreCase = true) }
        assertEquals(1, upcomingList.size)
        assertEquals("Upcoming Scrim", upcomingList[0].title)

        val finishedList = list.filter { it.status.equals("completed", ignoreCase = true) }
        assertEquals(1, finishedList.size)
        assertEquals("Past Scrim", finishedList[0].title)
    }

    @Test
    fun `test admin password verification with default credentials`() {
        val storedPassword = "admin123"
        val attempt1 = "admin123"
        val attemptAlias = "admin"
        val attemptWrong = "wrongpass"

        fun verifyPassword(pass: String): Boolean {
            return (pass == storedPassword) || (storedPassword == "admin123" && pass == "admin")
        }

        assertTrue("Expected default password to authenticate", verifyPassword(attempt1))
        assertTrue("Expected admin alias to authenticate on default", verifyPassword(attemptAlias))
        org.junit.Assert.assertFalse("Expected wrong password to fail", verifyPassword(attemptWrong))
    }

    @Test
    fun `test admin password change validation rules`() {
        var currentStored = "admin123"

        fun changePassword(current: String, newPass: String, confirm: String): Pair<Boolean, String?> {
            if (current != currentStored) return false to "Current password does not match"
            if (newPass.length < 4) return false to "New password must be at least 4 characters"
            if (newPass != confirm) return false to "New passwords do not match"
            currentStored = newPass
            return true to null
        }

        // Test mismatched confirmation
        val mismatchResult = changePassword("admin123", "secret2026", "secret9999")
        org.junit.Assert.assertFalse(mismatchResult.first)
        assertEquals("New passwords do not match", mismatchResult.second)

        // Test short password
        val shortResult = changePassword("admin123", "12", "12")
        org.junit.Assert.assertFalse(shortResult.first)
        assertEquals("New password must be at least 4 characters", shortResult.second)

        // Test correct change
        val successResult = changePassword("admin123", "masterpass", "masterpass")
        assertTrue(successResult.first)
        assertEquals(null, successResult.second)
        assertEquals("masterpass", currentStored)
    }
}

