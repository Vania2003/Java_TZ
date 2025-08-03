package app;

import processing.Processor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class TaskApp extends JFrame {

    private JTextField taskTextField;
    private JComboBox<String> classComboBox;
    private JTextArea resultTextArea;
    private JButton processButton;
    private JButton unloadClassButton;
    private JButton loadClassesButton;
    private JLabel instructionLabel;
    private JFileChooser fileChooser;
    private ClassLoaderApp classLoaderApp;

    public TaskApp() {
        setTitle("Task Executor");
        setSize(598, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        classLoaderApp = new ClassLoaderApp("C:/Users/битлокер/Desktop/studia/java_ta/lab04/target/classes");
        classLoaderApp.loadClasses();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JLabel taskLabel = new JLabel("Wprowadź zadanie:");
        taskTextField = new JTextField();
        inputPanel.add(taskLabel);
        inputPanel.add(taskTextField);

        JLabel classLabel = new JLabel("Wybierz klasę:");
        classComboBox = new JComboBox<>();
        List<Class<?>> loadedClasses = classLoaderApp.getLoadedClasses();
        for (Class<?> clazz : loadedClasses) {
            classComboBox.addItem(clazz.getSimpleName());
        }
        inputPanel.add(classLabel);
        inputPanel.add(classComboBox);

        // Инструкция для ввода
        instructionLabel = new JLabel("...");
        inputPanel.add(instructionLabel);
        inputPanel.add(new JLabel(""));

        add(inputPanel, BorderLayout.NORTH);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        processButton = new JButton("Wykonaj zadanie");
        buttonPanel.add(processButton);

        unloadClassButton = new JButton("Wyładuj klasę");
        buttonPanel.add(unloadClassButton);

        loadClassesButton = new JButton("Załaduj klasy");
        buttonPanel.add(loadClassesButton);

        add(buttonPanel, BorderLayout.SOUTH);

        resultTextArea = new JTextArea(5, 58);
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.EAST);

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeTask();
            }
        });

        unloadClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unloadClass();
            }
        });

        loadClassesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadClassesFromDirectory();
            }
        });

        // Обновление инструкции при изменении задания
        classComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInstruction();
            }
        });

        // Инициализация JFileChooser для выбора каталога
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz katalog z klasami");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private void updateInstruction() {
        String selectedClassName = (String) classComboBox.getSelectedItem();

        if (selectedClassName != null) {
            switch (selectedClassName) {
                case "CaesarCipherProcessor":
                    instructionLabel.setText("Podaj ciąg znaków i przesunięcie");
                    break;
                case "WeatherProcessor":
                    instructionLabel.setText("Podaj miasto");
                    break;
                case "CSVFilterProcessor":
                    instructionLabel.setText("Podaj ścieżkę do pliku i kolumny(x:y:z)");
                    break;
                default:
                    instructionLabel.setText("...");
                    break;
            }
        }
    }

    private void executeTask() {
        String task = taskTextField.getText();
        if (task.isEmpty()) {
            resultTextArea.setText("Proszę wprowadzić zadanie.");
            return;
        }

        String selectedClassName = (String) classComboBox.getSelectedItem();
        Class<?> selectedClass = null;
        for (Class<?> clazz : classLoaderApp.getLoadedClasses()) {
            if (clazz.getSimpleName().equals(selectedClassName)) {
                selectedClass = clazz;
                break;
            }
        }

        if (selectedClass == null) {
            resultTextArea.setText("Proszę wybrać klasę.");
            return;
        }

        try {
            Processor processor = (Processor) selectedClass.getConstructor().newInstance();
            processor.submitTask(task, status -> {
                if (status.getProgress() == 100) {
                    resultTextArea.setText("Wynik: " + processor.getResult());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            resultTextArea.setText("Wystąpił błąd podczas wykonywania zadania.");
        }
    }

    private void unloadClass() {
        String selectedClassName = (String) classComboBox.getSelectedItem();
        if (selectedClassName != null) {
            classLoaderApp.getLoadedClasses().removeIf(clazz -> clazz.getSimpleName().equals(selectedClassName));
            classComboBox.removeItem(selectedClassName);
            resultTextArea.setText("Usunięto klasę: " + selectedClassName);
        } else {
            resultTextArea.setText("Nie wybrano klasy do usunięcia.");
        }
    }

    private void loadClassesFromDirectory() {
        // Открываем окно выбора каталога
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            classLoaderApp = new ClassLoaderApp(selectedDirectory.getAbsolutePath());
            classLoaderApp.loadClasses();
            updateClassComboBox();
            resultTextArea.setText("Załadowano klasy z katalogu: " + selectedDirectory.getAbsolutePath());
        }
    }

    private void updateClassComboBox() {
        classComboBox.removeAllItems();
        List<Class<?>> loadedClasses = classLoaderApp.getLoadedClasses();
        for (Class<?> clazz : loadedClasses) {
            classComboBox.addItem(clazz.getSimpleName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskApp app = new TaskApp();
            app.setVisible(true);
        });
    }
}
