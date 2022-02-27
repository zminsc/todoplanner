package org.isyedu.cs_ia.todoplanner.util;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DataCollector {

    private Connection conn;

    private PreparedStatement additionQuery;
    private PreparedStatement editionQuery;
    private PreparedStatement deletionQuery;

    private final Utility utility = new Utility();

    public DataCollector() {
        connect();
        createTable();
        prepareStatements();
    }

    /**
     * Connect to the SQLite database using DriverManager
     */
    private void connect() {
        conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:src/main/resources/org/isyedu/cs_ia/todoplanner/sqlite/items.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the table 'items' in the SQLite database if it doesn't exist
     */
    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS items (
                   id integer PRIMARY KEY,
                   type integer NOT NULL,
                   date date NOT NULL,
                   name text NOT NULL,
                   description text NOT NULL,
                   start_time datetime,
                   end_time datetime,
                   complete integer
                );""";
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatements() {
        try {
            additionQuery = conn.prepareStatement("INSERT INTO items (type, date, name, description, start_time, end_time, complete) VALUES (?, ?, ?, ?, ?, ?, ?)");
            editionQuery = conn.prepareStatement("UPDATE items SET type=?, date=?, name=?, description=?, start_time=?, end_time=?, complete=? WHERE id=?");
            deletionQuery = conn.prepareStatement("DELETE FROM items WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to add a new item to the database
     * @param type the type of the item (0 = Task, 1 = ScheduleItem)
     * @param date the date of the item in the form of a LocalDate
     * @param name the name of the item in the form of a String
     * @param description the description of the item in the form of a String
     * @param startTime the start time of the schedule item in the form of a LocalDateTime
     * @param endTime the end time of the schedule item in the form of a LocalDateTime
     * @param complete the completion status of the task in the form of an integer (0 = incomplete, 1 = complete)
     */
    public void addItem(int type, LocalDate date, String name, String description,
                        LocalDateTime startTime, LocalDateTime endTime, int complete) {
        Date dateSQL = java.sql.Date.valueOf(date);
        Timestamp startTimeSQL = (startTime != null) ? java.sql.Timestamp.valueOf(utility.localDateTimeToDateTime(startTime)) : null;
        Timestamp endTimeSQL = (endTime != null) ? java.sql.Timestamp.valueOf(utility.localDateTimeToDateTime(endTime)) : null;
        try {
            additionQuery.setInt(1, type);
            additionQuery.setDate(2, dateSQL);
            additionQuery.setString(3, name);
            additionQuery.setString(4, description);
            additionQuery.setTimestamp(5, startTimeSQL);
            additionQuery.setTimestamp(6, endTimeSQL);
            additionQuery.setInt(7, complete);
            additionQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to edit an existing item in the database
     * @param id the unique id of the item in the form of an integer
     * @param type the type of the item in the form of an integer (0 = Task, 1 = ScheduleItem)
     * @param date the date of the item in the form of a LocalDate
     * @param name the name of the item in the form of a String
     * @param description the description of the item in the form of a String
     * @param startTime the start time of the schedule item in the form of a LocalDateTime
     * @param endTime the end time of the schedule item in the form of a LocalDateTime
     * @param complete the completion status of the item in the form of an integer (0 = Task, 1 = ScheduleItem)
     */
    public void editItem(int id, int type, LocalDate date, String name, String description,
                           LocalDateTime startTime, LocalDateTime endTime, int complete) {
        Date dateSQL = java.sql.Date.valueOf(date);
        Timestamp startTimeSQL = (startTime != null) ? java.sql.Timestamp.valueOf(utility.localDateTimeToDateTime(startTime)) : null;
        Timestamp endTimeSQL = (endTime != null) ? java.sql.Timestamp.valueOf(utility.localDateTimeToDateTime(endTime)) : null;
        try {
            editionQuery.setInt(1, type);
            editionQuery.setDate(2, dateSQL);
            editionQuery.setString(3, name);
            editionQuery.setString(4, description);
            editionQuery.setTimestamp(5, startTimeSQL);
            editionQuery.setTimestamp(6, endTimeSQL);
            editionQuery.setInt(7, complete);
            editionQuery.setInt(8, id);
            editionQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete an item in the database
     * @param id the unique id of the item
     */
    public void deleteItem(int id) {
        try {
            deletionQuery.setInt(1, id);
            deletionQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to retrieve items from the database between (and including) a specified start and end date
     * @param startDate the earliest date the item can be from
     * @param endDate the latest date the item can be from
     * @return a ResultSet containing information about the desired items from the database
     */
    public ResultSet retrieveItems(LocalDate startDate, LocalDate endDate) {
        ResultSet items = null;
        ZoneId zoneId = ZoneId.systemDefault();
        long startDateSQL = startDate.atStartOfDay(zoneId).toEpochSecond()*1000;
        long endDateSQL = endDate.atStartOfDay(zoneId).toEpochSecond()*1000;
        String query = "SELECT * FROM items WHERE date BETWEEN " + startDateSQL + " AND " + endDateSQL;
        try {
            items = conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Method to retrieve all the items from the database
     * @return a ResultSet containing information about all items in the database
     */
    public ResultSet retrieveAllItems() {
        ResultSet items = null;
        String query = "SELECT * FROM items";
        try {
            items = conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
