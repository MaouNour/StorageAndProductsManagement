// src/application/Main.java
package UI.application;

import BackEnd.Service;
import BackEnd.Exceptions.IDException;
import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Exceptions.PastDatesException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/MainUI.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Supply Chain System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            Service.createData();
        } catch (PastDatesException | IDException | IllegalAvailableAmountException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
