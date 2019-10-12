package RL.FXMLs;

import RL.Agent;
import RL.Environnement;
import RL.World;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class IndexController {
    @FXML
    JFXTextField gridL;
    @FXML
    JFXTextField gridC;
    @FXML
    JFXTextField learningRate;
    @FXML
    JFXTextField discountFactor;
    @FXML
    JFXTextField epsilon;
    @FXML
    JFXTextField numberEpisodes;
    @FXML
    JFXRadioButton q;
    @FXML
    JFXRadioButton s;
    @FXML
    JFXRadioButton lots;
    @FXML
    JFXRadioButton few;
    @FXML
    JFXButton train;
    @FXML
    Label errorMessage;

    @FXML
    private void Train(ActionEvent event) throws IOException {
        boolean check = true;
        if(gridC.getText().isEmpty() || gridL.getText().isEmpty() || learningRate.getText().isEmpty() || discountFactor.getText().isEmpty() || numberEpisodes.getText().equals("")){
            check = false;
        }
        try{
            if(Integer.parseInt(numberEpisodes.getText()) <= 0 ||
                    Float.valueOf(discountFactor.getText()) < 0 || Float.valueOf(discountFactor.getText()) > 1 ||
                    Float.valueOf(learningRate.getText()) < 0 || Float.valueOf(learningRate.getText()) > 1 ||
                    Float.valueOf(epsilon.getText()) < 0 || Float.valueOf(epsilon.getText()) > 1 ||
                    Integer.parseInt(gridL.getText()) <= 0 || Integer.parseInt(gridC.getText()) <= 0){
                check = false;
            }
        } catch (NumberFormatException e) {
            check = false;
        }

        if(check){
            errorMessage.setVisible(false);
            World.gridHeight = Integer.parseInt(gridL.getText());
            World.gridWidth = Integer.parseInt(gridC.getText());
            World.learningRate = Double.parseDouble(learningRate.getText());
            World.discountFactor = Double.parseDouble(discountFactor.getText());
            World.epsilon = Double.parseDouble(epsilon.getText());
            World.numberEpisodes = Integer.parseInt(numberEpisodes.getText());
            World.learningMethod = (q.isSelected()?0:1);
            Parent training = FXMLLoader.load(getClass().getResource("/RL/FXMLs/training.fxml"));
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(training));
            window.setTitle("Entrainement...");
            window.show();
            if(lots.isSelected()){
                World.env = new Environnement(World.gridHeight, World.gridWidth, 30, 25, 20);
            }else{
                World.env = new Environnement(World.gridHeight, World.gridWidth, 10, 10, 10);
            }

            World.agent = new Agent(World.env, World.learningRate, World.discountFactor, World.epsilon);
            World.boards = new LinkedListBoard(World.env.board);
            if(World.learningMethod == 0){
                World.totalTime = World.agent.trainQ(World.numberEpisodes);
            }else{
                World.totalTime = World.agent.trainS(World.numberEpisodes);
            }
            World.totalReward = World.agent.totalReward();
            Parent end = FXMLLoader.load(getClass().getResource("/RL/FXMLs/end.fxml"));
            window.setScene(new Scene(end));
            window.setTitle("Résultats...");
            window.show();
        }else{
            errorMessage.setVisible(true);
        }
    }
}
