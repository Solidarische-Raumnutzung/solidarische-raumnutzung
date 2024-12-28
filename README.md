# solidarische raumnutzung
- [Pflichtenheft](https://solidarische-raumnutzung.github.io/solidarische-raumnutzung/pflichtenheft.pdf)
- [Entwurfsheft](https://solidarische-raumnutzung.github.io/solidarische-raumnutzung/entwurfsheft.pdf)
- [Beispielpräsentation](https://solidarische-raumnutzung.github.io/solidarische-raumnutzung/presentation.pdf)
- [Andere Artefakte](https://solidarische-raumnutzung.github.io/solidarische-raumnutzung/)
- [Deployment](http://193.196.39.22:8080)

# Installation
Zur Installation des Projekts wird Docker benötigt.
Wenn Docker installiert ist, kann die Konfiguration aus dem Verzeichnis [soli_deploy_config](./soli_deploy_config) verwendet werden.
Die Umgebungsvariablen in der Datei `docker-compose.yml` müssen angepasst werden, um die Anwendung zu konfigurieren.
Außerdem sollte die Datei `Caddyfile` angepasst werden, um die Domain zu konfigurieren.
Danach kann das Projekt mit `docker-compose up` gestartet werden.
(Dieser Befehlt muss in dem erwähnten Verzeichnis ausgeführt werden.)