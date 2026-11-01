# TripBudget – Phase 0: Abrechnungslogik

Reines Kotlin-Projekt (ohne Android) mit der Kernlogik der TripBudget-App:

- **CostSplitter** – teilt einen Betrag centgenau auf mehrere Personen auf
- **BalanceCalculator** – berechnet den Saldo jeder Person (bekommt / schuldet)
- **SettlementSuggester** – schlägt vor, wer wem wie viel überweist

## Starten

```bash
./gradlew run     # Demo: Wochenende in Lissabon mit 3 Personen
./gradlew test    # 16 Unit-Tests
```

Unter Windows: `gradlew.bat run` bzw. `gradlew.bat test`.

## Struktur

```
src/main/kotlin/de/tripbudget/
├── Main.kt                      Demo-Programm
└── domain/
    ├── model/Models.kt          Traveler, Share, Expense, Transfer
    ├── logic/CostSplitter.kt
    ├── logic/BalanceCalculator.kt
    ├── logic/SettlementSuggester.kt
    └── util/MoneyFormatter.kt
src/test/kotlin/...              Unit-Tests (JUnit 5)
```

Der Ordner `domain/` wird in Phase 1 unverändert in die Android-App übernommen.
