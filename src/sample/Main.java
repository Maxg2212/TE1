package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;


public class Main extends Application {

    private TextArea messages = new TextArea();

    private Parent createContent() {
        messages.setPrefHeight(550);
        TextField input = new TextField();


        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600,600);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
