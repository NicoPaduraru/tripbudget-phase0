package de.tripbudget.domain.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/*
 * Formatiert Cent-Beträge für die Anzeige.
 * Deutsch:  1234 → "12,34 €"     Englisch: 1234 → "€12.34"
 * Wir nutzen dafür die Java-Klasse NumberFormat. Kotlin kann jede
 * Java-Bibliothek direkt verwenden.
 */
object MoneyFormatter {
    fun format(cents: Long, currencyCode: String = "EUR", locale: Locale = Locale.GERMANY): String {
        val currency = Currency.getInstance(currencyCode)
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.currency = currency // Kotlin-Property statt setCurrency(...)
        // Achtung: Nicht jede Währung hat 2 Nachkommastellen (z. B. JPY hat 0).
        val factor = Math.pow(10.0, currency.defaultFractionDigits.toDouble())
        return formatter.format(cents / factor) // Double nur für die Anzeige, nie zum Rechnen
    }
}
