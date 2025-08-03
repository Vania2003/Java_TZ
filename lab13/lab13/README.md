# Golden Thoughts Generator 📝✨

---

## 🚀 Co robi aplikacja?

1. **Losuje „złotą myśl”** z wybranego pliku tekstowego (*general / motivational / philosophical*).
2. Cytat pojawia się w **TextArea** – możesz go zaznaczyć i skopiować👍
3. Kategorie można zmieniać z `ChoiceBox`.

---

## 📂 Struktura

```
lab13/
   ├── pom.xml
   ├── src/
   └── main/
       ├── java/
       │   ├── module-info.java
       │   └── pl.e_science.git.ivahan0788/MainApp.java
       └── resources/
           ├── application.css
           ├── GoldenThoughts.fxml
           └── thoughts/
               ├── general.txt
               ├── motivational.txt
               └── philosophical.txt

```

`GoldenThoughts.fxml` zawiera dyrektywę
```xml
<?language javascript?>
```  
i **Nashorn‑style `<fx:script>`**; plik`.java` służy tylko do załadowania sceny.

---

## 🛠Wymagania

| Narzędzie     | Wersja |
|---------------|--------|
| **JDK**       | 11 (LTS) |
| **JavaFX SDK**| 17.0.15 |
| **Maven**     | 3.6+    |

---

## ▶️Uruchamianie

### 1. Maven(plugin javafx‑maven‑plugin)

```bash
mvn clean javafx:run
```

> Plugin pobiera JavaFX 11 artefakty z Maven Central i uruchamia klasę `MainApp`.

### 2. JAR + własne SDK (Windows)

```bash
mvn clean package

java ^
  --module-path "C:\javafx-sdk-17.0.15\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  -jar target\lab13-1.0-SNAPSHOT.jar
```

### 3. IntelliJ IDEA

1. **Project SDK** → JDK11
2. **Add VM options** w Run/Debug Configuration:

```
--module-path "C:\javafx-sdk-17.0.15\lib" --add-modules javafx.controls,javafx.fxml
```
