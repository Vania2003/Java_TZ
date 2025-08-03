package pl.app;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MeasurementViewer extends JFrame {
    private FileCache fileCache = new FileCache();

    // Komponenty GUI
    private JEditorPane previewArea;  // JEditorPane obsługuje HTML
    private JLabel sourceLabel;
    private JTree fileTree;
    private File rootDirectory;

    // ComboBox do wyboru sposobu renderowania
    private JComboBox<String> rendererCombo;
    // ComboBox do wyboru zakresu danych
    private JComboBox<String> dataScopeCombo;

    // Używamy tylko jednego algorytmu przetwarzania – średnich
    private DataProcessor dataProcessor = new AverageDataProcessor();
    private PreviewRenderer previewRenderer;

    public MeasurementViewer() {
        setTitle("Measurement Data Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Ustal folder bazowy (folder DanePomiarowe musi być w katalogu roboczym)
        rootDirectory = new File("DanePomiarowe");

        // Panel sterowania – wybór renderera i zakresu danych
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        controlPanel.add(new JLabel("Preview Renderer:"));
        rendererCombo = new JComboBox<>(new String[] {"Default Text", "Table HTML"});
        rendererCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreviewRenderer();
                if (fileTree != null) {
                    onFileSelected();
                }
            }
        });
        controlPanel.add(rendererCombo);

        controlPanel.add(new JLabel("Data Scope:"));
        dataScopeCombo = new JComboBox<>(new String[] {"Entire File", "Partial File"});
        dataScopeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileTree != null) {
                    onFileSelected();
                }
            }
        });
        controlPanel.add(dataScopeCombo);

        // Ustawienia domyślne
        rendererCombo.setSelectedIndex(0);
        dataScopeCombo.setSelectedIndex(0);
        updatePreviewRenderer();

        // Lewy panel – drzewo plików
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory.getName());
        createFileTree(rootDirectory, rootNode);
        fileTree = new JTree(rootNode);
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                onFileSelected();
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(fileTree);

        // Prawy panel – podgląd
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewArea = new JEditorPane();
        previewArea.setEditable(false);
        JScrollPane previewScrollPane = new JScrollPane(previewArea);
        sourceLabel = new JLabel("Source: ");
        previewPanel.add(previewScrollPane, BorderLayout.CENTER);
        previewPanel.add(sourceLabel, BorderLayout.SOUTH);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, previewPanel);
        splitPane.setDividerLocation(300);

        // Główny panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
    }

    private void updatePreviewRenderer() {
        String selected = (String) rendererCombo.getSelectedItem();
        if ("Default Text".equals(selected)) {
            previewRenderer = new DefaultPreviewRenderer();
        } else if ("Table HTML".equals(selected)) {
            previewRenderer = new TablePreviewRenderer();
        }
    }

    private void createFileTree(File dir, DefaultMutableTreeNode node) {
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
            for (File file : files) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName());
                node.add(child);
                if (file.isDirectory()) {
                    createFileTree(file, child);
                }
            }
        }
    }

    private void onFileSelected() {
        if (fileTree == null) return;
        TreePath path = fileTree.getSelectionPath();
        if (path == null) return;
        StringBuilder filePathBuilder = new StringBuilder();
        Object[] nodes = path.getPath();
        filePathBuilder.append(rootDirectory.getAbsolutePath());
        for (int i = 1; i < nodes.length; i++) {
            filePathBuilder.append(File.separator).append(nodes[i].toString());
        }
        File selectedFile = new File(filePathBuilder.toString());
        if (selectedFile.isFile() && (selectedFile.getName().endsWith(".csv") || selectedFile.getName().endsWith(".json"))) {
            loadAndDisplayFile(selectedFile);
        }
    }

    private FileLoader chooseLoader(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".csv")) {
            return new CSVLoader();
        } else if (name.endsWith(".json")) {
            return new JSONLoader();
        } else {
            return new CSVLoader();
        }
    }

    private void loadAndDisplayFile(File file) {
        FileData fileData;
        boolean fromCache = false;
        WeakReference<FileData> ref = fileCache.get(file.getAbsolutePath());
        if (ref != null && ref.get() != null) {
            fileData = ref.get();
            fromCache = true;
        } else {
            FileLoader loader = chooseLoader(file);
            fileData = loader.loadFile(file);
            fileCache.put(file.getAbsolutePath(), fileData);
        }
        // Sprawdzenie zakresu danych – cały plik czy tylko jego część
        String scope = (String) dataScopeCombo.getSelectedItem();
        if ("Partial File".equals(scope)) {
            List<DataRecord> fullRecords = fileData.getRecords();
            int half = fullRecords.size() / 2;  // bierzemy pierwszą połowę rekordów
            List<DataRecord> partialRecords = new ArrayList<>(fullRecords.subList(0, half));
            fileData = new FileData(partialRecords);
        }
        DataProcessingResult result = dataProcessor.process(fileData);
        String previewText = previewRenderer.renderPreview(fileData, result);
        if (previewText.trim().toLowerCase().startsWith("<html>")) {
            previewArea.setContentType("text/html");
        } else {
            previewArea.setContentType("text/plain");
        }
        previewArea.setText(previewText);
        previewArea.setCaretPosition(0);
        sourceLabel.setText("Source: " + (fromCache ? "Cache" : "Loaded fresh"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MeasurementViewer().setVisible(true);
        });
    }
}
