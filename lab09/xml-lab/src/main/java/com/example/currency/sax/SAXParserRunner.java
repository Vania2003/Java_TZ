// File: src/main/java/com/example/currency/sax/SAXParserRunner.java
package com.example.currency.sax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

public class SAXParserRunner {
    public static void run(String resourceName) {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(resourceName);
            if (stream == null) {
                throw new IllegalArgumentException("Nie znaleziono zasobu: " + resourceName);
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ExchangeRateHandler handler = new ExchangeRateHandler();
            parser.parse(stream, handler);
        } catch (Exception e) {
            System.err.println("Błąd SAX: " + e.getMessage());
        }
    }
}