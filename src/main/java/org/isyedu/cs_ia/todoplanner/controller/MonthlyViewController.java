package org.isyedu.cs_ia.todoplanner.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.Main;
import org.isyedu.cs_ia.todoplanner.ui.GUI;

import javafx.fxml.Initializable;
import org.isyedu.cs_ia.todoplanner.util.DataCollector;
import org.isyedu.cs_ia.todoplanner.util.MonthlyItem;
import org.isyedu.cs_ia.todoplanner.util.Utility;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

public class MonthlyViewController implements Initializable {
    @FXML private Label lblMonth;
    @FXML private Label lblMon;
    @FXML private Label lblTue;
    @FXML private Label lblWed;
    @FXML private Label lblThu;
    @FXML private Label lblFri;
    @FXML private Label lblSat;
    @FXML private Label lblSun;
    @FXML private Label lblToDo;

    @FXML private Pane monthPane;
    @FXML private Pane monPane;
    @FXML private Pane tuePane;
    @FXML private Pane wedPane;
    @FXML private Pane thuPane;
    @FXML private Pane friPane;
    @FXML private Pane satPane;
    @FXML private Pane sunPane;
    @FXML private Pane toDoPane;

    @FXML private GridPane gridPane;
    private LocalDate curDate = LocalDate.now();

    @FXML private ListView<MonthlyItem> toDoList;
    private ObservableList<MonthlyItem> allItems;
    private ObservableList<MonthlyItem> allTasks;

    private final DataCollector dc = new DataCollector();
    private final GUI gui = new GUI();
    private final Utility utility = new Utility();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeControls();
    }

    private void initializeControls() {
        initializeLabels();
        initializeLists();
        initializeGridPane();
    }

    private void initializeLabels() {
        setLblMonth();
        centerLabels();
    }

    private void setLblMonth() {
        lblMonth.setText(curDate.getMonth().toString().charAt(0) +
                curDate.getMonth().toString().substring(1).toLowerCase() +
                " " + curDate.getYear());
    }

    private void centerLabels() {
        Label[] labels = new Label[]{lblMonth, lblMon, lblTue, lblWed, lblThu, lblFri, lblSat, lblSun, lblToDo};
        Pane[] panes = new Pane[]{monthPane, monPane, tuePane, wedPane, thuPane, friPane, satPane, sunPane, toDoPane};

        gui.centerLabelsToPanes(labels, panes);
    }

    private void initializeLists() {
        allItems = FXCollections.observableArrayList();
        allTasks = FXCollections.observableArrayList();
        toDoList.setItems(allTasks);
        toDoList.setOnMouseClicked(mouseEvent ->  toDoList.getSelectionModel().getSelectedItem().showItem());
    }

    private void initializeGridPane() {
        int row = 0;
        int col = curDate.withDayOfMonth(1).getDayOfWeek().getValue()-1;
        int numDays = YearMonth.of(curDate.getYear(), curDate.getMonth().getValue()).lengthOfMonth();
        for (int i = 1; i <= numDays; i++) {
            gridPane.add(datePane(i), col, row);
            if (col == 6) row++;
            col = (col + 1) % 7;
        }
        getItems();
        for (MonthlyItem item : allItems) {
            item.display();
        }
    }

    private StackPane datePane(int date) {
        StackPane stackPane = new StackPane();
        VBox labelBox = new VBox();
        labelBox.setAlignment(Pos.TOP_RIGHT);
        VBox itemBox = new VBox();
        itemBox.setAlignment(Pos.TOP_LEFT);
        itemBox.prefWidthProperty().bind(labelBox.prefWidthProperty());
        Label lblDay = new Label();
        lblDay.setText(Integer.toString(date));
        stackPane.getChildren().add(labelBox);
        labelBox.getChildren().addAll(lblDay, itemBox);
        return stackPane;
    }

    private void getItems() {
        ResultSet items = dc.retrieveItems(curDate.withDayOfMonth(1), curDate.withDayOfMonth(curDate.lengthOfMonth()));
        try {
            while (items.next()) {
                int id = items.getInt("id");
                int type = items.getInt("type");
                LocalDate date = Instant.ofEpochMilli(items.getDate("date").getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                String name = items.getString("name");
                String description = items.getString("description");
                LocalDateTime startTime = (items.getTime("start_time") != null) ? new Timestamp(items.getTime("start_time").getTime()).toLocalDateTime() : null;
                LocalDateTime endTime = (items.getTime("end_time") != null) ? new Timestamp(items.getTime("end_time").getTime()).toLocalDateTime() : null;
                int complete = items.getInt("complete");
                MonthlyItem item = new MonthlyItem(id, type, date, name, description, startTime, endTime, complete, this);
                allItems.add(item);
                if (item.getType() == 0 && item.getComplete() == 0) allTasks.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display a MonthlyItem on the GridPane
     * @param lbl the label of the MonthlyItem to be displayed on the GridPane
     * @param col the column of the GridPane to display the MonthlyItem on
     * @param row the row of the GridPane to display the MonthlyItem on
     */
    public void displayItem(Label lbl, int col, int row) {
        StackPane stackPane = (StackPane) utility.getNodeFromGridPane(gridPane, col, row);
        if (stackPane != null) {
            VBox labelBox = (VBox) stackPane.getChildren().get(0);
            VBox itemBox = (VBox) labelBox.getChildren().get(1);
            itemBox.getChildren().add(lbl);
        }
    }

    /**
     * Method to update the GridPane after a MonthlyItem has been edited or deleted
     */
    public void updateGridPane() {
        gridPane.getChildren().remove(0, gridPane.getChildren().size());
        allItems.clear();
        allTasks.clear();
        initializeGridPane();
    }

    @FXML private void prevMonth() {
        curDate = curDate.minusMonths(1);
        setLblMonth();
        updateGridPane();
    }

    @FXML private void nextMonth() {
        curDate = curDate.plusMonths(1);
        setLblMonth();
        updateGridPane();
    }

    @FXML private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/NewItem.fxml"));
        Stage stage = gui.getStage(loader);
        stage.setTitle("Add Item");
        stage.setResizable(false);
        stage.showAndWait();
        updateGridPane();
    }

    @FXML private void showWeeklyView() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/WeeklyView.fxml"));
        Parent root;
        try {
            root = loader.load();
            Main.primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void showDailyView() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/DailyView.fxml"));
        Parent root;
        try {
            root = loader.load();
            Main.primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void showArchive() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/Archive.fxml"));
        Stage stage = gui.getStage(loader);
        stage.setTitle("Archive");
        stage.setResizable(false);
        stage.showAndWait();
        updateGridPane();
    }
}
