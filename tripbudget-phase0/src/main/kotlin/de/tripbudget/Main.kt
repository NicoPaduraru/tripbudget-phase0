package de.tripbudget

import de.tripbudget.domain.logic.BalanceCalculator
import de.tripbudget.domain.logic.CostSplitter
import de.tripbudget.domain.logic.SettlementSuggester
import de.tripbudget.domain.model.Expense
import de.tripbudget.domain.model.Traveler
import de.tripbudget.domain.util.MoneyFormatter

/*
 * Kleines Demo-Programm: ein Wochenende in Lissabon mit drei Personen.
 * Starten mit:  ./gradlew run   (Windows: gradlew.bat run)
 *
 * KOTLIN VS. JAVA: Kein "public static void main(String[] args)" in einer
 * Klasse nötig – eine einfache Funktion main() reicht.
 */
fun main() {
    val anna = Traveler(id = 1, name = "Anna")
    val ben = Traveler(id = 2, name = "Ben")
    val clara = Traveler(id = 3, name = "Clara")
    val travelers = listOf(anna, ben, clara)
    val everyone = travelers.map { it.id }

    val expenses = listOf(
        Expense(1, "Unterkunft", anna.id, 30000, CostSplitter.splitEvenly(30000, everyone)),
        Expense(2, "Abendessen", ben.id, 9000, CostSplitter.splitEvenly(9000, everyone)),
        Expense(3, "Taxi (nur Anna & Clara)", clara.id, 2500, CostSplitter.splitEvenly(2500, listOf(anna.id, clara.id))),
        Expense(4, "Museum", ben.id, 1000, CostSplitter.splitEvenly(1000, everyone)),
    )

    // associateBy baut eine Map: id → Traveler, damit wir Namen schnell finden konnen.
    val byId = travelers.associateBy { it.id }
    fun name(id: Long) = byId.getValue(id).name // lokale Funktion mit "="-Kurzform

    println("=== Ausgaben ===")
    for (e in expenses) {
        println("${e.description}: ${MoneyFormatter.format(e.amountCents)}, bezahlt von ${name(e.paidByTravelerId)}")
    }

    val balances = BalanceCalculator.calculate(travelers, expenses)
    println("\n=== Salden ===")
    for ((id, cents) in balances) { // Destructuring: Map-Eintrag direkt in id und cents zerlegen
        val status = when { // "when" ist ein mächtigeres "switch"
            cents > 0 -> "bekommt"
            cents < 0 -> "schuldet"
            else -> "ist ausgeglichen"
        }
        println("${name(id)} $status ${MoneyFormatter.format(kotlin.math.abs(cents))}")
    }

    println("\n=== Vorschlag zum Ausgleich ===")
    for (t in SettlementSuggester.suggest(balances)) {
        println("${name(t.fromTravelerId)} zahlt ${name(t.toTravelerId)} ${MoneyFormatter.format(t.amountCents)}")
    }
}
