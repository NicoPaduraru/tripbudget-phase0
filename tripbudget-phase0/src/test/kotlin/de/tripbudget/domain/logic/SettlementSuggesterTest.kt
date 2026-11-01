package de.tripbudget.domain.logic

import de.tripbudget.domain.model.Transfer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class SettlementSuggesterTest {

    @Test
    fun `ausgeglichene Gruppe braucht keine Überweisung`() {
        assertEquals(emptyList<Transfer>(), SettlementSuggester.suggest(mapOf(1L to 0L, 2L to 0L)))
    }

    @Test
    fun `zwei Personen - eine Überweisung`() {
        val transfers = SettlementSuggester.suggest(mapOf(1L to 2500L, 2L to -2500L))
        assertEquals(listOf(Transfer(fromTravelerId = 2, toTravelerId = 1, amountCents = 2500)), transfers)
    }

    @Test
    fun `ein Gläubiger, zwei Schuldner`() {
        val transfers = SettlementSuggester.suggest(mapOf(1L to 6000L, 2L to -3000L, 3L to -3000L))
        assertEquals(2, transfers.size)
        assertTrue(transfers.all { it.toTravelerId == 1L && it.amountCents == 3000L })
    }

    @Test
    fun `nach den Überweisungen sind alle bei 0 - Zufallstest`() {
        // Wir erzeugen 500 zufällige Gruppen und prüfen zwei Regeln:
        //   1. Nach allen Überweisungen hat jeder Saldo 0.
        //   2. Es gibt höchstens (Personen − 1) Überweisungen.
        val random = Random(seed = 42) // fester Seed → Test ist wiederholbar
        repeat(500) {
            val people = random.nextInt(2, 9)
            val balances = randomBalances(people, random)

            val transfers = SettlementSuggester.suggest(balances)

            val after = balances.toMutableMap()
            for (t in transfers) {
                after[t.fromTravelerId] = after.getValue(t.fromTravelerId) + t.amountCents
                after[t.toTravelerId] = after.getValue(t.toTravelerId) - t.amountCents
            }
            assertTrue(after.values.all { it == 0L }, "Nicht ausgeglichen: $balances → $after")
            assertTrue(transfers.size <= people - 1, "Zu viele Überweisungen: ${transfers.size}")
        }
    }

    @Test
    fun `Salden mit Summe ungleich 0 werden abgelehnt`() {
        assertThrows<IllegalArgumentException> { SettlementSuggester.suggest(mapOf(1L to 100L)) }
    }

    /** Zufällige Salden, die zusammen genau 0 ergeben. */
    private fun randomBalances(people: Int, random: Random): Map<Long, Long> {
        val values = MutableList(people - 1) { random.nextLong(-50_000, 50_000) }
        values += -values.sum() // letzte Person gleicht aus → Summe 0
        return values.mapIndexed { i, v -> (i + 1).toLong() to v }.toMap()
    }
}
