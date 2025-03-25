package org.example;

import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Window implements EventHandler<ActionEvent> {

  //TODO: classroom mode
  //TODO: figure out how to export it

  Button startButton, returnButton;

  Stage stage;

  Scene scene;

  StartScene startScene;
  DrawingScene drawingScene;
  
  static Rectangle2D bounds;


  public static final double BUTTON_SIZE = 18;
  public static final double LABEL_SIZE = 18;

  public int duration = 60;
  
  public Window(Stage stage) throws IOException, FileNotFoundException {

    this.stage = stage;

    Screen screen = Screen.getPrimary();
    bounds = screen.getVisualBounds();

    setupButtons();

    startScene = new StartScene(startButton);

    stage.setScene(startScene.scene());

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());
    stage.show();
    
  }

  public String directoryChooserDialog() {
    DirectoryChooser dc = new DirectoryChooser();
    dc.setTitle("Select Image Folder");
    App.resourceDirectory = dc.showDialog(stage);

    return App.resourceDirectory.getName();
  }

  private void setupButtons() {
    startButton = new Button("Start");
    startButton.setFont(Font.font(BUTTON_SIZE));
    startButton.setOnAction(this);

    returnButton = new Button("X");
    returnButton.setFont(Font.font(BUTTON_SIZE));
    returnButton.setOnAction(this);
  }

  @Override
  public void handle(ActionEvent event) {
    if(event.getSource()==startButton) {
      duration = startScene.getDurationFromSelected();
      drawingScene = new DrawingScene(returnButton);
      stage.setScene(drawingScene.begin(duration));
    }

    if(event.getSource()==returnButton) {
      drawingScene.end();
      stage.setScene(startScene.scene());
    }
  }

}
