```plantuml
[*] --> Overview
Overview : Kalender mit Buchungen
Overview --> Erstellen : Klick auf Kalender
Erstellen: HTML-Form zum Erstellen einer Buchung
Erstellen --> Details : Speichern
Details: Anzeige der Buchungsdetails, Löschung
Overview --> Details : Klick auf Buchung
```