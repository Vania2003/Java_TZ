// File: src/main/java/com/example/currency/xslt/XSLTProcessor.java
package com.example.currency.xslt;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XSLTProcessor {
    public static void transform(String xmlResource, String xsltResourceName, String outputPath) {
        try {
            InputStream xmlStream = ClassLoader.getSystemResourceAsStream(xmlResource);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream("xslt/" + xsltResourceName);

            if (xmlStream == null || xsltStream == null) {
                throw new IllegalArgumentException("Nie znaleziono zasobów XML lub XSLT");
            }

            if (outputPath == null || outputPath.isBlank()) {
                outputPath = "output.html";
                System.out.println("Nie podano nazwy pliku wyjściowego – domyślnie: output.html");
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(xsltStream);
            Source xml = new StreamSource(xmlStream);
            Transformer transformer = factory.newTransformer(xslt);

            try (FileOutputStream outStream = new FileOutputStream(outputPath)) {
                transformer.transform(xml, new StreamResult(outStream));
            }

            System.out.println("Plik wynikowy zapisano jako: " + outputPath);
        } catch (Exception e) {
            System.err.println("Błąd XSLT: " + e.getMessage());
        }
    }
}
