package pl.e_science.git.ivahan0788;

import org.mockito.Mockito;

import java.util.Random;

import static org.mockito.Mockito.*;

public class PerformanceTestWithMockito {
    private static final int SIZE = 1_000_000;
    private static final int REPEATS = 10;

    public static void main(String[] args) {
        Double[] bigData = generateRandomArray(SIZE);
        NativeSorter sorter = Mockito.spy(new NativeSorter());
        sorter.order = true;

        System.out.println("=== ŚREDNIE CZASY (z " + REPEATS + " powtórzeń) ===");
        System.out.println("Tablica: " + SIZE + " elementów\n");

        // --- sort04 (Java) ---
        double avgJava = avgTimeMillis(() -> {
            sorter.a = bigData.clone();
            sorter.sort04();
        }, REPEATS);
        System.out.printf("sort04 (Java): %.2f ms%n", avgJava);
        verify(sorter, atLeastOnce()).sort04();

        // --- sort01 (JNI) ---
        double avgJNI01 = avgTimeMillis(() -> {
            try {
                sorter.sort01(bigData.clone(), sorter.order);
            } catch (Exception ignored) {}
        }, REPEATS);
        System.out.printf("sort01 (JNI): %.2f ms%n", avgJNI01);
        verify(sorter, atLeastOnce()).sort01(any(), any());

        // --- sort02 (JNI z pola) ---
        sorter.order = true;
        sorter.a = bigData.clone();
        double avgJNI02 = avgTimeMillis(() -> {
            try {
                sorter.sort02(sorter.a);
            } catch (Exception ignored) {}
        }, REPEATS);
        System.out.printf("sort02 (JNI): %.2f ms%n", avgJNI02);
        try {
            verify(sorter, atLeastOnce()).sort02(any());
        } catch (Exception e) {
            System.out.println("Mockito nie mógł zweryfikować sort02() – metoda natywna lub wyjątek JNI.");
        }
    }

    private static Double[] generateRandomArray(int size) {
        Random rand = new Random(123);
        Double[] array = new Double[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextDouble() * 1000;
        }
        return array;
    }

    private static double avgTimeMillis(Runnable action, int repeats) {
        long total = 0;
        for (int i = 0; i < repeats; i++) {
            long start = System.nanoTime();
            action.run();
            total += (System.nanoTime() - start);
        }
        return total / repeats / 1e6;
    }
}
