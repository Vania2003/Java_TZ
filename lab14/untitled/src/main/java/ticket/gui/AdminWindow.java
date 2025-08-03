package ticket.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ticket.model.TicketQueueManager;

public class AdminWindow {

    public static void show(TicketQueueManager queueManager) {

        Stage stage = new Stage();
        stage.setTitle("Panel administratora");

        /* ---------- układ ---------- */
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        /* ---------- dodawanie kategorii ---------- */
        TextField categoryField  = new TextField();
        TextField priorityField  = new TextField();
        Button    addCategoryBtn = new Button("Dodaj kategorię");

        addCategoryBtn.setOnAction(e -> {
            String name = categoryField.getText().trim();
            String prio = priorityField.getText().trim();

            if (name.isEmpty() || prio.isEmpty()) {
                GuiUtil.showError("Uzupełnij nazwę i priorytet");
                return;
            }
            try {
                int priority = Integer.parseInt(prio);
                queueManager.addCategory(name, priority);
                GuiUtil.showInfo("Dodano kategorię \"" + name +
                        "\" (priorytet " + priority + ")");
                TicketMachineWindow.refresh(queueManager);     // ⬅️ odśwież Biletomat
                categoryField.clear();
                priorityField.clear();
            } catch (NumberFormatException ex) {
                GuiUtil.showError("Priorytet musi być liczbą całkowitą");
            }
        });

        grid.add(GuiUtil.boldLabel("Nowa kategoria:"), 0, 0);
        grid.add(categoryField,                       1, 0);
        grid.add(GuiUtil.boldLabel("Priorytet:"),     0, 1);
        grid.add(priorityField,                       1, 1);
        grid.add(addCategoryBtn,                      1, 2);

        /* ---------- przypisywanie do stanowiska ---------- */
        ComboBox<Integer> deskSelector = new ComboBox<>(
                FXCollections.observableArrayList(1, 2, 3));
        deskSelector.setPromptText("nr");

        TextField assignCatField = new TextField();
        Button    assignBtn      = new Button("Przypisz kategorię do stanowiska");

        assignBtn.setOnAction(e -> {
            Integer desk = deskSelector.getValue();
            String  cat  = assignCatField.getText().trim();

            if (desk == null || cat.isEmpty()) {
                GuiUtil.showError("Wybierz stanowisko i kategorię");
                return;
            }
            queueManager.assignCategoryToDesk(desk, cat);
            GuiUtil.showInfo("Przypisano \"" + cat + "\" do stanowiska " + desk);
            TicketMachineWindow.refresh(queueManager);         // ⬅️ też odśwież
            assignCatField.clear();
        });

        grid.add(GuiUtil.boldLabel("Stanowisko:"), 0, 4);
        grid.add(deskSelector,                     1, 4);
        grid.add(GuiUtil.boldLabel("Kategoria:"),  0, 5);
        grid.add(assignCatField,                   1, 5);
        grid.add(assignBtn,                        1, 6);

        /* ---------- scena ---------- */
        stage.setScene(new Scene(grid, 400, 300));
        stage.setX(100);
        stage.setY(400);
        stage.show();
    }
}
