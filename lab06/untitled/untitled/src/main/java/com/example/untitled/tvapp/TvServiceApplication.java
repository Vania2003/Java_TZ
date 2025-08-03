package com.example.untitled.tvapp;

import com.example.untitled.tvapp.model.ServiceType;
import com.example.untitled.tvapp.model.Subscription;
import com.example.untitled.tvapp.model.PricingEntry;
import com.example.untitled.tvapp.model.Due;
import com.example.untitled.tvapp.model.Payment;
import com.example.untitled.tvapp.service.*;
import com.example.untitled.tvapp.util.ConsoleUtils;
import com.example.untitled.tvapp.util.TimeSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class TvServiceApplication implements CommandLineRunner {
    @Autowired private ClientService clientService;
    @Autowired private SubscriptionService subscriptionService;
    @Autowired private PricingService pricingService;
    @Autowired private AccountingService accountingService;
    @Autowired private TimeSimulator timeSimulator;

    public static void main(String[] args) {
        System.out.println("..:: TV Service Application ::..");
        System.out.println(">> SPRING DATASOURCE URL = " +
                System.getProperty("spring.datasource.url",
                        System.getenv("SPRING_DATASOURCE_URL") + " [env]"));
        SpringApplication.run(TvServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        while (true) {
            printMainMenu();
            int choice = ConsoleUtils.readInt("Wybierz opcję", 0, 5);
            switch (choice) {
                case 1 -> manageClients();
                case 2 -> manageSubscriptions();
                case 3 -> managePricing();
                case 4 -> manageAccounting();
                case 5 -> manageTime();
                case 0 -> { System.out.println("Koniec działania aplikacji."); return; }
                default -> System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=== Główne Menu ===");
        System.out.println("1) Zarządzanie klientami");
        System.out.println("2) Zarządzanie abonamentami");
        System.out.println("3) Cennik");
        System.out.println("4) Rozliczenia");
        System.out.println("5) Symulacja czasu");
        System.out.println("0) Wyjście");
    }

    private void manageClients() {
        System.out.println();
        System.out.println("--- Zarządzanie Klientami ---");
        System.out.println("1) Dodaj klienta");
        System.out.println("2) Pokaż listę klientów");
        System.out.println("0) Powrót");
        int c = ConsoleUtils.readInt("Opcja", 0, 2);
        switch (c) {
            case 1 -> {
                var fn = ConsoleUtils.readString("Imię");
                var ln = ConsoleUtils.readString("Nazwisko");
                var client = clientService.createClient(fn, ln);
                System.out.println("Dodano klienta: " + client);
            }
            case 2 -> {
                var list = clientService.listAll();
                String[] hdr = {"ID","Imię","Nazwisko"};
                var rows = list.stream()
                        .map(cli -> new String[]{
                                String.valueOf(cli.getId()),
                                cli.getFirstName(),
                                cli.getLastName()
                        }).toList();
                ConsoleUtils.printTable(hdr, rows);
            }
            case 0 -> {
                // powrót
            }
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }

    private void manageSubscriptions() {
        System.out.println();
        System.out.println("--- Zarządzanie Abonamentami ---");
        System.out.println("1) Dodaj abonament");
        System.out.println("2) Pokaż abonamenty klienta");
        System.out.println("3) Dodaj subkonto");
        System.out.println("0) Powrót");
        int c = ConsoleUtils.readInt("Opcja", 0, 3);
        switch (c) {
            case 1 -> {
                long cid = ConsoleUtils.readLong("ID klienta");
                var type = ConsoleUtils.readEnum(ServiceType.class, "Typ usługi");
                var sub = subscriptionService.createSubscription(cid, type);
                System.out.println("Dodano abonament: " + sub);
            }
            case 2 -> {
                long cid = ConsoleUtils.readLong("ID klienta");
                var subs = subscriptionService.listSubscriptions(cid);
                String[] hdr = {"ID","Typ","Klient"};
                var rows = subs.stream()
                        .map(s -> new String[]{
                                String.valueOf(s.getId()),
                                s.getType().name(),
                                String.valueOf(s.getClient().getId())
                        }).toList();
                ConsoleUtils.printTable(hdr, rows);
            }
            case 3 -> {
                long sid = ConsoleUtils.readLong("ID abonamentu");
                var login = ConsoleUtils.readString("Login subkonta");
                var pwd = ConsoleUtils.readString("Hasło subkonta");
                subscriptionService.addSubaccount(sid, login, pwd);
                System.out.println("Subkonto zostało dodane.");
            }
            case 0 -> {}
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }

    private void managePricing() {
        System.out.println();
        System.out.println("--- Zarządzanie Cennikiem ---");
        System.out.println("1) Dodaj wpis cenowy");
        System.out.println("2) Pokaż cennik");
        System.out.println("0) Powrót");
        int c = ConsoleUtils.readInt("Opcja", 0, 2);
        switch (c) {
            case 1 -> {
                var type = ConsoleUtils.readEnum(ServiceType.class, "Typ usługi");
                var price = ConsoleUtils.readDouble("Cena");
                var from = ConsoleUtils.readDate("Od (YYYY-MM-DD)");
                System.out.print("Do (YYYY-MM-DD lub puste): ");
                var toLine = new Scanner(System.in).nextLine().trim();
                LocalDate to = toLine.isBlank() ? null : LocalDate.parse(toLine);
                var entry = pricingService.addPricingEntry(type, price, from, to);
                System.out.println("Dodano wpis: " + entry);
            }
            case 2 -> {
                var list = pricingService.listPricingEntries();
                String[] hdr = {"ID","Typ","Cena","Od","Do"};
                var rows = list.stream()
                        .map(e -> new String[]{
                                String.valueOf(e.getId()),
                                e.getType().name(),
                                String.valueOf(e.getPrice()),
                                e.getEffectiveFrom().toString(),
                                e.getEffectiveTo()==null?"":e.getEffectiveTo().toString()
                        }).toList();
                ConsoleUtils.printTable(hdr, rows);
            }
            case 0 -> {}
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }

    private void manageAccounting() {
        System.out.println();
        System.out.println("--- Rozliczenia ---");
        System.out.println("1) Generuj miesięczne należności");
        System.out.println("2) Pokaż należności");
        System.out.println("3) Dodaj wpłatę");
        System.out.println("0) Powrót");
        int c = ConsoleUtils.readInt("Opcja", 0, 3);
        switch (c) {
            case 1 -> {
                accountingService.generateMonthlyDues();
                System.out.println("Należności wygenerowane.");
            }
            case 2 -> {
                var dues = accountingService.listDues();
                String[] hdr = {"ID","Data","Kwota","Zapłacone","Sub"};
                var rows = dues.stream()
                        .map(d -> new String[]{
                                String.valueOf(d.getId()),
                                d.getDueDate().toString(),
                                String.valueOf(d.getAmount()),
                                String.valueOf(d.isPaid()),
                                String.valueOf(d.getSubscription().getId())
                        }).toList();
                ConsoleUtils.printTable(hdr, rows);
            }
            case 3 -> {
                long did = ConsoleUtils.readLong("ID należności");
                var date = ConsoleUtils.readDate("Data płatności");
                var amt = ConsoleUtils.readDouble("Kwota");
                var pay = accountingService.recordPayment(did, date, amt);
                System.out.println("Zarejestrowano płatność: " + pay);
            }
            case 0 -> {}
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }

    private void manageTime() {
        System.out.println();
        System.out.println("--- Symulacja Czasu ---");
        System.out.println("1) Następny dzień");
        System.out.println("2) Następny miesiąc");
        System.out.println("0) Powrót");
        int c = ConsoleUtils.readInt("Opcja", 0, 2);
        switch (c) {
            case 1 -> {
                timeSimulator.advanceDay();
                System.out.println("Aktualna data: " + timeSimulator.today());
            }
            case 2 -> {
                timeSimulator.advanceMonth();
                System.out.println("Aktualna data: " + timeSimulator.today());
            }
            case 0 -> {}
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }
}