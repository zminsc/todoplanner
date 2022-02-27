package org.isyedu.cs_ia.todoplanner.util;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.Main;
import org.isyedu.cs_ia.todoplanner.controller.ShowScheduleItemController;
import org.isyedu.cs_ia.todoplanner.controller.ShowTaskController;
import org.isyedu.cs_ia.todoplanner.ui.GUI;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Item {
    private final int id;
    private final int type;
    private final LocalDate date;
    private final String name;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int complete;

    private final GUI gui = new GUI();

    public abstract void display();

    public Item(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
    }

    /**
     * @return the unique id of the item in the form of an integer
     */
    public int getId() {
        return id;
    }

    /**
     * @return the type of the item in the form of an integer (0 = Task, 1 = ScheduleItem)
     */
    public int getType() {
        return type;
    }

    /**
     * @return the date of the item in the form of a LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the name of the item in the form of a String
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of the item in the form of a String
     */
    public String getDesc() {
        return description;
    }

    /**
     * @return the start time of the item in the form of a LocalDateTime
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @return the end time of the item in the form of a LocalDateTime
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @return the completion status of the item in the form of an integer (0 = incomplete, 1 = complete)
     */
    public int getComplete() {
        return complete;
    }

    /**
     * Method to display the ShowTask.fxml file on a new Stage
     * @param id the unique id of the task in the form of an integer
     * @param name the name of the task in the form of a String
     * @param date the date of the task in the form of a LocalDate
     * @param description the description of the task in the form of a String
     * @param complete the completion status of the task in the form of an integer (0 = incomplete, 1 = complete)
     */
    public void showTask(int id, String name, LocalDate date, String description, int complete) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/ShowTask.fxml"));
        Stage stage = gui.getStage(loader);
        stage.setTitle("Edit Task");
        stage.setResizable(false);
        ShowTaskController stc = loader.getController();
        stc.setFields(id, name, date, description, complete);
        stage.showAndWait();
    }

    /**
     * Method to display the ShowScheduleItem.fxml file on a new Stage
     * @param id the unique id of the schedule item in the form of an integer
     * @param name the name of the schedule item in the form of a String
     * @param date the date of the schedule item in the form of a LocalDate
     * @param description the description of the schedule item in the form of a string
     * @param startTime the start time of the schedule item in the form of a LocalDateTime
     * @param endTime the end time of the schedule item in the form of a LocalDateTime
     */
    public void showScheduleItem(int id, String name, LocalDate date, String description, LocalDateTime startTime, LocalDateTime endTime) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/ShowScheduleItem.fxml"));
        Stage stage = gui.getStage(loader);
        stage.setTitle("Edit Schedule Item");
        stage.setResizable(false);
        ShowScheduleItemController ssic = loader.getController();
        ssic.setFields(id, name, date, description, startTime, endTime);
        stage.showAndWait();
    }
}
