package org.isyedu.cs_ia.todoplanner.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.util.DataCollector;
import org.isyedu.cs_ia.todoplanner.util.Utility;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class NewItemController implements Initializable {
    @FXML private ChoiceBox<String> itemSelector;

    @FXML private Label lblName;
    @FXML private Label lblDate;
    @FXML private Label lblDescription;
    @FXML private Label lblStartTime;
    @FXML private Label lblStartTimeHH;
    @FXML private Label lblStartTimeMM;
    @FXML private Label lblStartTimeAMPM;
    @FXML private Label lblEndTime;
    @FXML private Label lblEndTimeHH;
    @FXML private Label lblEndTimeMM;
    @FXML private Label lblEndTimeAMPM;

    @FXML private TextField nameField;
    @FXML private DatePicker dateField;
    @FXML private TextArea descriptionField;

    @FXML private ComboBox<Integer> startTimeHH;
    @FXML private ComboBox<String> startTimeMM;
    @FXML private ChoiceBox<String> startTimeAMPM;

    @FXML private ComboBox<Integer> endTimeHH;
    @FXML private ComboBox<String> endTimeMM;
    @FXML private ChoiceBox<String> endTimeAMPM;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final DataCollector dc = new DataCollector();
    private final Utility utility = new Utility();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultView();
        initializeControls();
    }

    private void initializeControls() {
        initializeItemSelector();
        utility.initializeTimeFields(startTimeHH, endTimeHH, startTimeMM, endTimeMM, startTimeAMPM, endTimeAMPM);
        descriptionField.setWrapText(true);
    }

    private void initializeItemSelector() {
        itemSelector.getItems().addAll("Task", "Schedule Item");
        itemSelector.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            if (current.equals("Task")) {
                taskView();
            } else if (current.equals("Schedule Item")) {
                scheduleItemView();
            }
        });
    }

    private void defaultView() {
        lblName.setVisible(false);
        lblDate.setVisible(false);
        lblDescription.setVisible(false);
        lblStartTime.setVisible(false);
        lblStartTimeHH.setVisible(false);
        lblStartTimeMM.setVisible(false);
        lblStartTimeAMPM.setVisible(false);
        lblEndTime.setVisible(false);
        lblEndTimeHH.setVisible(false);
        lblEndTimeMM.setVisible(false);
        lblEndTimeAMPM.setVisible(false);
        nameField.setVisible(false);
        dateField.setVisible(false);
        descriptionField.setVisible(false);
        startTimeHH.setVisible(false);
        startTimeMM.setVisible(false);
        startTimeAMPM.setVisible(false);
        endTimeHH.setVisible(false);
        endTimeMM.setVisible(false);
        endTimeAMPM.setVisible(false);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
    }

    private void taskView() {
        defaultView();
        lblName.setVisible(true);
        lblDate.setVisible(true);
        lblDescription.setVisible(true);
        nameField.setVisible(true);
        dateField.setVisible(true);
        descriptionField.setVisible(true);
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
    }

    private void scheduleItemView() {
        taskView();
        lblStartTime.setVisible(true);
        lblStartTimeHH.setVisible(true);
        lblStartTimeMM.setVisible(true);
        lblStartTimeAMPM.setVisible(true);
        lblEndTime.setVisible(true);
        lblEndTimeHH.setVisible(true);
        lblEndTimeMM.setVisible(true);
        lblEndTimeAMPM.setVisible(true);
        startTimeHH.setVisible(true);
        startTimeMM.setVisible(true);
        startTimeAMPM.setVisible(true);
        endTimeHH.setVisible(true);
        endTimeMM.setVisible(true);
        endTimeAMPM.setVisible(true);
    }

    @FXML private void save() {
        int type;
        LocalDate date = dateField.getValue();
        String name = nameField.getText();
        String description = descriptionField.getText();

        switch (itemSelector.getSelectionModel().getSelectedItem()) {
            case "Task" -> {
                type = 0;
                dc.addItem(type, date, name, description, null, null, 0);
            }
            case "Schedule Item" -> {
                type = 1;
                LocalDateTime startTime = utility.fieldsToLocalDateTime(date, startTimeHH.getValue(), Integer.parseInt(startTimeMM.getValue()), startTimeAMPM.getValue());
                LocalDateTime endTime = utility.fieldsToLocalDateTime(date, endTimeHH.getValue(), Integer.parseInt(startTimeMM.getValue()), endTimeAMPM.getValue());
                dc.addItem(type, date, name, description, startTime, endTime, 0);
            }
        }
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML private void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
