package de.tripbudget.domain.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/*
 * Unit-Tests prüfen automatisch, ob unser Code das Richtige tut.
 * Aufbau jedes Tests (Arrange – Act – Assert):
 *   1. Vorbereiten, 2. Ausführen, 3. Ergebnis prüfen.
 *
 * KOTLIN-TRICK: Funktionsnamen in `Backticks` dürfen Leerzeichen enthalten.
 * So liest sich der Testbericht wie ein Satz.
 */
class CostSplitterTest {

    @Test
    fun `teilt glatte Beträge gleichmäßig`() {
        val shares = CostSplitter.splitEvenly(900, listOf(1, 2, 3))
        assertEquals(listOf(300L, 300L, 300L), shares.map { it.amountCents })
    }

    @Test
    fun `verteilt den Restcent auf die ersten Personen`() {
        val shares = CostSplitter.splitEvenly(1000, listOf(1, 2, 3))
        assertEquals(listOf(334L, 333L, 333L), shares.map { it.amountCents })
    }

    @Test
    fun `Summe der Anteile ist immer genau der Betrag`() {
        // Wir probieren viele Kombinationen aus – so finden Tests auch seltene Fehler.
        for (amount in 1L..2000L step 7) {
            for (people in 1..7) {
                val ids = (1L..people.toLong()).toList()
                val shares = CostSplitter.splitEvenly(amount, ids)
                assertEquals(amount, shares.sumOf { it.amountCents }, "Betrag $amount, $people Personen")
            }
        }
    }

    @Test
    fun `eine Person bekommt alles`() {
        val shares = CostSplitter.splitEvenly(1234, listOf(5))
        assertEquals(1234L, shares.single().amountCents)
    }

    @Test
    fun `lehnt ungültige Eingaben ab`() {
        assertThrows<IllegalArgumentException> { CostSplitter.splitEvenly(0, listOf(1)) }
        assertThrows<IllegalArgumentException> { CostSplitter.splitEvenly(100, emptyList()) }
        assertThrows<IllegalArgumentException> { CostSplitter.splitEvenly(100, listOf(1, 1)) }
    }
}
