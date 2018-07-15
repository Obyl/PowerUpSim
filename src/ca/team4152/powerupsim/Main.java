package ca.team4152.powerupsim;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage stage){
        Window window = new Window();

        stage.setScene(window);
        stage.setResizable(false);
        stage.setTitle("FIRST Power-Up Game Simulator");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.show();
    }

    public static void main(String[] args){
        Main.launch(args);
    }

}