package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String API_URL = "https://musicbrainz.org/ws/2/artist/?query=type:group&fmt=json&limit=10&offset=";
    private static final int NUM_ARTISTS = 10;

    private JFrame frame;
    private JComboBox<String> artistList;
    private JTextArea songList;
    private String currentSongTitle = "";
    private String currentArtistName;
    private String currentSongYear = "";
    private String currentAlbumName = "";
    private JButton languageButton;
    private Locale currentLocale;
    private ResourceBundle messages;
    private List<String> usedSongsForArtist = new ArrayList<>();
    private List<String> usedSongsForAlbum = new ArrayList<>();
    private List<String> usedSongsForYear = new ArrayList<>();

    private int currentIndex = 0;
    private List<String> artists = new ArrayList<>();  // List of artist names
    private List<String> artistIds = new ArrayList<>();  // Corresponding artist IDs

    private List<String> questions = Arrays.asList("question", "question2", "question3");  // W pierwszej wersji 'question2' to pytanie o album, a 'question3' o rok
    private String currentQuestion = questions.get(0);

    public Main() {
        currentLocale = Locale.ENGLISH;
        messages = ResourceBundle.getBundle("messages", currentLocale);

        frame = new JFrame(messages.getString(currentQuestion));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        artistList = new JComboBox<>();
        loadArtists();
        panel.add(artistList, BorderLayout.NORTH);

        songList = new JTextArea();
        songList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(songList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton checkButton = new JButton(messages.getString("checkButton"));
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        panel.add(checkButton, BorderLayout.SOUTH);

        languageButton = new JButton(messages.getString("switchLanguage"));
        languageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleLanguage();
            }
        });
        panel.add(languageButton, BorderLayout.WEST);

        frame.add(panel);
        frame.pack();
        updateSongList();
        frame.setSize(500, 200);
        frame.setVisible(true);
    }

    private void loadArtists() {
        for (int i = 0; i < NUM_ARTISTS; i++) {
            try {
                String apiUrl = API_URL + (i + 1);
                String response = getApiResponse(apiUrl);
                String artistName = parseArtistName(response);
                artists.add(artistName);

                String artistId = parseArtistId(response);
                artistIds.add(artistId);
            } catch (IOException e) {
                artists.add("Error: Could not get artist name");
                e.printStackTrace();
            }
        }

        List<String> shuffledArtists = new ArrayList<>(artists);
        Collections.shuffle(shuffledArtists);

        for (String artist : shuffledArtists) {
            artistList.addItem(artist);
        }
    }

    private void toggleLanguage() {
        if (currentLocale.equals(Locale.ENGLISH)) {
            currentLocale = new Locale("pl", "PL");
        } else {
            currentLocale = Locale.ENGLISH;
        }
        messages = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
    }

    private void updateUI() {
        frame.setTitle(messages.getString(currentQuestion));
        languageButton.setText(messages.getString("switchLanguage"));
        songList.setText(messages.getString(currentQuestion) + " '" + currentSongTitle + "' ?");
    }

    private void checkAnswer() {
        String correctAnswer = "";
        if ("question".equals(currentQuestion)) {
            correctAnswer = currentArtistName;
        } else if ("question2".equals(currentQuestion)) {
            correctAnswer = currentAlbumName;
        } else if ("question3".equals(currentQuestion)) {
            correctAnswer = currentSongYear;
        }

        String selectedAnswer = (String) artistList.getSelectedItem();

        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            JOptionPane.showMessageDialog(null, messages.getString("goodAnswer"));
        } else {
            JOptionPane.showMessageDialog(null, messages.getString("wrongAnswer"));
        }

        artistList.setSelectedIndex(-1);

        loadNextQuestion();
    }

    private String getApiResponse(String apiUrl) throws IOException {
        int retryCount = 5; // Liczba prób ponowienia
        int delayBetweenRetries = 2000; // Opóźnienie w ms (2 sekundy)

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                return response; // Jeśli się uda, zwróć odpowiedź
            } catch (IOException e) {
                if (attempt == retryCount) {
                    throw new IOException("Server is unavailable after " + retryCount + " attempts", e);
                }
                try {
                    Thread.sleep(delayBetweenRetries); // Czekaj przez określony czas przed ponowną próbą
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        return null; // W przypadku niepowodzenia po kilku próbach, zwróć null
    }

    private String parseArtistName(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray artistsArray = jsonObject.getAsJsonArray("artists");

        if (artistsArray != null && artistsArray.size() > 0) {
            return artistsArray.get(0).getAsJsonObject().get("name").getAsString();
        } else {
            return "Unknown Artist";
        }
    }

    private String parseArtistId(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray artistsArray = jsonObject.getAsJsonArray("artists");

        if (artistsArray != null && artistsArray.size() > 0) {
            return artistsArray.get(0).getAsJsonObject().get("id").getAsString();
        } else {
            return "";
        }
    }

    private void updateArtistList() {
        artistList.removeAllItems();  // Clear previous items in the ComboBox

        if ("question".equals(currentQuestion)) {
            // Populate with artist names
            for (String artist : artists) {
                artistList.addItem(artist);
            }
        } else if ("question2".equals(currentQuestion)) {
            // Populate with possible album names
            Set<String> albums = new HashSet<>();
            // Add the correct album first
            if (!currentAlbumName.equals("No album available")) {
                albums.add(currentAlbumName);  // Add the correct album
            }

            // Add random albums from other songs (but not the current one)
            addRandomAlbums(albums);

            for (String album : albums) {
                artistList.addItem(album);
            }
        } else if ("question3".equals(currentQuestion)) {
            // Populate with possible years
            Set<String> years = new HashSet<>();
            // Add the correct year first
            if (!currentSongYear.equals("No year available")) {
                years.add(currentSongYear);  // Add the correct year
            }

            // Add random years from other songs (but not the current one)
            addRandomYears(years);

            for (String year : years) {
                artistList.addItem(year);
            }
        }
    }

    private void addRandomAlbums(Set<String> albums) {
        Random rand = new Random();
        int maxRandomAlbums = 10;  // Limit number of random albums to add

        for (int i = 0; i < artists.size() && albums.size() < maxRandomAlbums; i++) {
            String artistId = artistIds.get(i);
            try {
                String songUrl = "https://musicbrainz.org/ws/2/artist/" + artistId + "?inc=recordings+releases&fmt=json";
                String songResponse = getApiResponse(songUrl);

                JsonObject jsonObject = JsonParser.parseString(songResponse).getAsJsonObject();
                JsonArray releases = jsonObject.getAsJsonArray("releases");

                if (releases != null && releases.size() > 0) {
                    JsonObject release = releases.get(new Random().nextInt(releases.size())).getAsJsonObject();
                    String albumName = release.has("title") ? release.get("title").getAsString() : "No album available";

                    // Add to the set of albums if it's not already there
                    albums.add(albumName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addRandomYears(Set<String> years) {
        Random rand = new Random();
        int maxRandomYears = 10;  // Limit number of random years to add

        for (int i = 0; i < artists.size() && years.size() < maxRandomYears; i++) {
            String artistId = artistIds.get(i);
            try {
                String songUrl = "https://musicbrainz.org/ws/2/artist/" + artistId + "?inc=recordings+releases&fmt=json";
                String songResponse = getApiResponse(songUrl);

                JsonObject jsonObject = JsonParser.parseString(songResponse).getAsJsonObject();
                JsonArray releases = jsonObject.getAsJsonArray("releases");

                if (releases != null && releases.size() > 0) {
                    JsonObject release = releases.get(new Random().nextInt(releases.size())).getAsJsonObject();
                    String releaseDate = release.has("date") && !release.get("date").isJsonNull() ? release.get("date").getAsString() : null;

                    if (releaseDate != null) {
                        String year = releaseDate.split("-")[0];  // Extract year
                        years.add(year);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSongList() {
        if (artists.isEmpty()) {
            songList.setText("No artists loaded");
            return;
        }

        String artistName = artists.get(currentIndex);
        try {
            String artistId = artistIds.get(currentIndex);
            String songUrl = "https://musicbrainz.org/ws/2/artist/" + artistId + "?inc=recordings+releases&fmt=json";
            String songResponse = getApiResponse(songUrl);

            JsonObject jsonObject = JsonParser.parseString(songResponse).getAsJsonObject();
            JsonArray recordings = jsonObject.getAsJsonArray("recordings");
            JsonArray releases = jsonObject.getAsJsonArray("releases");

            if (recordings != null && recordings.size() > 0) {
                // Losowanie piosenki
                JsonObject randomSong = recordings.get(new Random().nextInt(recordings.size())).getAsJsonObject();

                // Przetwarzanie tytułu piosenki
                currentSongTitle = randomSong.get("title").getAsString();
                currentArtistName = artistName;

                // Wyciąganie roku i albumu
                currentSongYear = getReleaseYear(releases);
                currentAlbumName = getAlbumName(releases);

                // Zaktualizowanie pytania
                String question = "";
                if ("question".equals(currentQuestion)) {
                    question = messages.getString("question") + " '" + currentSongTitle + "' ?";
                } else if ("question2".equals(currentQuestion)) {
                    question = messages.getString("question2") + " '" + currentSongTitle + "' ?";
                } else if ("question3".equals(currentQuestion)) {
                    question = messages.getString("question3") + " '" + currentSongTitle + "' ?";
                }
                songList.setText(question);
                updateArtistList();  // Zaktualizowanie ComboBoxa z odpowiedziami

            } else {
                songList.setText("No recordings found for " + artistName);
            }

        } catch (IOException e) {
            songList.setText("Error: Could not get song information for " + artistName);
            e.printStackTrace();
        }
    }

    private String getReleaseYear(JsonArray releases) {
        if (releases != null && releases.size() > 0) {
            int latestYear = Integer.MIN_VALUE;
            for (int i = 0; i < releases.size(); i++) {
                JsonObject release = releases.get(i).getAsJsonObject();
                if (release.has("date") && !release.get("date").isJsonNull()) {
                    String releaseDate = release.get("date").getAsString();
                    String year = releaseDate.split("-")[0];
                    try {
                        int releaseYear = Integer.parseInt(year);
                        if (releaseYear > latestYear) {
                            latestYear = releaseYear;
                        }
                    } catch (NumberFormatException e) {
                        // Jeśli rok jest niepoprawny, ignorujemy go
                    }
                }
            }
            if (latestYear != Integer.MIN_VALUE) {
                return String.valueOf(latestYear);
            } else {
                return "No year available";
            }
        }
        return "No year available";
    }

    private String getAlbumName(JsonArray releases) {
        if (releases != null && releases.size() > 0) {
            JsonObject release = releases.get(0).getAsJsonObject();
            if (release.has("title")) {
                return release.get("title").getAsString();
            }
        }
        return "No album available";
    }

    private void loadNextQuestion() {
        // Losujemy pytanie
        Random rand = new Random();
        currentQuestion = questions.get(rand.nextInt(questions.size()));

        // Losowanie różnych piosenek dla różnych pytań
        if ("question".equals(currentQuestion)) {
            currentIndex = getUniqueSongIndex(usedSongsForArtist);
            usedSongsForArtist.add(artists.get(currentIndex)); // Zapamiętujemy użyty utwór dla pytania o artystę
        } else if ("question2".equals(currentQuestion)) {
            currentIndex = getUniqueSongIndex(usedSongsForAlbum);
            usedSongsForAlbum.add(artists.get(currentIndex)); // Zapamiętujemy użyty utwór dla pytania o album
        } else if ("question3".equals(currentQuestion)) {
            currentIndex = getUniqueSongIndex(usedSongsForYear);
            usedSongsForYear.add(artists.get(currentIndex)); // Zapamiętujemy użyty utwór dla pytania o rok
        }

        updateSongList();  // Zaktualizuj UI po zmianie pytania
    }

    private int getUniqueSongIndex(List<String> usedSongs) {
        Random rand = new Random();
        int index;
        // Losujemy piosenkę, która jeszcze nie została użyta
        do {
            index = rand.nextInt(artists.size());
        } while (usedSongs.contains(artists.get(index)));  // Dopóki piosenka była już użyta, losujemy nową

        return index;
    }

    public static void main(String[] args) {
        new Main();
    }
}
