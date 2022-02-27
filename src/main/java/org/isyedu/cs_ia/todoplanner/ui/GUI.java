package org.isyedu.cs_ia.todoplanner.ui;

import org.isyedu.cs_ia.todoplanner.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI {

    /**
     * Method to center a label to a pane on the user interface
     * @param lbl the label to be centered
     * @param pane the pane to center the label to
     */
    public void centerLblToPane(Label lbl, Pane pane) {
        lbl.layoutXProperty().bind(pane.widthProperty().subtract(lbl.widthProperty()).divide(2));
        lbl.layoutYProperty().bind(pane.heightProperty().subtract(lbl.heightProperty()).divide(2));
    }

    /**
     * Method to center multiple labels to their respective panes
     * @param labels the parallel array of labels to center
     * @param panes the parallel array of panes to be centered to
     */
    public void centerLabelsToPanes(Label[] labels, Pane[] panes) {
        if (labels.length == panes.length) {
            for (int i = 0; i < labels.length; i++) {
                centerLblToPane(labels[i], panes[i]);
            }
        }
    }

    /**
     * Method to generate a stage for an FXMLLoader loader
     * @param loader the loader containing the FXML file to display on the stage
     * @return the stage on which the FXML file is initialized
     */
    public Stage getStage(FXMLLoader loader){
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Stage inputStage = new Stage();
        inputStage.initOwner(Main.primaryStage);
        inputStage.setScene(newScene);
        return inputStage;
    }
}