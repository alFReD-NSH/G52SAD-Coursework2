package com.github.alFReDNSH.imageViewer;

import com.github.alFReDNSH.imageViewer.conflictDialog.ConflictDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    public void start(Stage stage) throws Exception
    {
        // Load the FXML file.
        Parent parent = FXMLLoader.load(
                getClass().getResource("MainView.fxml"));

        // Build the scene graph.
        Scene scene = new Scene(parent);

        // Display our window, using the scene graph.
        stage.setTitle("ImageViewDemo");
        stage.setScene(scene);
        scene.getStylesheets().add(
                getClass().getResource("MainView.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args)
    {
        // Launch the application.
        launch(args);
    }
}
