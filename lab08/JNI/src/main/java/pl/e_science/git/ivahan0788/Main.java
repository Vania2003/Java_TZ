package pl.e_science.git.ivahan0788;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 10;
        Double[] testData = new Double[size];
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            testData[i] = r.nextDouble() * 100;
        }

        NativeSorter sorter = new NativeSorter();
        sorter.a = testData;
        sorter.order = true;

        // --- sort04 (Java) ---
        sorter.sort04();
        System.out.println("sort04 (Java):");
        printArray(sorter.b);

        // --- sort01 (JNI z parametrami) ---
        Double[] result01 = sorter.sort01(testData, sorter.order);
        System.out.println("sort01 (JNI, parametry):");
        printArray(result01);

        // --- sort02 (JNI, pobiera order z obiektu) ---
        sorter.order = false;
        Double[] result02 = sorter.sort02(testData);
        System.out.println("sort02 (JNI, pobiera order z pola):");
        printArray(result02);

        // --- sort03 (terminalowe wejście) + sort04 (Java) ---
        System.out.println("\n*** sort03 (wczytaj dane z terminala) + sort04 (Java) ***");
        sorter.sort03();  // ustawia sorter.a i sorter.order
        sorter.sort04();  // sortuje i zapisuje do sorter.b
        System.out.println("sort04 wynik (dla danych z sort03):");
        printArray(sorter.b);
    }

    private static void printArray(Double[] array) {
        for (Double d : array) {
            System.out.printf("%.3f ", d);
        }
        System.out.println();
    }
}
