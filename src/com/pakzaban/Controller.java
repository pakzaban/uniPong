package com.pakzaban;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class Controller {
    public AnchorPane anchorPane;
    public Pane graphPane;
    public Label scoreField;
    public Button startButton;
    public Button stopButton;
    public Label messageField;

    private final int radius = 20;
    private double vel;
    private double angle;
    double xVel;
    double yVel;
    private double tol;
    private Circle c1;
    private Rectangle r;
    private int score;
    private AnimationTimer at;
    private int counter;
    private int paddleVel = 50;

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
        score = 0;
        counter = 0;

        messageField.setVisible(false);
        startButton.setOpacity(1.0);
        stopButton.setOpacity(0.5);
    }

    public void moveBall() {

        at = new AnimationTimer() {

            public void handle(long l) {
                tol = Math.abs(xVel);
                //horizontal wall collisions
                if (Math.abs(c1.getCenterX() - (graphPane.getWidth() - radius)) < tol) {
                    xVel = -Math.abs(xVel);
                } else if (Math.abs(c1.getCenterX() - radius) < tol) {
                    xVel = Math.abs(xVel);
                    score--;

                    //horizontal paddle collisions
                } else if ((Math.abs(c1.getCenterX() - r.getX() - r.getWidth() - radius) < tol) && c1.getCenterY() >= r.getY() - radius && c1.getCenterY() <= r.getY() + r.getHeight() + radius) {
                    if (xVel<0){
                        score++;
                        counter++;
                    }
                    xVel = Math.abs(xVel);
                }
                c1.setCenterX(c1.getCenterX() + xVel);

                //Vertical wall collisions
                if (Math.abs(c1.getCenterY() - (graphPane.getHeight() - radius)) < tol) {
                    yVel = -Math.abs(yVel);
                } else if (Math.abs(c1.getCenterY() - radius) < tol) {
                    yVel = Math.abs(yVel);
                }
                c1.setCenterY(c1.getCenterY() + yVel);
                scoreField.setText("Score: " + String.valueOf(score));
                if (counter == 2){
                    counter = 0;
                    xVel++;
                    yVel++;
                    paddleVel +=3;
                    System.out.println(xVel+ ", " +yVel);
                }

                if(stopButton.isPressed()){
                    at.stop();
                    messageField.setText("Game Over");
                    messageField.setVisible(true);
                }
                if (score == 10){
                    at.stop();
                    messageField.setText("YOU WON!");
                    messageField.setVisible(true);
                    stopPressed();
                }
                if (score == -5){
                    at.stop();
                    messageField.setText("YOU LOST!");
                    messageField.setVisible(true);
                    stopPressed();
                }
            }
        };
        at.start();

    }

    public void keyUsed(javafx.scene.input.KeyEvent ke) {

        if (ke.getText().equals("p") && r.getY()>= paddleVel) {
            r.setY(r.getY() - paddleVel);
        }
        if (ke.getText().equals("l") && r.getY() + r.getHeight() <= graphPane.getHeight()-paddleVel) {
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
    }

    public double randomAngle() {
        return  (Math.PI /2) * Math.random() - Math.PI/4;
    }


}
