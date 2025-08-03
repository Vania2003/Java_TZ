# Java Labs 

This repository contains my complete work for the Java programming labs at Wrocław University of Science and Technology.  
Each lab focused on a distinct set of skills, technologies, and Java concepts – spanning from modularization and cryptography to REST APIs, reflection, JNI, XML processing, and JVM tuning.

---

## 🔍 What I Learned

Over the course I gained deep and practical experience in the following areas:

- **Java Modularity (JPMS)** and creating multi-module applications with `jlink`
- **Data processing** using CSV, XML (JAXB, SAX, DOM), and JSON
- **GUI development** with JavaFX, FXML, and custom visualizations
- **REST API design** and development using **Spring Boot**
- **Service Provider Interface (SPI)** for plugin-like extensibility
- **JNI (Java Native Interface)** for performance-critical native code
- **Java Reflection API** and custom class loaders
- **Encryption with JCA/JCE**, keystores, and signed JARs
- **Garbage Collection tuning**, memory management, and WeakReference use
- **Internationalization (i18n)** and localization with resource bundles
- **Scripting integration** via JavaScript (Nashorn, GraalVM)

These labs gave me not just Java-specific expertise, but broader software engineering insights:
- Modular thinking and architecture design
- Interoperability with native code
- Designing extensible and testable systems
- Debugging memory and performance bottlenecks
- Secure development practices (cryptography, permissions)
- Working with public APIs and real-world data

---

## 📁 Lab Summaries

### Lab01: Archiving Tool with MD5 Verification
- Zip files/folders, generate & verify MD5 checksums.
- GUI & CLI versions.
- Uses Java Modules + `jlink` to build a self-contained runtime.

### Lab02: Measurement Data Viewer
- CSV file explorer with GUI (dual panel: navigation + preview).
- Uses `WeakReference` for in-memory caching to avoid redundant reads.
- JVM tuning and GC options tested (`-Xmx`, `-XX:+UseG1GC`, etc.).

### Lab03: Bibliophile Quiz with Public Book API
- Fetch data from Wolne Lektury API.
- Localized GUI (PL & EN) using `ResourceBundle` and `ChoiceFormat`.
- Template-based dynamic question/answer generation and validation.

### Lab04: Task Execution with Reflection and Custom Class Loaders
- Dynamically load/unload external JARs implementing `Processor`.
- Task routing via GUI, with `getInfo()` metadata and sandboxing.

### Lab05: Classification Metrics Viewer (SPI-based)
- Load evaluation metrics (Cohen’s Kappa, etc.) via SPI in external JARs.
- Editable confusion matrix GUI.
- Modular & non-modular versions.

### Lab06: IPTV Subscription Manager (Standalone)
- JavaFX + JPA (Hibernate) + H2 or SQLite.
- Monthly billing, payment logging, escalation logic.
- Clean service-repository architecture.

### Lab07: IPTV Management API (Spring Boot)
- Full REST API exposing the same logic from Lab06.
- Tested via Swagger UI and Postman.
- Follows REST best practices and supports JSON I/O.

### Lab08: JNI Sorting and Performance Tests
- Implements 3 native sorting methods using JNI and C.
- Java-native sorting for comparison.
- Includes performance benchmarks and JIT behavior analysis.

### Lab09: XML Processing Engine
- Loads and renders XML via JAXB, SAX, and DOM.
- Transforms XML via selectable XSLT files.
- Source data from public open data portals.

### Lab10: File Encryption Tool
- Encrypt/decrypt files using JCA/JCE.
- Keys stored in keystores, signed external JAR library.
- JavaFX GUI with selectable algorithms and key files.

### Lab11: Multi-Release JAR + Installer
- Builds a multi-release JAR (MRJAR) using modular Java (JPMS).
- Supports packaging with `jpackage` to create an installer.
- Demonstrates version-specific class loading and runtime creation.

### Lab12: Scriptable Cellular Automata (Graal.js/Nashorn)
- GUI for simulating various cellular automata.
- Core logic dynamically loaded from external `.js` scripts.
- Supports loading/unloading named scripts and visualizing output.

### Lab13: FXML-Only JavaFX App with JavaScript Logic
- Entire application logic written in FXML with embedded JavaScript (`fx:script`).
- Generates "inspirational quotes" from file-based templates.
- Uses only a minimal Java launcher; everything else is scripted inside the view.

### Lab14: Ticket Queue System with Agents and JMX
- Simulates real-world ticketing (e.g. at public service points).
- Includes ticket machines, service desks, and information panels.
- Dynamically reconfigurable via JMX agent (categories, priorities, assignments).
- Supports notification and state updates between components.

---

## 🧠 Skills Acquired

| Area                     | Technologies / Concepts                           |
|--------------------------|---------------------------------------------------|
| Java Core                | JPMS, SPI, Reflection, WeakReference              |
| GUI                      | JavaFX, FXML, CSS                                 |
| Data Formats             | CSV, XML (JAXB, SAX, DOM), JSON                   |
| Native Code              | JNI, C, JIT testing                               |
| Networking & APIs        | Spring Boot, REST, Swagger, Wolne Lektury API    |
| Cryptography             | JCA, JCE, keystores, signed JARs                 |
| Build & Deployment       | Maven, jlink, jpackage, modular JARs             |
| JVM Tuning               | -Xmx, -Xms, GC flags, GC behavior analysis       |
| Internationalization     | ResourceBundles, ChoiceFormat                    |
| Extensibility            | ServiceLoader (SPI), custom classloaders         |

---

## 📄 How to Run

Most labs are runnable via:
```bash
mvn clean install
java -p mods -m labXX.module/labXX.Main

Labs using Spring Boot:
mvn spring-boot:run

JNI (Lab08):
Requires building native C library (gcc or cl) and configuring java.library.path.
