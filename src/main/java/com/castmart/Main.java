package com.castmart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Infection propagation simulation");
        Scene scene = new Scene(root, 600,850);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
