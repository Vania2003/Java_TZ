-- Cennik
INSERT INTO pricing_entry(type, price, effective_from) VALUES
                                                           ('BASIC',   29.99, CURRENT_DATE - INTERVAL '3' MONTH),
                                                           ('PREMIUM', 49.99, CURRENT_DATE - INTERVAL '3' MONTH),
                                                           ('UHD',     59.99, CURRENT_DATE - INTERVAL '3' MONTH),
                                                           ('SPORTS',  39.99, CURRENT_DATE - INTERVAL '3' MONTH);

-- Klienci
INSERT INTO client(first_name, last_name) VALUES
                                              ('Jan','Kowalski'),
                                              ('Aga','Nowak');

-- Abonamenty (zakładając, że PK są 1 i 2)
INSERT INTO subscription(client_id, type) VALUES
                                              (1, 'BASIC'),
                                              (2, 'PREMIUM');

-- Subkonta
INSERT INTO subaccount(login, password, subscription_id) VALUES
                                                             ('jkowalski','password123',1),
                                                             ('anowak','qwerty',2);

-- Należności
INSERT INTO due(due_date, amount, paid, subscription_id) VALUES
                                                             (CURRENT_DATE + INTERVAL '7' DAY, 29.99, FALSE, 1),
                                                             (CURRENT_DATE + INTERVAL '10' DAY, 49.99, FALSE, 2);

-- Wpłaty
INSERT INTO payment(payment_date, amount, subscription_id) VALUES
    (CURRENT_DATE, 15.00, 1);
