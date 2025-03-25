package org.example;

import java.io.FileNotFoundException;
import java.util.EmptyStackException;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class DrawingScene implements EventHandler<ActionEvent> {

  Scene scene; 

  BorderPane root;

  Button playButton, pauseButton, skipButton, backButton, returnButton;
  Button[] drawingButtons;

  ImageLoader imageLoader;
  ImageView iv, ivRotated;

  Label timeLabel;
  long timeLeft;

  long lastSecond;

  AnimationTimer animationTimer;

  int duration;


  public DrawingScene(Button returnButton) {
    this.returnButton = returnButton;
    //imageLoader = new ImageLoader();
    iv = new ImageView();
    iv.setPreserveRatio(true);

    ivRotated = new ImageView();
    ivRotated.setPreserveRatio(true);
    ivRotated.setRotate(90);
    /*
    iv.setFitHeight(Window.bounds.getHeight()/5 * 4);
    iv.setFitWidth(Window.bounds.getWidth());
    */

  }

  public Scene begin(int duration) {
    imageLoader = new ImageLoader();
    root = new BorderPane();

    this.duration = duration;

    createTimer();

    //root.setCenter(iv);

    buttons();

    timeLabel = new Label();
    timeLabel.setFont(Font.font(Window.LABEL_SIZE));
    timeLabel.setAlignment(Pos.CENTER);

    HBox buttonHolder = new HBox(15, backButton, pauseButton, playButton, skipButton);
    buttonHolder.setAlignment(Pos.CENTER);
    
    VBox controls = new VBox(timeLabel, buttonHolder);
    controls.setAlignment(Pos.CENTER);
    VBox.setMargin(timeLabel, new Insets(20));   
    VBox.setMargin(buttonHolder, new Insets(0, 0, 30, 0));
    controls.setMinHeight(Window.bounds.getHeight()/5);

    root.setBottom(controls);

    StackPane aboveRoot = new StackPane(root, returnButton);
    StackPane.setAlignment(returnButton, Pos.TOP_LEFT);

    nextImage();
    return new Scene (aboveRoot);
  }

  private void setImage() {
    try {
    imageViewSetup(imageLoader.getImage());
    }
    catch (Exception e) {}
  }

  private void createTimer() {

    animationTimer = new AnimationTimer () {

      private long lastSecond = 0;

      @Override
      public void handle(long now) {
        if(lastSecond == 0) {lastSecond = now;}

        if(now - lastSecond > 1000000000) {
          lastSecond = now;
          timeLeft--;
          if(timeLeft < 0) {
            setImage();
            timeLeft = duration;
          }
        }
        timeLabel.setText(convertTime(timeLeft));
      }
    
    };

  }

  private void buttons () {
    backButton = new Button("");
    pauseButton = new Button("");
    playButton = new Button("");
    skipButton = new Button("");

    drawingButtons = new Button[]{backButton, pauseButton, playButton, skipButton};

    for (Button button : drawingButtons) {
      button.setFont(Font.font("Webdings", Window.BUTTON_SIZE));
      button.setOnAction(this);
    }
  }

  private static String convertTime(long seconds) {
    String timeString;

    long minutes = seconds/60;
    seconds = seconds - (minutes * 60);

    String minuteString = (minutes < 10) ? "0" + minutes : "" + minutes;
    String secondString = (seconds < 10) ? "0" + seconds : "" + seconds;

    timeString =  minuteString + ":" + secondString;

    return timeString;
  }

  @Override
  public void handle(ActionEvent event) {

    if(event.getSource() == backButton) {
      try {
        imageViewSetup(imageLoader.previousImage());
        timeLeft = duration;
        animationTimer.start();
      }
      catch (EmptyStackException e) {
        System.out.println("caught");
      }
      catch (FileNotFoundException e) {}
    }

    if(event.getSource() == pauseButton) {
      animationTimer.stop();
    }

    if(event.getSource() == playButton) {
      animationTimer.start();
    }

    if(event.getSource() == skipButton) {
      nextImage();
    }
  }

  public void nextImage() {
    setImage();
    timeLeft = duration;
    animationTimer.start();
  }

  private void imageViewSetup(Image image) {
    iv.setImage(image);
    iv.fitWidthProperty().bind(root.widthProperty());
    iv.setFitHeight(Window.bounds.getHeight()/5 * 4);
    root.setCenter(iv);
    //System.out.println("h " + iv.getFitHeight() + " w " + iv.getFitWidth() + " x " + iv.getX() + " y " + iv.getY());
  }

  public void end() {
    animationTimer.stop();
  }

}
