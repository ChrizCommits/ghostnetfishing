# 🌊 ghostnetfishing

Webprojekt im Modul Programmierung von industriellen Informationssystemen mit Java EE (IPWA02-01).
Im Rahmen des ersten Sprints wurde eine Plattform entwickelt, über die Geisternetze gemeldet und Bergungen koordiniert werden können. 
Die Netze werden nach Status gruppiert dargestellt und noch zu bergende Netze auf einer Weltkarte visualisiert.

## ⚙️ Installation & Setup

### 📋 Voraussetzungen
- **[JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** oder höher
- **Maven** – in Eclipse bereits integriert, in VS Code z. B. über das Plugin *Maven for Java*  
- **MySQL** – z. B. über [XAMPP](https://www.apachefriends.org/de/index.html) oder lokal installiert  
- **IDE** – Eclipse, VS Code, IntelliJ IDEA …

---

### 🧩 Einrichtungsschritte

#### 1️⃣ Repository in IDE klonen
```git clone https://github.com/chriz/ghostnetfishing.git```

#### 2️⃣ XAMPP starten
Im XAMPP-Control-Panel **Apache** und **MySQL** starten.

#### 3️⃣ Datenbank erstellen und Dump importieren
1. Im Browser http://localhost/phpmyadmin/ öffnen:
2. Neue Datenbank erstellen
  SQL Befehle:
  ```
  CREATE DATABASE ghostnetfishing;
  USE ghostnetfishing;
  ```
3. Anschließend die Datei `db/ghostnetfishing.sql` aus dem Repository über phpMyAdmin importieren.

#### 4️⃣ Anwendung in IDE starten
Eclipse: Run As → Spring Boot App
VS Code: `GhostNetFishingApplication.java` ausführen

#### 5️⃣ Anwendung im Browser öffnen
http://localhost:8090/

---

## 💡 Hinweise

Falls Port 8090 oder 3306 belegt ist, kann in `resources/application.properties` ein anderer Port gewählt werden:
```
pring.datasource.url=jdbc:mariadb://localhost:3306/ghostnetfishing
server.port=8090
```

Die Ports sollten mit der `my.ini` (MySQL-Konfiguration) übereinstimmen:
```
[client]
port=3306

[mysqld]
port=3306 
```

Falls eine andere Version als Jdk 17 gewählt wurde, bitte in `pom.xml` anpassen:
```
<java.version>*VERSIONSNUMMER*</java.version>
```



