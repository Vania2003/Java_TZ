package com.example.untitled.tvapp.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class ConsoleUtils {
    private static final Scanner IN = new Scanner(System.in);

    private ConsoleUtils() {}

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + min + "–" + max + "]: ");
            String line = IN.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("  >> Proszę podać liczbę całkowitą z zakresu " + min + "–" + max);
            }
        }
    }

    public static long readLong(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = IN.nextLine().trim();
            try {
                long v = Long.parseLong(line);
                if (v < 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("  >> Proszę podać nieujemną liczbę całkowitą");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = IN.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException ex) {
                System.out.println("  >> Proszę podać poprawną liczbę");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt + ": ");
        return IN.nextLine().trim();
    }

    public static <E extends Enum<E>> E readEnum(Class<E> type, String prompt) {
        List<String> options = List.of(type.getEnumConstants())
                .stream().map(Enum::name).collect(Collectors.toList());
        String opts = String.join(", ", options);
        while (true) {
            System.out.print(prompt + " (" + opts + "): ");
            String line = IN.nextLine().trim().toUpperCase();
            try {
                return Enum.valueOf(type, line);
            } catch (IllegalArgumentException ex) {
                System.out.println("  >> Nieprawidłowa opcja, dozwolone: " + opts);
            }
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String line = IN.nextLine().trim();
            try {
                return LocalDate.parse(line);
            } catch (Exception ex) {
                System.out.println("  >> Nieprawidłowy format daty");
            }
        }
    }

    public static void printTable(String[] header, List<String[]> rows) {
        int cols = header.length;
        int[] widths = new int[cols];
        for (int i = 0; i < cols; i++) widths[i] = header[i].length();
        for (String[] row : rows) {
            for (int i = 0; i < cols; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        String sep = "+" + java.util.Arrays.stream(widths)
                .mapToObj(w -> "-".repeat(w + 2) + "+")
                .collect(Collectors.joining());
        System.out.println(sep);
        System.out.print("|");
        for (int i = 0; i < cols; i++) System.out.printf(" %-" + widths[i] + "s |", header[i]);
        System.out.println();
        System.out.println(sep);
        for (String[] row : rows) {
            System.out.print("|");
            for (int i = 0; i < cols; i++) System.out.printf(" %-" + widths[i] + "s |", row[i]);
            System.out.println();
        }
        System.out.println(sep);
    }
}