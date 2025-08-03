# 📊 Raport z projektu JNI: Sortowanie tablicy `Double[]`

## 🧾 Opis

Projekt demonstracyjny wykorzystujący **JNI (Java Native Interface)** do sortowania tablic typu `Double[]` przy użyciu kodu natywnego w C. Celem było porównanie wydajności sortowania w Javie z implementacjami natywnymi oraz analiza ewentualnego wpływu **JIT (Just-In-Time compilation)**.

---

## 🧱 Klasy i metody

### 🧩 `NativeSorter`

Klasa odpowiedzialna za logikę sortowania. Pola:

* `Double[] a` – tablica wejściowa,
* `Double[] b` – wynik sortowania,
* `Boolean order` – `true` oznacza sortowanie rosnące, `false` – malejące.

Metody:

| Metoda                      | Opis                                                                      |
| --------------------------- | ------------------------------------------------------------------------- |
| `sort01(Double[], Boolean)` | JNI: sortowanie z bezpośrednimi parametrami `a` i `order`                 |
| `sort02(Double[])`          | JNI: sortowanie, gdzie `order` pobierany jest z pola obiektu              |
| `sort03()`                  | JNI: terminalowe GUI (scanf), pozwala wpisać dane i ustawia `a` + `order` |
| `sort04()`                  | Implementacja w czystej Javie; wynik zapisywany do `b`                    |

### ▶️ `Main`

Klasa testowa z `main()`, umożliwia ręczne uruchomienie wszystkich metod oraz test interaktywny `sort03()`.

### 🧪 `PerformanceTestWithMockito`

Test porównujący wydajność metod. Wykorzystuje:

* `Mockito.spy()` do monitorowania wywołań,
* Pomiar czasu wykonania przy użyciu `System.nanoTime()` i uśrednianie,
* Powtórzenia (10x) każdej metody,
* Weryfikację wywołań (tam, gdzie możliwe).

---

## ⏱️ Wyniki testów wydajnościowych (średnia z 10 powtórzeń)

```
=== ŚREDNIE CZASY (z 10 powtórzeń) ===
Tablica: 1000000 elementów

sort04 (Java):  461,18 ms
sort01 (JNI): 1866,51 ms
sort02 (JNI): 2251,86 ms
```

---

## 📌 Wnioski

* 🔸 `sort04()` (czysta Java) była **najszybsza** – blisko 4x szybsza niż natywne JNI.
* 🔸 JNI (`sort01`, `sort02`) był znacząco wolniejszy, głównie z powodu:

  * konwersji `Double[]` do `double*` i odwrotnie,
  * alokacji/dealokacji pamięci w C,
  * braku optymalizacji przez JIT.
* 🔸 Nie zauważono wpływu **JIT** – czasy wykonania nie malały przy kolejnych wywołaniach.
* 🔸 `sort03()` nie był testowany w teście automatycznym ze względu na wymagane wejście z terminala.

---

## ⚠️ Uwagi

* 📌 Próba weryfikacji `sort02()` przez Mockito zakończyła się ostrzeżeniem: metoda natywna lub problem JNI uniemożliwił pełną inspekcję.
* 📌 `sort03()` można byłoby przetestować, przekierowując `System.in`, ale nie zostało to zrealizowane.

