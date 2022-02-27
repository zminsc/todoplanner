package org.isyedu.cs_ia.todoplanner.util;

import org.isyedu.cs_ia.todoplanner.controller.WeeklyViewController;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeeklyItem extends Item {
    private final WeeklyViewController wvc;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private boolean hovered = false;

    public WeeklyItem(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete, WeeklyViewController wvc) {
        super(id, type, date, name, description, startTime, endTime, complete);
        this.wvc = wvc;
        initializeCoordinates(startTime, endTime);
    }

    /**
     * Method to display the WeeklyItem on the ListView in the form of a Label
     */
    public String toString() {
        return getName();
    }

    private void initializeCoordinates(LocalDateTime startTime, LocalDateTime endTime) {
        if (getType() == 1) {
            int dayOfWeek = getDate().getDayOfWeek().getValue();
            dayOfWeek = (dayOfWeek == 0) ? 7 : dayOfWeek;

            startX = 52.5 * dayOfWeek;
            startY = startTime.getHour() * 60 + startTime.getMinute();
            endX = startX + 52.5;
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
                wvc.updateCanvas();
            }
            case 1 -> {
                showScheduleItem(getId(), getName(), getDate(), getDesc(), getStartTime(), getEndTime());
                wvc.updateCanvas();
            }
        }
    }

    /**
     * Method to display the WeeklyItem on the canvas
     */
    @Override
    public void display() {
        if (getType() == 1)
            wvc.drawItem(this);
    }

    /**
     * @return the x-coordinate of the leftmost point of WeeklyItem on the canvas
     */
    public double getStartX() {
        return startX;
    }

    /**
     * @return the y-coordinate of the topmost point of WeeklyItem on the canvas
     */
    public double getStartY() {
        return startY;
    }

    /**
     * @return the x-coordinate of the rightmost point of WeeklyItem on the canvas
     */
    public double getEndX() {
        return endX;
    }

    /**
     * @return the y-coordinate of the bottommost point of WeeklyItem on the canvas
     */
    public double getEndY() {
        return endY;
    }

    /**
     * @return whether the WeeklyItem is being hovered over on the canvas
     */
    public boolean getHovered() {
        return getType() == 1 && hovered;
    }

    /**
     * Method to set hover status of WeeklyItem depending on mouse coordinates
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void setHovered(double mouseX, double mouseY) {
        if (getType() == 1)
            hovered = ((mouseX > startX && mouseX < endX)) && ((mouseY > startY && mouseY < endY));
    }
}
