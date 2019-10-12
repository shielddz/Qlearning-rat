package RL;

import RL.FXMLs.LinkedListBoard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class World extends Application{
    public static double learningRate;
    public static double discountFactor;
    public static int numberEpisodes;
    public static int learningMethod;//0 for q learning, 1 for sarsa
    public static int gridWidth;
    public static int gridHeight;
    public static Environnement env;
    public static LinkedListBoard boards;
    public static Agent agent;
    public static double epsilon;
    public static double totalReward;
    public static double totalTime;
    public static int state;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("/RL/FXMLs/index.fxml"));
        stage.getIcons().add(new Image("/RL/Images/mouse.png"));
        stage.setTitle("Choisir les hyper-paramètres.");
        stage.setScene(new Scene(login));
        stage.show();
    }
}
