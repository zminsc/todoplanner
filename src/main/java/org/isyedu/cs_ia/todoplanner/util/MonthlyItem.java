package org.isyedu.cs_ia.todoplanner.util;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.isyedu.cs_ia.todoplanner.controller.MonthlyViewController;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthlyItem extends Item {
    private final MonthlyViewController mvc;
    private final Label itemLbl = new Label();

    public MonthlyItem(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete, MonthlyViewController mvc) {
        super(id, type, date, name, description, startTime, endTime, complete);
        this.mvc = mvc;
        if (getType() == 1)
            initializeLabel();
    }

    /**
     * Method to display the MonthlyItem on the ListView in the form of a Label
     */
    @Override
    public void display() {
        if (getType() == 1) {
            itemLbl.setText(getName());
            int col = getDate().getDayOfWeek().getValue()-1;
            int row = (getDate().getDayOfMonth()-(getDate().withDayOfMonth(1).getDayOfWeek().getValue()-1)) / 7;
            mvc.displayItem(itemLbl, col, row);
        }
    }

    /**
     * Method to return the name of the MonthlyItem for display on a ListView
     * @return the name of the MonthlyItem in the form of a String
     */
    public String toString() {
        return getName();
    }

    private void initializeLabel() {
        itemLbl.setText(getName());
        itemLbl.setMaxWidth(Double.MAX_VALUE);
        itemLbl.cursorProperty().set(Cursor.HAND);
        itemLbl.setOnMouseEntered(mouseEvent -> itemLbl.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200),
                CornerRadii.EMPTY, Insets.EMPTY))));
        itemLbl.setOnMouseExited(mouseEvent -> itemLbl.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,
                CornerRadii.EMPTY, Insets.EMPTY))));
        itemLbl.setOnMouseClicked(mouseEvent -> {
            showItem();
            mvc.updateGridPane();
        });
    }

    /**
     * Method to display the item's information upon request
     */
    public void showItem() {
        switch (getType()) {
            case 0 -> {
                showTask(getId(), getName(), getDate(), getDesc(), getComplete());
                mvc.updateGridPane();
            }
            case 1 -> {
                showScheduleItem(getId(), getName(), getDate(), getDesc(), getStartTime(), getEndTime());
                mvc.updateGridPane();
            }
        }
    }
}
