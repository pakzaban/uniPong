package com.pakzaban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.setTitle("Ball and Paddle");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
