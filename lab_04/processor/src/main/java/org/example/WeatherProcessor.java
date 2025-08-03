package org.example;

import processing.Processor;
import processing.StatusListener;
import processing.Status;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherProcessor implements Processor {

    private String result;

    private static final String API_KEY = "43a0c125e9050b51683fa6b9cce61ed1";  // Twój klucz API OpenWeatherMap

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        // Rozdzielamy dane wejściowe na miasto (brak daty)
        String city = task.trim();  // Tylko miasto

        if (city.isEmpty()) {
            result = "Proszę podać miasto.";
            sl.statusChanged(new Status(1, 100)); // Status zakończenia
            return false;
        }

        // Pobieranie danych z OpenWeatherMap (prognozy)
        String weatherData = getWeatherData(city);

        if (weatherData != null) {
            result = weatherData;
        } else {
            result = "Błąd podczas pobierania danych pogodowych.";
        }

        // Aktualizacja statusu na zakończenie
        sl.statusChanged(new Status(1, 100)); // Status zakończenia
        return true;
    }

    @Override
    public String getInfo() {
        return "pogoda: #1"; // Wskazówka dla użytkownika, że podaje miasto
    }

    @Override
    public String getResult() {
        return result;
    }

    // Metoda do pobierania danych prognozy z OpenWeatherMap API (prognoza na 5 dni)
    private String getWeatherData(String city) {
        try {
            // URL do zapytania z OpenWeatherMap API (prognozy na 5 dni)
            String urlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric&cnt=8";  // 8 to liczba punktów w prognozie (8 x 3 godziny)

            // Tworzymy obiekt URL
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Odczytujemy odpowiedź z serwera
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Przetwarzamy odpowiedź JSON
            JSONObject myResponse = new JSONObject(response.toString());
            JSONArray list = myResponse.getJSONArray("list");  // Lista prognoz dla różnych dni i godzin

            // Pobieramy dane z pierwszego punktu prognozy (w tym przypadku, prognoza na następne 3 godziny)
            JSONObject firstForecast = list.getJSONObject(0);
            double temperature = firstForecast.getJSONObject("main").getDouble("temp");
            int pressure = firstForecast.getJSONObject("main").getInt("pressure");

            // Tworzymy wynik
            return "Miasto: " + city + ", Temperatura: " + temperature + "°C, Ciśnienie: " + pressure + " hPa";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
