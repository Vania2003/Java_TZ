package processing;

public interface Processor {

    /**
     * Metoda do zlecania zadania do przetworzenia.
     *
     * @param task Zadanie, które ma zostać wykonane.
     * @param sl   Słuchacz statusu, który będzie informowany o postępie zadania.
     * @return Zwraca true, jeśli zadanie zostało poprawnie przyjęte do przetworzenia.
     */
    boolean submitTask(String task, StatusListener sl);

    /**
     * Metoda zwraca opis zadania.
     *
     * @return Opis zadania.
     */
    String getInfo();

    /**
     * Metoda zwraca wynik przetworzenia zadania.
     *
     * @return Wynik zadania, jeśli zostało zakończone, lub null, jeśli jeszcze w trakcie.
     */
    String getResult();
}
