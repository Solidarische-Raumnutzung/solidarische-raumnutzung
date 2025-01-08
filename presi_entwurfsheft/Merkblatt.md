# Architektur

- Architektur ist in *Frontend* und *Backend* unterteilt
- Folgt der *MVC-Struktur* zur klaren Trennung der Verantwortlichkeitsbereiche
- *Frontend* nutzt:
    - *HTML*
    - *DaisyUI* für das Design
    - *FullCalendar* in *JavaScript* für die Kalenderfunktionalität
- Kommunikation mit dem *Backend* erfolgt über *HTTPS*
- *Caddy*-Server läuft in einem eigenen *Container* und dient als *HTTPS-Reverse-Proxy*
- *Backend* basiert auf *Spring Boot* (Java 21) und läuft in einem weiteren *Container*
    - *Authentication Filter* prüft die Anfragen
    - Anfragen werden von einem *Controller* verarbeitet
    - Dynamische HTML-Antworten werden mithilfe von *JTE*-Templates generiert
- *Businesslogik* liegt in den *Services*, die Anfragen koordinieren und weiterleiten
- *Repositories* kapseln den SQL-Zugriff und ermöglichen eine saubere, abstrahierte Kommunikation mit der *PostgreSQL*-Datenbank in einem separaten *Container*
- Einsatz von *Docker* sorgt für eine einfache Bereitstellung und Wartbarkeit der Anwendung

