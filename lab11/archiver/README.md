# ArchiverApp – Multi-Release Modular JavaFX App

## 📦 Opis projektu

Aplikacja **ArchiverApp** to narzędzie GUI stworzone w technologii JavaFX, umożliwiające:

- 📁 archiwizowanie folderów do formatu ZIP,
- 🔐 generowanie i zapisywanie hashy MD5,
- ✅ weryfikację integralności archiwum ZIP na podstawie pliku `.md5`.

Aplikacja wykorzystuje **Java Modules (JPMS)** oraz **multi-release JAR (MR-JAR)**, aby wspierać różne wersje Javy (Java 11 i Java 17).

---

## 🧱 Struktura modułów

Projekt składa się z dwóch modułów:

- `archiver.library` – biblioteka z funkcjami `zipDirectory`, `generateMD5`, `verifyMD5`, itp.
- `archiver.app` – moduł aplikacji GUI, oparty na JavaFX.

---

## 🧰 Proces kompilacji

### 1. Kompilacja modułu `archiver.library`

```bash
javac -d out/11/archiver.library src/archiver.library/module-info.java src/archiver.library/com/archiver/library/Archiver.java

jar --create --file dist/archiver.library.jar --module-version 1.0 -C out/11/archiver.library .
```

### 2. Kompilacja modułu `archiver.app` dla Java 11

```bash
javac --module-path "dist;javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -d out/11/archiver.app src/archiver.app/module-info.java src/archiver.app/com/archiver/app/ArchiverApp.java
```

### 3. Kompilacja wersji Java 17 (multi-release)

```bash
javac --release 17 --module-path "dist;javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -d out/17/archiver.app src/archiver/app/java17/com/archiver/app/ArchiverApp.java
```

### 4. Tworzenie multi-release JAR-a

```bash
jar --create --file dist/archiver.app.jar --manifest manifest.txt -C out/11/archiver.app . --release 17 -C out/17/archiver.app .
```

---

## ⚙️ Tworzenie obrazu wykonawczego (runtime) z `jlink`

```bash
jlink ^
  --module-path "dist;javafx-sdk-21.0.7/lib" ^
  --add-modules archiver.app,archiver.library,javafx.controls,javafx.fxml,javafx.graphics ^
  --output archiver-runtime ^
  --strip-debug ^
  --no-header-files ^
  --no-man-pages ^
  --compress=2
```

---

## 📦 Tworzenie instalatora `.msi` z `jpackage`

```bash
jpackage ^
  --type msi ^
  --name Archiver ^
  --app-version 1.0 ^
  --input dist ^
  --main-jar archiver.app.jar ^
  --main-class com.archiver.app.ArchiverApp ^
  --runtime-image archiver-runtime ^
  --vendor "Ivan Hancharyk"
```

Instalator został utworzony poprawnie, **ale po instalacji aplikacja nie uruchamia się**, wyświetlając błąd:

```
Failed to launch JVM
```

---

## 🧪 Próby naprawy błędu

1. ✅ Zweryfikowano poprawność `module-info.class` w obu JAR-ach.
2. ✅ Sprawdzono obecność klas i folderów `META-INF/versions/17/` w archiwum.
3. ✅ Upewniono się, że `main-class` dziedziczy po `javafx.application.Application`.
4. ✅ Przetestowano różne ścieżki do `runtime-image`, także z użyciem `%JAVA_HOME%`.
5. ✅ Sprawdzono poprawność ścieżek do JavaFX w `--module-path`.
6. ✅ Spróbowano użycia instalatora bez `jlink`, z domyślnym JDK.

---

## ❌ Wnioski

Mimo poprawności kompilacji i konfiguracji, problem `Failed to launch JVM` występuje w fazie uruchamiania instalatora MSI.

**Możliwe przyczyny**:
- Niekompatybilność JavaFX + MR-JAR z `jpackage`
- Problemy z lokalizacją `main-class` w kontekście `module-info`
