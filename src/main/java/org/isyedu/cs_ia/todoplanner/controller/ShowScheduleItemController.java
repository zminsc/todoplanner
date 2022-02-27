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

public class ShowScheduleItemController implements Initializable  {
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
    @FXML private Button btnDelete;

    private int id;

    private final DataCollector dc = new DataCollector();
    private final Utility utility = new Utility();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeControls();
    }

    private void initializeControls() {
        utility.initializeTimeFields(startTimeHH, endTimeHH, startTimeMM, endTimeMM, startTimeAMPM, endTimeAMPM);
    }

    /**
     * Method to initialize ShowScheduleItem.fxml with the necessary information
     * @param id the id of the schedule item
     * @param name the name of the schedule item
     * @param date the date of the schedule item
     * @param description the description of the schedule item
     * @param startTime the start time of the schedule item
     * @param endTime the end time of the schedule item
     */
    public void setFields(int id, String name, LocalDate date, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        nameField.setText(name);
        dateField.setValue(date);
        descriptionField.setText(description);
        descriptionField.setWrapText(true);
        if (startTime.getHour() <= 11) {
            startTimeHH.setValue((startTime.getHour() == 0) ? 12 : startTime.getHour());
            startTimeAMPM.setValue("am");
        } else {
            startTimeHH.setValue((startTime.getHour() == 12) ? 12 : startTime.getHour() - 12);
            startTimeAMPM.setValue("pm");
        }
        if (endTime.getHour() <= 11) {
            endTimeHH.setValue((endTime.getHour() == 0) ? 12 : endTime.getHour());
            endTimeAMPM.setValue("am");
        } else {
            endTimeHH.setValue((endTime.getHour() == 12) ? 12 : endTime.getHour() - 12);
            endTimeAMPM.setValue("pm");
        }
        startTimeMM.setValue(String.format("%02d", startTime.getMinute()));
        endTimeMM.setValue(String.format("%02d", endTime.getMinute()));
    }

    @FXML private void save() {
        LocalDate date = dateField.getValue();
        String name = nameField.getText();
        String description = descriptionField.getText();
        LocalDateTime startTime = utility.fieldsToLocalDateTime(date, startTimeHH.getValue(), Integer.parseInt(startTimeMM.getValue()), startTimeAMPM.getValue());
        LocalDateTime endTime = utility.fieldsToLocalDateTime(date, endTimeHH.getValue(), Integer.parseInt(endTimeMM.getValue()), endTimeAMPM.getValue());
        dc.editItem(id, 1, date, name, description, startTime, endTime, 1);
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML private void delete() {
        dc.deleteItem(id);
        Stage stage = (Stage) btnDelete.getScene().getWindow();
        stage.close();
    }
}
