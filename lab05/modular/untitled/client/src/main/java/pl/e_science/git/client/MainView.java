package pl.e_science.git.client;

import pl.e_science.git.ivahan0788.AnalysisException;
import pl.e_science.git.ivahan0788.AnalysisService;
import pl.e_science.git.ivahan0788.DataSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class MainView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> algorithmComboBox;
    private JTextArea resultTextArea;

    private ServiceLoader<AnalysisService> analysisServiceLoader;
    private List<AnalysisService> availableServices;

    private String chosenAlgorithmName;

    public MainView() {
        setTitle("Algorithm Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dynamiczne ładowanie serwisów z folderu "services"
        updateServiceLoader();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("A");
        tableModel.addColumn("B");
        tableModel.addColumn("C");
        tableModel.addColumn("D");

        Object[][] data = {
                {"5", "3", "2", "7"},
                {"1", "4", "9", "6"},
                {"8", "5", "3", "4"},
                {"2", "6", "7", "1"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        algorithmComboBox = new JComboBox<>();
        if (availableServices != null) {
            for (AnalysisService service : availableServices) {
                algorithmComboBox.addItem(service.getName());
            }
        }

        panel.add(algorithmComboBox, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JButton runButton = new JButton("Run Algorithm");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenAlgorithmName = (String) algorithmComboBox.getSelectedItem();
                runChosenAlgorithm();
            }
        });

        bottomPanel.add(runButton, BorderLayout.WEST);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        bottomPanel.add(resultScrollPane, BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void updateServiceLoader() {
        try {
            File servicesFolder = new File("services");
            File[] jarFiles = servicesFolder.listFiles((dir, name) -> name.endsWith(".jar"));

            if (jarFiles == null || jarFiles.length == 0) {
                throw new RuntimeException("No service JARs found in /services directory.");
            }

            URL[] urls = new URL[jarFiles.length];
            for (int i = 0; i < jarFiles.length; i++) {
                urls[i] = jarFiles[i].toURI().toURL();
            }

            URLClassLoader urlClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

            analysisServiceLoader = ServiceLoader.load(AnalysisService.class, urlClassLoader);
            availableServices = new ArrayList<>();
            if (algorithmComboBox != null) {
                algorithmComboBox.removeAllItems();
            }

            for (AnalysisService service : analysisServiceLoader) {
                availableServices.add(service);
                if (algorithmComboBox != null) {
                    algorithmComboBox.addItem(service.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading services: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runChosenAlgorithm() {
        if (chosenAlgorithmName == null) {
            showAlert("Error", "Error!", "Can't start analyze, you must choose algorithm!");
            return;
        }

        DataSet dataSet = generateDataSet();

        for (AnalysisService service : availableServices) {
            if (service.getName().equals(chosenAlgorithmName)) {
                try {
                    service.submit(dataSet);
                    DataSet result = service.retrieve(true);
                    String[] resultHeader = result.getHeader();
                    String[][] resultData = result.getData();
                    showResult(resultHeader, resultData);
                } catch (AnalysisException e) {
                    System.err.println("Can't run service " + service.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    private DataSet generateDataSet() {
        DataSet dataSet = new DataSet();
        String[] headers = new String[tableModel.getColumnCount()];

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            headers[i] = tableModel.getColumnName(i);
        }

        String[][] data = getDataFromTable();
        dataSet.setHeader(headers);
        dataSet.setData(data);
        return dataSet;
    }

    private String[][] getDataFromTable() {
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();
        String[][] data = new String[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = (String) tableModel.getValueAt(i, j);
            }
        }

        return data;
    }

    private void showResult(String[] headers, String[][] data) {
        StringBuilder resultBuilder = new StringBuilder();
        for (String header : headers) {
            resultBuilder.append(header).append("\t");
        }
        resultBuilder.append("\n");

        for (String[] row : data) {
            for (String cell : row) {
                resultBuilder.append(cell).append("\t");
            }
            resultBuilder.append("\n");
        }

        resultTextArea.setText(resultBuilder.toString());
    }

    private void showAlert(String title, String header, String content) {
        JOptionPane.showMessageDialog(this, content, header, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
