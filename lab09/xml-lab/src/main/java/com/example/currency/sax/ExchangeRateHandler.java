package com.example.currency.sax;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ExchangeRateHandler extends DefaultHandler {
    private boolean insideTargetCurrency = false;
    private boolean insideTargetName = false;
    private boolean insideExchangeRate = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "targetCurrency": insideTargetCurrency = true; break;
            case "targetName": insideTargetName = true; break;
            case "exchangeRate": insideExchangeRate = true; break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String content = new String(ch, start, length).trim();
        if (insideTargetCurrency) {
            System.out.print("Waluta: " + content);
        } else if (insideTargetName) {
            System.out.print(" (" + content + ")");
        } else if (insideExchangeRate) {
            System.out.println(" - Kurs: " + content);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "targetCurrency": insideTargetCurrency = false; break;
            case "targetName": insideTargetName = false; break;
            case "exchangeRate": insideExchangeRate = false; break;
        }
    }
}