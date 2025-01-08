# Kernsoftware
Die Kernsoftware wird mit Gradle gebaut.
Zum Starten des Programms kann der Befehl `./gradlew bootRun` verwendet werden.
Stelle vor dem Start sicher, dass folgende Umgebungsvariablen gesetzt sind:
- `KIT_MAIL_USERNAME`: Der Benutzername des E-Mail-Accounts, der zum Versenden von E-Mails verwendet wird.
- `KIT_MAIL_PASSWORD`: Das Passwort des E-Mail-Accounts, der zum Versenden von E-Mails verwendet wird.
- `KIT_CLIENT_ID`: Die Client-ID des OAuth2-Clients, der zum Authentifizieren von Benutzern verwendet wird.
- `KIT_CLIENT_SECRET`: Das Client-Geheimnis des OAuth2-Clients, der zum Authentifizieren von Benutzern verwendet wird.

Stelle außerdem sicher, dass Docker installiert ist und läuft, da die Datenbank in einem Docker-Container läuft.
Du musst sie nicht manuell starten, da Gradle dies für dich übernimmt.

Die Architektur der Kernsoftware ist in den im README auffindbaren Dokumenten ausführlich beschrieben.

# Dokumentation
Die Dokumentation wird mit LaTeX erstellt.
Da die Dokumentation eine Abgabe für das Modul "PSE" ist, sollte sie nach dem Termin der Abgabe nicht mehr verändert werden.
Verbesserungen abgesehen von notwendigen Korrekturen, die den Inhalt nicht verändern, sind also nicht erwünscht.

Da das Entwurfsheft JavaDoc-Kommentare aus dem Quellcode verwendet, müssen diese vor dem Bauen des Hefts mit DocTeX in LaTeX umgewandelt werden.
Führe dazu `./gradlew doctex` aus.
