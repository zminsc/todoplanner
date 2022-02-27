module org.isyedu.cs_ia.todoplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.isyedu.cs_ia.todoplanner.controller to javafx.fxml;
    exports org.isyedu.cs_ia.todoplanner;
}