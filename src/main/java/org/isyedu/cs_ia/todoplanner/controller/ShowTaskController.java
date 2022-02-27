package org.isyedu.cs_ia.todoplanner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.util.DataCollector;

import java.time.LocalDate;

public class ShowTaskController {
    @FXML private TextField nameField;
    @FXML private DatePicker dateField;
    @FXML private TextArea descriptionField;

    @FXML private CheckBox completeBox;

    @FXML private Button btnSave;
    @FXML private Button btnDelete;

    private int id;

    private final DataCollector dc = new DataCollector();

    /**
     * Method to initialize ShowTask.fxml with the necessary information
     * @param id the id of the task
     * @param name the name of the task
     * @param date the due date of the task
     * @param description the description of the task
     * @param complete the completion status of the task (0 = incomplete, 1 = complete)
     */
    public void setFields(int id, String name, LocalDate date, String description, int complete) {
        this.id = id;
        nameField.setText(name);
        dateField.setValue(date);
        descriptionField.setWrapText(true);
        descriptionField.setText(description);
        completeBox.setSelected(complete == 1);
    }

    @FXML private void save() {
        LocalDate date = dateField.getValue();
        String name = nameField.getText();
        String description = descriptionField.getText();
        int complete = (completeBox.selectedProperty().get()) ? 1 : 0;
        dc.editItem(id, 0, date, name, description, null, null, complete);
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML private void delete() {
        dc.deleteItem(id);
        Stage stage = (Stage) btnDelete.getScene().getWindow();
        stage.close();
    }
}
