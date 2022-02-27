package org.isyedu.cs_ia.todoplanner.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.isyedu.cs_ia.todoplanner.ui.GUI;
import org.isyedu.cs_ia.todoplanner.util.ArchiveItem;
import org.isyedu.cs_ia.todoplanner.util.DataCollector;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ArchiveController implements Initializable {
    @FXML private Label lblArchive;
    @FXML private Pane archivePane;

    @FXML private ListView<ArchiveItem> archive;
    private ObservableList<ArchiveItem> completedTasks;

    private final DataCollector dc = new DataCollector();
    private final GUI gui = new GUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeControls();
    }

    private void initializeControls() {
        initializeLabels();
        initializeLists();
        initializeListView();
    }

    private void initializeLabels() {
        Label[] labels = new Label[]{lblArchive};
        Pane[] panes = new Pane[]{archivePane};

        gui.centerLabelsToPanes(labels, panes);
    }

    private void initializeLists() {
        completedTasks = FXCollections.observableArrayList();
        archive.setItems(completedTasks);
        archive.setOnMouseClicked(mouseEvent -> archive.getSelectionModel().selectedItemProperty().get().showItem());
    }

    private void initializeListView() {
        getItems();
    }

    private void getItems() {
        ResultSet items = dc.retrieveAllItems();
        try {
            while (items.next()) {
                int type = items.getInt("type");
                int complete = items.getInt("complete");
                if (type == 0 && complete == 1) {
                    int id = items.getInt("id");
                    LocalDate date = Instant.ofEpochMilli(items.getDate("date").getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    String name = items.getString("name");
                    String description = items.getString("description");
                    LocalDateTime startTime = (items.getTime("start_time") != null) ? new Timestamp(items.getTime("start_time").getTime()).toLocalDateTime() : null;
                    LocalDateTime endTime = (items.getTime("end_time") != null) ? new Timestamp(items.getTime("end_time").getTime()).toLocalDateTime() : null;
                    ArchiveItem item = new ArchiveItem(id, type, date, name, description, startTime, endTime, complete, this);
                    completedTasks.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to update the ListView after an ArchiveItem has been edited or deleted
     */
    public void updateListView() {
        completedTasks.clear();
        initializeListView();
    }
}
