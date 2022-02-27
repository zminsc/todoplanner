package org.isyedu.cs_ia.todoplanner.util;

import org.isyedu.cs_ia.todoplanner.controller.DailyViewController;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyItem extends Item {
    private final DailyViewController dvc;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private boolean hovered = false;

    public DailyItem(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete, DailyViewController dvc) {
        super(id, type, date, name, description, startTime, endTime, complete);
        this.dvc = dvc;
        initializeCoordinates(startTime, endTime);
    }

    /**
     * Method to return the name of the DailyItem for display on a ListView
     * @return the name of the DailyItem in the form of a String
     */
    public String toString() {
        return getName();
    }

    private void initializeCoordinates(LocalDateTime startTime, LocalDateTime endTime) {
        if (getType() == 1) {
            startX = 52.5;
            startY = startTime.getHour() * 60 + startTime.getMinute();
            endX = 420;
            endY = startY + 60 * (endTime.getHour() - startTime.getHour()) + (endTime.getMinute() - startTime.getMinute());
        }
    }

    /**
     * Method to display item information upon request
     */
    public void showItem() {
        switch (getType()) {
            case 0 -> {
                showTask(getId(), getName(), getDate(), getDesc(), getComplete());
                dvc.updateCanvas();
            }
            case 1 -> {
                showScheduleItem(getId(), getName(), getDate(), getDesc(), getStartTime(), getEndTime());
                dvc.updateCanvas();
            }
        }
    }

    /**
     * Method to display DailyItem schedule items on canvas
     */
    @Override
    public void display() {
        if (getType() == 1) {
            dvc.drawItem(this);
        }
    }

    /**
     * @return the x-coordinate of the leftmost point of DailyItem on canvas
     */
    public double getStartX() {
        return startX;
    }

    /**
     * @return the y-coordinate of the topmost point of DailyItem on canvas
     */
    public double getStartY() {
        return startY;
    }

    /**
     * @return the x-coordinate of the rightmost point of DailyItem on canvas
     */
    public double getEndX() {
        return endX;
    }

    /**
     * @return the y-coordinate of the bottommost point of DailyItem on canvas
     */
    public double getEndY() {
        return endY;
    }

    /**
     * @return whether the DailyItem is being hovered over on the canvas
     */
    public boolean getHovered() {
        return getType() == 1 && hovered;
    }

    /**
     * Method to set hover status of DailyItem depending on mouse coordinates
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void setHovered(double mouseX, double mouseY) {
        if (getType() == 1)
            hovered = ((mouseX > startX && mouseX < endX)) && ((mouseY > startY && mouseY < endY));
    }
}
