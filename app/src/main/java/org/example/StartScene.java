package org.example;

import java.util.LinkedHashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartScene implements EventHandler<ActionEvent> {

  static final int TITLE_SIZE = 24;

  Button startButton;
  VBox root;

  ChoiceBox<String> options;

  LinkedHashMap<String,Integer> dropdownValues;

  HBox fileSelection;
  Button fileButton;

  public StartScene(Button button) {
    startButton = button;
    makeDictionary();
  }

  public Scene scene() {

    Label label = new Label("Life Drawing");
    label.setFont(Font.font(TITLE_SIZE));

    StackPane title = new StackPane(label);
    VBox.setMargin(title, new Insets(80));

    options = new ChoiceBox<String>();
    options.getItems().addAll("30 Seconds", "1 Minute", "2 Minutes", "5 Minutes", "10 Minutes", "20 Minutes", "30 Minutes", "60 Minutes");
    options.setPrefWidth(150);
    options.setMinHeight(30);

    fileSelection();
    
    StackPane optionContainer = new StackPane(options);

    StackPane buttonContainer = new StackPane(startButton);

    root = new VBox(30, title, fileSelection, optionContainer, buttonContainer);
    root.setAlignment(Pos.CENTER);

    

    return new Scene(root);
  }

  private void makeDictionary() {
    dropdownValues = new LinkedHashMap<>();

    dropdownValues.put("30 Seconds", 30);    
    dropdownValues.put("1 Minute", 60);  
    dropdownValues.put("2 Minutes", 120);  
    dropdownValues.put("5 Minutes", 300);  
    dropdownValues.put("10 Minutes", 600);  
    dropdownValues.put("20 Minutes", 1200);  
    dropdownValues.put("30 Minutes", 1800);  
    dropdownValues.put("60 Minutes", 3600);  
  }

  public int getDurationFromSelected() {
    String selected = options.getValue();
    return dropdownValues.get(selected);
  }

  public void fileSelection() {
    Label fileLabel = new Label("Image Folder:");
    fileButton = new Button();
    fileButton.setOnAction(this);
    fileButton.setMinWidth(50);

    fileLabel.setFont(Font.font(Window.LABEL_SIZE));
    fileButton.setFont(Font.font(Window.LABEL_SIZE));

    fileSelection = new HBox(15, fileLabel, fileButton);
    fileSelection.setAlignment(Pos.CENTER);
  }


  @Override
  public void handle(ActionEvent event) {
    if(event.getSource() == fileButton) {
      fileButton.setText(App.window.directoryChooserDialog());
    }
  }



}