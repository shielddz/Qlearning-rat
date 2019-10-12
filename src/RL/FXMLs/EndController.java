package RL.FXMLs;

import RL.World;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class EndController implements Initializable {
    private int value;
    private boolean backup;
    @FXML
    JFXButton backToIndex;
    @FXML
    Label rewardLabel;
    @FXML
    Label timeText;
    @FXML
    Label timeLabel;
    @FXML
    JFXButton next;
    @FXML
    JFXButton previous;
    @FXML
    AnchorPane insideAnchor;

    private GridPane mouseGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mouseGrid = new GridPane();
        mouseGrid.setStyle("-fx-background-color:#3F5765;");
        insideAnchor.getChildren().add(mouseGrid);

        if(World.totalReward == -500){
            rewardLabel.setText("Entrainement échoué.");
            rewardLabel.setTextFill(Color.web("#FF530D"));
            timeText.setVisible(false);
            timeLabel.setVisible(false);
        }else{
            DecimalFormat df = new DecimalFormat("#.#");
            rewardLabel.setText("Récompenses accumulées: "+Float.parseFloat(df.format(World.totalReward))+" points.");
            rewardLabel.setTextFill(Color.web("#2b3a42"));
            timeText.setVisible(true);
            timeLabel.setVisible(true);
            timeLabel.setText(World.totalTime+" ms.");
        }


        World.env.initState();
        World.state = World.env.mouseS;
        //calculate the image size:
        int maxWidth = 900/World.gridWidth;
        int maxHeight = 500/World.gridHeight;
        value = Integer.min(maxWidth, maxHeight);
        value = Integer.min(value, 80);

        mouseGrid.setVgap(2);
        mouseGrid.setHgap(2);
        mouseGrid.setPrefWidth(value*World.gridWidth);
        mouseGrid.setPrefHeight(value*World.gridHeight);

        //columns and rows constraints
        for (int i = 0; i < World.gridHeight; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(value);
            mouseGrid.getRowConstraints().add(row);
        }
        for (int i = 0; i < World.gridWidth; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(value);
            mouseGrid.getColumnConstraints().add(col);
        }

        previous.setDisable(true);

        //dessiner la grille
        draw();

        //centering the grid
        double x = (insideAnchor.getPrefWidth() - mouseGrid.getPrefWidth()) / 2;
        double y = (insideAnchor.getPrefHeight() - mouseGrid.getPrefHeight()) / 2;
        AnchorPane.setTopAnchor(mouseGrid, y);
        AnchorPane.setRightAnchor(mouseGrid, x);
        AnchorPane.setBottomAnchor(mouseGrid, y);
        AnchorPane.setLeftAnchor(mouseGrid, x);
    }

    @FXML
    private void backToIndex(ActionEvent event) throws IOException {
        next.setDisable(false);
        Parent training = FXMLLoader.load(getClass().getResource("/RL/FXMLs/index.fxml"));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(training));
        window.setTitle("Choisir les hyper-paramètres.");
        window.show();
    }

    @FXML
    private void step(){
        boolean end;
        if(World.boards.hasNext()){
            World.boards = World.boards.next();
            if(World.boards.hasNext()){
                end = false;
            }else{
                end = backup;
            }
        }else{
            end = World.agent.step();
            backup = end;
            World.boards = World.boards.add(World.env.board);
        }
        if(end){
            next.setDisable(true);
        }
        previous.setDisable(false);
        draw();
    }

    @FXML
    private void stepBack(){
        World.boards = World.boards.previous();
        if(!World.boards.hasPrevious()){
            previous.setDisable(true);

        }
        next.setDisable(false);
        draw();
    }

    private void draw(){
        //importing images:
        Image mouse = new Image("/RL/Images/filled/mouse.png");
        Image cheese = new Image("/RL/Images/filled/one_cheese.png");
        Image doubleCheese = new Image("/RL/Images/filled/double_cheese.png");
        Image poison = new Image("/RL/Images/filled/poison.png");
        Image end = new Image("/RL/Images/filled/cheese_end.png");
        Image empty = new Image("/RL/Images/filled/empty.png");

        for (int i = 0; i < World.gridHeight; i++) {
            for (int j = 0; j < World.gridWidth; j++) {
                ImageView image = new ImageView();
                image.setFitWidth(value);
                image.setFitHeight(value);
                switch (World.boards.board[i][j]){
                    case 0: image.setImage(empty);break;
                    case 1: image.setImage(cheese);break;
                    case 2: image.setImage(doubleCheese);break;
                    case 3: image.setImage(end);break;
                    case 4: image.setImage(poison);break;
                    case 5: image.setImage(mouse);break;
                }
                mouseGrid.add(image, j, i);
                GridPane.setConstraints(image, j, i);
            }
        }
    }
}
