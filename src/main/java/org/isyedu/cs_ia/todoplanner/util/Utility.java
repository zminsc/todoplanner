package org.isyedu.cs_ia.todoplanner.util;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {

    /**
     * Method to convert a LocalDateTime object to a String format for insertion into the SQL database
     * @param dateTime the LocalDateTime object to be converted
     * @return the String representation of the LocalDateTime object
     */
    public String localDateTimeToDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "null";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Method to create a LocalDateTime object from date, hour, minute, and ampm fields
     * @param date the date of the object in the form of a LocalDate
     * @param hh the hour of the object in the form of an integer
     * @param mm the minute of the object in the form of an integer
     * @param ampm whether the hour is in am or pm in the form of a String ("am" = am, "pm" = pm)
     * @return the created LocalDateTime object
     */
    public LocalDateTime fieldsToLocalDateTime(LocalDate date, int hh, int mm, String ampm) {
        if (ampm.equals("pm") && hh != 12) {
            hh += 12;
        } else if (ampm.equals("am") && hh == 12) {
            hh -= 12;
        }
        return date.atTime(hh, mm);
    }

    /**
     * Method to get the StackPane of a specific day from the GridPane
     * @param gridPane the GridPane
     * @param col the column index of the GridPane (indexed at 0)
     * @param row the row index of the GridPane (indexed at 1)
     * @return the Node at the specific column and row indexes of the GridPane, to be typecast to StackPane
     */
    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Method to initialize the startTime and endTime hour, minute, and ampm fields in the NewItem and ShowItem dialog boxes
     * @param startTimeHH the startTime hour field in the form of a ComboBox
     * @param endTimeHH the endTime hour field in the form of a ComboBoz
     * @param startTimeMM the startTime minute field in the form of a ComboBox
     * @param endTimeMM the endTime minute field in the form of a ComboBox
     * @param startTimeAMPM the startTime ampm field in the form of a ChoiceBox
     * @param endTimeAMPM the endTime ampm field in the form of a ChoiceBox
     */
    public void initializeTimeFields(ComboBox<Integer> startTimeHH, ComboBox<Integer> endTimeHH, ComboBox<String> startTimeMM, ComboBox<String> endTimeMM, ChoiceBox<String> startTimeAMPM, ChoiceBox<String> endTimeAMPM) {
        for (int i = 1; i <= 12; i++) {
            startTimeHH.getItems().add(i);
            endTimeHH.getItems().add(i);
        }
        for (int i = 0; i <= 59; i++) {
            startTimeMM.getItems().add(String.format("%02d", i));
            endTimeMM.getItems().add(String.format("%02d", i));
        }
        startTimeAMPM.getItems().addAll("am", "pm");
        endTimeAMPM.getItems().addAll("am", "pm");
    }
}
