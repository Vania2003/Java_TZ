package pl.e_science.git.ivahan0788;

import pl.e_science.git.ivahan0788.GUI.VisualizationPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cellular Automata Lab12");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());
            frame.add(new VisualizationPanel(), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}