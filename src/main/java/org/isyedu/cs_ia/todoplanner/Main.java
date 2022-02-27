package org.isyedu.cs_ia.todoplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.isyedu.cs_ia.todoplanner.controller.MonthlyViewController;

import java.io.IOException;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("fxml/MonthlyView.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("ToDoPlanner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}