package org.isyedu.cs_ia.todoplanner.util;

import org.isyedu.cs_ia.todoplanner.controller.ArchiveController;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ArchiveItem extends Item {
    private final ArchiveController ac;

    public ArchiveItem(int id, int type, LocalDate date, String name, String description, LocalDateTime startTime, LocalDateTime endTime, int complete, ArchiveController ac) {
        super(id, type, date, name, description, startTime, endTime, complete);
        this.ac = ac;
    }

    /**
     * Method to return the name of the ArchiveItem for display on a ListView
     * @return the name of the ArchiveItem in the form of a String
     */
    public String toString() {
        return getName();
    }

    /**
     * Method to display item information upon request
     */
    public void showItem() {
        showTask(getId(), getName(), getDate(), getDesc(), getComplete());
        ac.updateListView();
    }

    @Override
    public void display() {

    }
}
