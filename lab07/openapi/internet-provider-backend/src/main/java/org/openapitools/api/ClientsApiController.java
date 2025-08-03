package org.openapitools.api;

import org.openapitools.model.Client;
import org.openapitools.model.Payment;
import org.openapitools.model.Subscription;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.PostConstruct;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.*;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-28T23:11:08.615839200+02:00[Europe/Warsaw]")
@Controller
@RequestMapping("${openapi.internetProvider.base-path:}")
public class ClientsApiController implements ClientsApi {

    private Map<Integer, Client> clients = new HashMap<>();
    private Map<Integer, List<Subscription>> subscriptions = new HashMap<>();
    private Map<Integer, List<Payment>> payments = new HashMap<>();
    private int clientIdCounter = 1;
    private int subscriptionIdCounter = 1;
    private int paymentIdCounter = 1;

    private final NativeWebRequest request;

    @Autowired
    public ClientsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> clientsPost(@Valid @RequestBody Client client) {
        client.setId(clientIdCounter++);
        clients.put(client.getId(), client);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Client>> clientsGet() {
        return ResponseEntity.ok(new ArrayList<>(clients.values()));
    }

    @Override
    public ResponseEntity<Client> clientsClientIdGet(@PathVariable Integer clientId) {
        Client client = clients.get(clientId);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(client);
    }

    @Override
    public ResponseEntity<Void> clientsClientIdPut(@PathVariable Integer clientId, @Valid @RequestBody Client updatedClient) {
        if (!clients.containsKey(clientId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        updatedClient.setId(clientId);
        clients.put(clientId, updatedClient);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> clientsClientIdDelete(@PathVariable Integer clientId) {
        clients.remove(clientId);
        subscriptions.remove(clientId);
        payments.remove(clientId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> clientsClientIdSubscriptionsPost(@PathVariable Integer clientId, @Valid @RequestBody Subscription subscription) {
        if (!clients.containsKey(clientId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        subscription.setId(subscriptionIdCounter++);
        subscriptions.computeIfAbsent(clientId, k -> new ArrayList<>()).add(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Subscription>> clientsClientIdSubscriptionsGet(@PathVariable Integer clientId) {
        List<Subscription> list = subscriptions.getOrDefault(clientId, Collections.emptyList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<Void> clientsClientIdPaymentsPost(@PathVariable Integer clientId, @Valid @RequestBody Payment payment) {
        if (!clients.containsKey(clientId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        payment.setId(paymentIdCounter++);
        payments.computeIfAbsent(clientId, k -> new ArrayList<>()).add(payment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Payment>> clientsClientIdPaymentsGet(@PathVariable Integer clientId) {
        List<Payment> list = payments.getOrDefault(clientId, Collections.emptyList());
        return ResponseEntity.ok(list);
    }

    @PostConstruct
    public void initData() {
        // Przykładowy klient
        Client client1 = new Client();
        client1.setId(clientIdCounter++);
        client1.setName("Jan Kowalski");
        client1.setEmail("jan.kowalski@example.com");
        client1.setAddress("ul. Przykładowa 1, Warszawa");
        clients.put(client1.getId(), client1);

        // Drugi klient
        Client client2 = new Client();
        client2.setId(clientIdCounter++);
        client2.setName("Anna Nowak");
        client2.setEmail("anna.nowak@example.com");
        client2.setAddress("ul. Testowa 5, Kraków");
        clients.put(client2.getId(), client2);

        // Subskrypcja dla klienta 1
        Subscription sub1 = new Subscription();
        sub1.setId(subscriptionIdCounter++);
        sub1.setServiceType("Internet 600Mb/s");
        sub1.setStartDate(LocalDate.of(2024, 1, 1));
        sub1.setEndDate(LocalDate.of(2025, 1, 1));
        subscriptions.computeIfAbsent(client1.getId(), k -> new ArrayList<>()).add(sub1);

        // Płatność dla klienta 1
        Payment payment1 = new Payment();
        payment1.setId(paymentIdCounter++);
        payment1.setAmount(99.99f);
        payment1.setPaymentDate(LocalDate.of(2024, 1, 5));
        payment1.setStatus(Payment.StatusEnum.PAID);
        payments.computeIfAbsent(client1.getId(), k -> new ArrayList<>()).add(payment1);
    }

}
