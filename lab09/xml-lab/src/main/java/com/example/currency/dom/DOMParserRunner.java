// File: src/main/java/com/example/currency/dom/DOMParserRunner.java
package com.example.currency.dom;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class DOMParserRunner {
    public static void run(String resourceName) {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(resourceName);
            if (stream == null) {
                throw new IllegalArgumentException("Nie znaleziono zasobu: " + resourceName);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);

            NodeList items = document.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String currency = getText(item, "targetCurrency");
                String name = getText(item, "targetName");
                String rate = getText(item, "exchangeRate");
                System.out.println(currency + " (" + name + "): " + rate);
            }
        } catch (Exception e) {
            System.err.println("Błąd DOM: " + e.getMessage());
        }
    }

    private static String getText(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0 && list.item(0).getFirstChild() != null)
            return list.item(0).getFirstChild().getNodeValue();
        return "";
    }
}
