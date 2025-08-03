package pl.e_science.git.ivahan0788.GUI;

import pl.e_science.git.ivahan0788.AutomatonEngine;
import pl.e_science.git.ivahan0788.AutomatonLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VisualizationPanel extends JPanel {
    private final Timer timer;
    private final AutomatonEngine engine;
    private AutomatonLoader loader;
    private boolean isRunning = true;
    private final JComboBox<String> scriptSelector;
    private final JButton startPauseButton;

    private final Map<String, Path> scriptMap = new HashMap<>();

    public VisualizationPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        engine = new AutomatonEngine(80, 60);

        // Mapy nazw do ścieżek
        scriptMap.put("Game of Life", Paths.get("src/main/resources/scripts/game_of_life.js"));
        scriptMap.put("Wireworld", Paths.get("src/main/resources/scripts/wireworld.js"));

        // UI top panel
        JPanel topPanel = new JPanel();
        scriptSelector = new JComboBox<>(scriptMap.keySet().toArray(new String[0]));
        scriptSelector.addActionListener(e -> reloadScript());
        startPauseButton = new JButton("Pause");
        startPauseButton.addActionListener(e -> toggleTimer());
        topPanel.add(new JLabel("Automaton:"));
        topPanel.add(scriptSelector);
        topPanel.add(startPauseButton);

        add(topPanel, BorderLayout.NORTH);

        String defaultAutomaton = (String) scriptSelector.getSelectedItem();
        loadScript(scriptMap.get(defaultAutomaton));
        int stateCount = defaultAutomaton.equals("Wireworld") ? 4 : 2;
        engine.randomize(stateCount);

        // Timer do aktualizacji
        timer = new Timer(200, this::updateState);
        timer.start();

        // Obsługa kliknięcia myszą do edycji
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isRunning) {
                    int[][] grid = engine.getGrid();
                    int cellWidth = getWidth() / grid[0].length;
                    int cellHeight = (getHeight() - 40) / grid.length;
                    int x = e.getX() / cellWidth;
                    int y = (e.getY() - 40) / cellHeight;
                    if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                        grid[y][x] = grid[y][x] == 0 ? 1 : 0;
                        repaint();
                    }
                }
            }
        });
    }

    private void toggleTimer() {
        if (isRunning) {
            timer.stop();
            startPauseButton.setText("Start");
        } else {
            timer.start();
            startPauseButton.setText("Pause");
        }
        isRunning = !isRunning;
    }

    private void reloadScript() {
        timer.stop();
        String selected = (String) scriptSelector.getSelectedItem();
        if (selected != null) {
            Path path = scriptMap.get(selected);
            try {
                loader = new AutomatonLoader(path);
                int stateCount = selected.equals("Wireworld") ? 4 : 2;
                engine.randomize(stateCount);
                repaint();
                if (isRunning) timer.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd ładowania skryptu: " + ex.getMessage());
            }
        }
    }

    private void loadScript(Path scriptPath) {
        try {
            loader = new AutomatonLoader(scriptPath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd ładowania skryptu: " + e.getMessage());
            loader = null;
        }
    }

    private void updateState(ActionEvent e) {
        if (loader != null) {
            try {
                int[][] newGrid = loader.nextGeneration(engine.getGrid());
                engine.setGrid(newGrid);
                repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] grid = engine.getGrid();
        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = (getHeight() - 40) / grid.length;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * cellWidth, y * cellHeight + 40, cellWidth, cellHeight);
                } else if (grid[y][x] == 2) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(x * cellWidth, y * cellHeight + 40, cellWidth, cellHeight);
                } else if (grid[y][x] == 3) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * cellWidth, y * cellHeight + 40, cellWidth, cellHeight);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x * cellWidth, y * cellHeight + 40, cellWidth, cellHeight);
                }
            }
        }
    }
}