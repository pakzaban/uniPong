package com.pakzaban;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


public class Controller {
    public AnchorPane anchorPane;
    public Pane graphPane;
    public Label scoreField;
    public Button startButton;
    public Button stopButton;
    public Label messageField;
    public Label levelField;
    public Button nextLevelButton;
    public Label highScoreField;

    private final int radius = 20;
    private double vel;
    private double angle;
    double xVel;
    double yVel;
    private Circle c1;
    private Rectangle r;
    private int score;
    private AnimationTimer at;
    private int counter;
    private int paddleVel;
    private int level;
    private int highScore = 0;

    public void initialize() {
        graphPane.getChildren().clear();
        DropShadow shadow =  new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetX(3);
        c1 = new Circle(200, 300, radius, Color.MAGENTA);
        c1.setEffect(shadow);
        r = new Rectangle(150, 250, 5, 100);
        r.setFill(Color.DARKTURQUOISE);
        r.setEffect(shadow);
        graphPane.getChildren().addAll(c1, r);

        vel = 10;
        angle = randomAngle();
        xVel = vel * Math.cos(angle);
        yVel = vel * Math.sin(angle);
        paddleVel = 50;
        score = 0;
        counter = 0;
        level = 1;

        messageField.setVisible(false);
        startButton.setOpacity(1.0);
        stopButton.setOpacity(0.5);
        levelField.setText("Level " + level);
        nextLevelButton.setVisible(false);

        try {
            highScore = Integer.parseInt(readHighScore().split(" ")[3]);
            highScoreField.setText(readHighScore());
        }
        catch (Exception e) {
        }

    }

    public void moveBall() {
        at = new AnimationTimer() {
            public void handle(long l) {
                //horizontal wall collisions
                if (c1.getCenterX() + radius >= graphPane.getWidth()) {
                    xVel = -xVel;
                } else if (c1.getCenterX() - radius <= 0) {
                    xVel = -xVel;
                    score--;

                    //horizontal paddle collisions from right side
                } else if (c1.getCenterX() - radius <= r.getX() + r.getWidth()
                        && c1.getCenterY() + radius >= r.getY()
                        && c1.getCenterY() - radius <= r.getY() + r.getHeight()
                        && xVel <0) {
                            score++;
                            counter++;
                            xVel = -xVel;
                }
                c1.setCenterX(c1.getCenterX() + xVel);

                //Vertical wall collisions
                if (c1.getCenterY()+ radius >= graphPane.getHeight()) {
                    yVel = -yVel;
                } else if (c1.getCenterY() - radius <= 0) {
                    yVel = -yVel;
                }
                c1.setCenterY(c1.getCenterY() + yVel);

                scoreField.setText("Score: " + String.valueOf(score));
                if (counter == 2){
                    counter = 0;
                    xVel++;
                    yVel++;
                    paddleVel +=3;
                    System.out.println(paddleVel+ ", " + xVel+ ", " +yVel);
                }
                if(stopButton.isPressed()){
                    stopPressed();
                    messageField.setText("Game Over");
                }
                if (score == 5 * level){
                    stopPressed();
                    messageField.setText("YOU WON!");
                    nextLevelButton.setVisible(true);
                    nextLevelButton.requestFocus();
                }
                if (score == -5){
                    stopPressed();
                    messageField.setText("YOU LOST!");
                }
            }
        };
        at.start();

    }
    public void keyUsed(javafx.scene.input.KeyEvent ke) {

        if (ke.getText().equals("h") && r.getY() - paddleVel >= 0) {
            r.setY(r.getY() - paddleVel);
        }
        if (ke.getText().equals("b") && r.getY() + r.getHeight() + paddleVel <= graphPane.getHeight()) {
            r.setY(r.getY() + paddleVel);
        }
    }
    public void startPressed(){
        initialize();
        moveBall();
        startButton.setOpacity(0.5);
        startButton.setDisable(true);
        stopButton.setOpacity(1.0);
        stopButton.requestFocus();
    }
    public void stopPressed(){
        startButton.setOpacity(1.0);
        startButton.setDisable(false);
        stopButton.setOpacity(0.5);
        startButton.requestFocus();
        messageField.setVisible(true);
        at.stop();
    }

    public double randomAngle() {
        return  (Math.PI /2) * Math.random() - Math.PI/4;
    }
    public void nextLevelPressed(){
        level++;
        levelField.setText("Level "+ level);
        nextLevelButton.setVisible(false);
        messageField.setVisible(false);
        moveBall();
        startButton.setOpacity(0.5);
        startButton.setDisable(true);
        stopButton.setOpacity(1.0);
        stopButton.requestFocus();
    }

    public void quitPressed(){
        stopPressed();
        if(score > highScore){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("High Score");
            dialog.setHeaderText("Congrats! You set a new high score.");
            dialog.setContentText("Enter your name");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String name = result.get();
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String highScoreString = "High score of " + score + " was set by "+ name +" on "+ formatter.format(date);

                try (FileWriter fileWriter = new FileWriter("data.txt")) {
                    fileWriter.write(highScoreString);
                }
                catch (Exception e){

                }
            }
        }
        System.exit(1);
    }
    public String readHighScore(){
        try (FileReader fileReader = (new FileReader("data.txt"))) {
            BufferedReader reader = new BufferedReader(fileReader);
            return reader.readLine();
        }
        catch (Exception e){
        }
        return "";
    }
}
