# Testowanie Uruchomienia Aplikacji przy Różnych Ustawieniach JVM

## 📌 Opis
Ten dokument opisuje, jakie polecenia JVM zostały przetestowane dla aplikacji **MeasurementViewer** oraz przedstawia wnioski dotyczące wpływu poszczególnych opcji na zachowanie **garbage collection (GC)**.

---

## Użyte Parametry JVM
Testy przeprowadzono przy użyciu ograniczenia pamięci:
- `-Xms512m` – minimalny rozmiar sterty: **512 MB**
- `-Xmx1024m` – maksymalny rozmiar sterty: **1024 MB**

Logi garbage collection zbierano za pomocą **Unified Logging**:
```sh
-Xlog:gc:file=[nazwa_pliku].log:tags,time,level*
```

---

## ⚙️ Przetestowane Konfiguracje GC

### G1GC z etapowym kurczeniem sterty
```sh
java -Xms512m -Xmx1024m -XX:+UseG1GC -XX:+ShrinkHeapInSteps -Xlog:gc*:file=gc_g1.log:tags,time,level -cp out/production/MeasurementViewer pl.app.MeasurementViewer
```

### G1GC bez etapowego kurczenia sterty
```sh
java -Xms512m -Xmx1024m -XX:+UseG1GC -XX:-ShrinkHeapInSteps -Xlog:gc*:file=gc_g1_noshrink.log:tags,time,level -cp out/production/MeasurementViewer pl.app.MeasurementViewer
```

### SerialGC z wyłączonym etapowym kurczeniem sterty
```sh
java -Xms512m -Xmx1024m -XX:+UseSerialGC -XX:-ShrinkHeapInSteps -Xlog:gc*:file=gc_serial.log:tags,time,level -cp out/production/MeasurementViewer pl.app.MeasurementViewer
```

### ParallelGC z etapowym kurczeniem sterty
```sh
java -Xms512m -Xmx1024m -XX:+UseParallelGC -XX:+ShrinkHeapInSteps -Xlog:gc*:file=gc_parallel.log:tags,time,level -cp out/production/MeasurementViewer pl.app.MeasurementViewer
```

### Domyślne ustawienia GC
```sh
java -Xms512m -Xmx1024m -Xlog:gc*:file=gc_default.log:tags,time,level -cp out/production/MeasurementViewer pl.app.MeasurementViewer
```

---

## 📊 Analiza Logów GC
Logi zostały przeanalizowane przy użyciu **GCViewer** oraz monitoringu za pomocą **VisualVM**. Oto najważniejsze obserwacje:

### G1GC z etapowym kurczeniem sterty
- **Logi:** `gc_g1.log`
- **Obserwacje:**
  - Krótkie i przewidywalne pauzy (`G1 Evacuation Pause`).
  - Etapowe kurczenie sterty pozwala na dynamiczne odzyskiwanie nieużywanej pamięci.
- **Wnioski:**
  - Optymalne dla środowisk z ograniczonymi zasobami (512m - 1024m).
  - Szczególnie korzystne na maszynach wielordzeniowych.

### G1GC bez etapowego kurczenia sterty
- **Logi:** `gc_g1_noshrink.log`
- **Obserwacje:**
  - Sterta nie jest kurczona etapowo, co może prowadzić do utrzymania większej ilości pamięci, ale potencjalnie mniej dynamicznego odzyskiwania.
- **Wnioski:**
  - Może być mniej efektywne w środowiskach o ograniczonej pamięci.

### SerialGC
- **Logi:** `gc_serial.log`
- **Obserwacje:**
  - GC działa w pojedynczym wątku, co skutkuje dłuższymi pauzami, szczególnie w aplikacjach wielowątkowych.
- **Wnioski:**
  - Może być stosowany w prostych scenariuszach, ale nie jest zalecany dla interaktywnych aplikacji na maszynach wielordzeniowych.

### ParallelGC
- **Logi:** `gc_parallel.log`
- **Obserwacje:**
  - Wykorzystanie wielu wątków przez ParallelGC przekłada się na lepszą przepustowość i krótsze pauzy niż w przypadku SerialGC.
- **Wnioski:**
  - Dobrze sprawdza się na maszynach wielordzeniowych, choć nie optymalizuje pauz tak dobrze, jak G1GC.

### Domyślne ustawienia
- **Logi:** `gc_default.log`
- **Obserwacje:**
  - Domyślnie wykorzystywany jest **G1GC**, co potwierdzają logi.
- **Wnioski:**
  - Domyślne ustawienia są dobrym punktem wyjścia, jednak dodatkowe opcje, takie jak etapowe kurczenie sterty, mogą przynieść dodatkowe korzyści.

---

## ✅ Podsumowanie
Testy uruchomienia aplikacji **MeasurementViewer** przy różnych ustawieniach JVM wykazały, że:
**G1GC + ShrinkHeapInSteps** → najlepszy kompromis między krótkimi pauzami a efektywnym odzyskiwaniem pamięci.
**SerialGC** → generuje dłuższe pauzy, co może negatywnie wpływać na interaktywność.
**ParallelGC** → działa dobrze na maszynach wielordzeniowych, ale G1GC oferuje bardziej przewidywalne czasy pauz.
**Domyślne ustawienia JVM** (JDK 21, G1GC) → solidna baza, lecz dodatkowa konfiguracja (ShrinkHeapInSteps) może przynieść istotne korzyści przy ograniczonych zasobach pamięci.

