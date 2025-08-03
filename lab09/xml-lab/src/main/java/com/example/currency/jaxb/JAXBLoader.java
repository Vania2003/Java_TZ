// File: src/main/java/com/example/currency/jaxb/JAXBLoader.java
package com.example.currency.jaxb;

import com.example.currency.model.Channel;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.InputStream;

public class JAXBLoader {
    public static Channel loadChannelFromFile(String resourceName) {
        try {
            JAXBContext context = JAXBContext.newInstance(Channel.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream stream = ClassLoader.getSystemResourceAsStream(resourceName);
            if (stream == null) {
                throw new IllegalArgumentException("Nie znaleziono zasobu: " + resourceName);
            }
            return (Channel) unmarshaller.unmarshal(stream);
        } catch (JAXBException e) {
            System.err.println("Błąd przy deserializacji pliku: " + e.getMessage());
            return null;
        }
    }
}
