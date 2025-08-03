// File: src/main/java/com/example/currency/App.java
package com.example.currency;

import com.example.currency.jaxb.JAXBLoader;
import com.example.currency.model.Channel;
import com.example.currency.model.Item;
import com.example.currency.sax.SAXParserRunner;
import com.example.currency.dom.DOMParserRunner;
import com.example.currency.xslt.XSLTProcessor;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Wybierz plik XML (np. BYN.xml, UAH.xml, PLN.xml):");
            String path = scanner.nextLine().trim();

            System.out.println("Wybierz metodę parsowania:");
            System.out.println("1 - JAXB");
            System.out.println("2 - SAX");
            System.out.println("3 - DOM");
            System.out.println("4 - XSLT");
            System.out.println("0 - Zakończ program");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Zamykanie programu.");
                break;
            }

            switch (choice) {
                case 1:
                    Channel channel = JAXBLoader.loadChannelFromFile(path);
                    if (channel == null) {
                        System.out.println("Nie udało się wczytać danych.");
                        break;
                    }
                    System.out.println("\n=== Kursy walut dla " + channel.getTitle() + " ===");
                    for (Item item : channel.getItem()) {
                        System.out.println(item);
                    }
                    break;

                case 2:
                    SAXParserRunner.run(path);
                    break;

                case 3:
                    DOMParserRunner.run(path);
                    break;

                case 4:
                    System.out.println("Podaj nazwę arkusza XSLT (np. compact.xslt):");
                    String xslt = scanner.nextLine().trim();
                    System.out.println("Podaj nazwę pliku wyjściowego (Enter = output.html):");
                    String out = scanner.nextLine().trim();
                    XSLTProcessor.transform(path, xslt, out);
                    break;

                default:
                    System.out.println("Nieprawidłowy wybór.");
            }

            System.out.println("\n--- Operacja zakończona. ---\n");
        }
    }
}
