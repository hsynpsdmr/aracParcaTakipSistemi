/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uygulama;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Seyf
 */
public class AracParcaTakipSistemi extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
       AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("AnasayfaFxml.fxml"));
			Scene scene = new Scene(root,600,400);
			primaryStage.setScene(scene);
                        primaryStage.setTitle("Hoşgeldiniz");
                        primaryStage.setResizable(false);
			primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
