# Views:
```plantuml
[*] --> Overview
Overview : Kalender mit Buchungen
Overview --> Erstellen : Klick auf Kalender
Erstellen: HTML-Form zum Erstellen einer Buchung
Erstellen --> Details : Speichern
Details: Anzeige der Buchungsdetails, Löschung
Overview --> Details : Klick auf Buchung
Overview --> Admin : Klick auf Admin (falls admin)
Admin: Anzeige der Buchungen, Löschung, Sicherheit
Overview --> Login : Klick auf Login
Login: Anzeige des Login-Formulars (KIT OIDC/...)
Login --> Overview : Login erfolgreich
```