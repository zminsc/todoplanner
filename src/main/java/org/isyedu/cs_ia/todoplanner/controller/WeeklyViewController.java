package org.isyedu.cs_ia.todoplanner.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.Main;
import org.isyedu.cs_ia.todoplanner.ui.GUI;
import org.isyedu.cs_ia.todoplanner.util.DataCollector;
import org.isyedu.cs_ia.todoplanner.util.WeeklyItem;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

public class WeeklyViewController implements Initializable {
    @FXML private Label lblWeek;
    @FXML private Label lblTime;
    @FXML private Label lblMon;
    @FXML private Label lblTue;
    @FXML private Label lblWed;
    @FXML private Label lblThu;
    @FXML private Label lblFri;
    @FXML private Label lblSat;
    @FXML private Label lblSun;
    @FXML private Label lblToDo;

    @FXML private Pane timePane;
    @FXML private Pane weekPane;
    @FXML private Pane monPane;
    @FXML private Pane tuePane;
    @FXML private Pane wedPane;
    @FXML private Pane thuPane;
    @FXML private Pane friPane;
    @FXML private Pane satPane;
    @FXML private Pane sunPane;
    @FXML private Pane toDoPane;

    @FXML private Canvas canvas;
    private LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.FRANCE).getFirstDayOfWeek()));
    private LocalDate weekEnd = weekStart.plusDays(6);

    @FXML private ListView<WeeklyItem> toDoList;
    private ObservableList<WeeklyItem> allItems;
    private ObservableList<WeeklyItem> allTasks;

    private final DataCollector dc = new DataCollector();
    private final GUI gui = new GUI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeControls();
    }

    private void initializeControls() {
        initializeLabels();
        initializeLists();
        initializeCanvas();
    }

    private void initializeLabels() {
        setLblWeek();
        centerLabels();
    }

    private void setLblWeek() {
        String strWeekStart = weekStart.getMonth().toString().charAt(0) + weekStart.getMonth().toString().substring(1).toLowerCase() + " " + weekStart.getDayOfMonth();
        String strWeekEnd = weekEnd.getMonth().toString().charAt(0) + weekEnd.getMonth().toString().substring(1).toLowerCase() + " " + weekEnd.getDayOfMonth();
        lblWeek.setText("Week of " + strWeekStart + " to " + strWeekEnd);

        Label[] dayLabels = new Label[]{lblMon, lblTue, lblWed, lblThu, lblFri, lblSat, lblSun};
        for (int i = 0; i < 7; i++) {
            dayLabels[i].setText(weekStart.plusDays(i).getMonth().toString().charAt(0) + weekStart.plusDays(i).getMonth().toString().substring(1, 3).toLowerCase() + " " + weekStart.plusDays(i).getDayOfMonth());
        }
    }

    private void centerLabels() {
        Label[] labels = new Label[]{lblWeek, lblTime, lblMon, lblTue, lblWed, lblThu, lblFri, lblSat, lblSun, lblToDo};
        Pane[] panes = new Pane[]{weekPane, timePane, monPane, tuePane, wedPane, thuPane, friPane, satPane, sunPane, toDoPane};

        gui.centerLabelsToPanes(labels, panes);
    }

    private void initializeLists() {
        allItems = FXCollections.observableArrayList();
        allTasks = FXCollections.observableArrayList();
        toDoList.setItems(allTasks);toDoList.setOnMouseClicked(mouseEvent -> toDoList.getSelectionModel().selectedItemProperty().get().showItem());

    }

    private void initializeCanvas() {
        GraphicsContext gc = initializeGraphicsContext();
        drawHourlyIntervals(gc);
        getItems();
        displayItems();
        setEventHandlers();
    }

    private GraphicsContext initializeGraphicsContext() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.grayRgb(175));
        gc.setFill(Color.grayRgb(50));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Helvetica", 13));
        return gc;
    }

    private void drawHourlyIntervals(GraphicsContext gc) {
        int hour = 12;
        String ampm = "am";
        for (int pos = 0; pos <= canvas.getHeight(); pos += (canvas.getHeight())/24) {
            gc.fillText(String.format("%d %s", hour, ampm), canvas.getWidth()/16, pos);
            gc.strokeLine(canvas.getWidth()/8, pos, canvas.getWidth(), pos);
            hour++;
            if (hour == 12) {
                ampm = (ampm.equals("am")) ? "pm" : "am";
            } else {
                hour %= 12;
            }
        }
    }

    private void getItems() {
        ResultSet items = dc.retrieveItems(weekStart, weekEnd);
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
                WeeklyItem item = new WeeklyItem(id, type, date, name, description, startTime, endTime, complete, this);
                allItems.add(item);
                if (item.getType() == 0 && item.getComplete() == 0) allTasks.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to draw a WeeklyItem on the canvas
     * @param item the WeeklyItem to be drawn on the canvas
     */
    public void drawItem(WeeklyItem item) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (item.getHovered())
            gc.setFill(Color.grayRgb(150));
        else
            gc.setFill(Color.grayRgb(200));
        gc.fillRect(item.getStartX(), item.getStartY(), item.getEndX()-item.getStartX(), item.getEndY()-item.getStartY());
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Helvetica", 11));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(item.getName(), (item.getStartX() + item.getEndX()) / 2, (item.getStartY() + item.getEndY()) / 2.0);
    }

    private void displayItems() {
        for (WeeklyItem item : allItems)
            item.display();
    }

    private void setEventHandlers() {
        canvas.setOnMouseMoved(mouseEvent -> {
            for (WeeklyItem item : allItems) {
                item.setHovered(mouseEvent.getX(), mouseEvent.getY());
                if (item.getHovered())
                    canvas.getScene().setCursor(Cursor.HAND);
                else
                    canvas.getScene().setCursor(Cursor.DEFAULT);
                item.display();
            }
        });
        canvas.setOnMouseClicked(mouseEvent -> {
            WeeklyItem clickedItem = null;
            for (WeeklyItem item : allItems) {
                if (item.getHovered()) {
                    clickedItem = item;
                    break;
                }
            }
            if (clickedItem != null) {
                clickedItem.showItem();
            }
        });
    }

    /**
     * Method to update the canvas after a WeeklyItem has been modified.
     */
    public void updateCanvas() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        allItems.clear();
        allTasks.clear();
        initializeCanvas();
    }

    @FXML private void prevWeek() {
        weekStart = weekStart.minusWeeks(1);
        weekEnd = weekEnd.minusWeeks(1);
        setLblWeek();
        updateCanvas();
    }

    @FXML private void nextWeek() {
        weekStart = weekStart.plusWeeks(1);
        weekEnd = weekEnd.plusWeeks(1);
        setLblWeek();
        updateCanvas();
    }

    @FXML private void addNewItem() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/NewItem.fxml"));
        Stage stage = gui.getStage(loader);
        stage.setTitle("Add Item");
        stage.setResizable(false);
        stage.showAndWait();
        updateCanvas();
    }

    @FXML private void showMonthlyView() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/MonthlyView.fxml"));
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
        updateCanvas();
    }
}
