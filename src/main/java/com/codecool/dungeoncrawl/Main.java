package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    int currentMapIndex;
    int canvasWidth = 23;
    ArrayList<GameMap> maps = new ArrayList<>();
    GameMap currentMap;
    Canvas canvas = new Canvas(
            canvasWidth * Tiles.TILE_WIDTH,
            canvasWidth * Tiles.TILE_WIDTH);
    BorderPane borderPane = new BorderPane();
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Label strengthLabel = new Label();
    Label nameLabel = new Label();
    Label expLabel = new Label();
    Button pickupButton = new Button("Pick up");
    Button nameButton = new Button("Set Name");
    TextField nameTextField = new TextField();
    GridPane ui = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createMaps();
        currentMap = maps.get(currentMapIndex);
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.setStyle("-fx-background-color:#472D3C");

        addPickupButton();
        addNameButtonAndTextField();
        createLabels();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void addNameButtonAndTextField() {
        nameTextField.setPrefWidth(40);
        ui.add(nameTextField, 0, 0);
        ui.add(nameButton, 1, 0);


//        nameButton.setDisable(true);
//        nameTextField.setOnMouseClicked(e -> {
//            nameButton.setDisable(false);
//        });

        nameButton.setOnAction(value -> {
            currentMap.getPlayer().setName(nameTextField.getText());
            refresh();
            nameTextField.setDisable(true);
            nameButton.setDisable(true);
        });
    }

    private void createLabels() {
        Label nameHeader = new Label("Name: ");
        changeLabelColor(nameHeader);
        ui.add(nameHeader, 0, 1);
        ui.add(nameLabel, 1, 1);

        Label expHeader = new Label("Experience: ");
        changeLabelColor(expHeader);
        ui.add(expHeader, 0, 2);
        ui.add(expLabel, 1, 2);

        Label healthHeader = new Label("Health: ");
        changeLabelColor(healthHeader);
        ui.add(healthHeader, 0, 3);
        ui.add(healthLabel, 1, 3);

        Label strengthHeader = new Label("Strength: ");
        changeLabelColor(strengthHeader);
        ui.add(strengthHeader, 0, 4);
        ui.add(strengthLabel, 1, 4);

        Label inventoryHeader = new Label("Inventory: ");
        changeLabelColor(inventoryHeader);
        ui.add(inventoryHeader, 0, 5);
        ui.add(inventoryLabel, 1, 5);
    }

    private void addPickupButton() {
        ui.add(pickupButton, 0, 6);
        pickupButton.setDisable(true);

        pickupButton.setOnAction(value -> {
            onButtonPressed();
        });

        pickupButton.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            onKeyPressed(ev);
            pickupButton.setDisable(true);
        });
    }

    private void onButtonPressed() {
        currentMap.getPlayer().addToInventory();
        pickupButton.setDisable(true);
        refresh();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                currentMap.getPlayer().move(0, -1);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case DOWN:
                currentMap.getPlayer().move(0, 1);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case LEFT:
                currentMap.getPlayer().move(-1, 0);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case RIGHT:
                currentMap.getPlayer().move(1, 0);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            default:
                break;
        }
    }

    private void enablePickUp() {
        if (currentMap.getPlayer().isOnItem()) {
            pickupButton.setDisable(false);
        }
    }

    private void createMaps() {
        String[] mapTxts = {"/map1.txt", "/map2.txt", "/map3.txt"};
        for (String mapTxt : mapTxts) {
            maps.add(MapLoader.loadMap(mapTxt));
        }
    }

    private void refresh() {
        checkIfGameOver();
        changeCurrentMap();
        Color background = Color.rgb(71, 45, 60);
        context.setFill(background);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < currentMap.getWidth(); x++) {
            for (int y = 0; y < currentMap.getHeight(); y++) {
                int centeredX = x - currentMap.getPlayer().getX() + canvasWidth / 2;
                int centeredY = y - currentMap.getPlayer().getY() + canvasWidth / 2;

                Cell cell = currentMap.getCell(x, y);

                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), centeredX, centeredY);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), centeredX, centeredY);
                } else {
                    Tiles.drawTile(context, cell, centeredX, centeredY);
                }
            }
        }
        changeLabels();
    }

    private void changeCurrentMap() {
        if (currentMap.getPlayer().isOnStairsUp()) {
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex++;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getStairsDown();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
        } else if (currentMap.getPlayer().isOnStairsDown()) {
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex--;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getStairsUp();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
        }
    }

    private void changeLabels() {
        healthLabel.setText("" + currentMap.getPlayer().getHealth() + " HP");
        changeHealthColor();
        inventoryLabel.setText("" + currentMap.getPlayer().getInventoryString());
        changeLabelColor(inventoryLabel);
        strengthLabel.setText("" + currentMap.getPlayer().getStrength() + " STR");
        changeLabelColor(strengthLabel);
        nameLabel.setText("" + currentMap.getPlayer().getName());
        changeLabelColor(nameLabel);
        expLabel.setText("" + currentMap.getPlayer().getExperience() + " XP");
        changeLabelColor(expLabel);
    }

    private void changeHealthColor() {
        if (currentMap.getPlayer().isPoisoned()) {
            healthLabel.setTextFill(Color.web("#ed3e24"));
        } else {
            healthLabel.setTextFill(Color.web("white"));
        }
    }

    private void changeLabelColor(Label label) {
        label.setTextFill(Color.web("white"));
    }

    private void checkIfGameOver() {
        if (currentMap.getPlayer().getHealth() <= 0) {
            endGame("You lose!");
        } else if (currentMap.getBoss() != null) {
            if (currentMap.getBoss().getHealth() <= 0) {
                endGame("You win!");
            }
        }
    }

    private void endGame(String message) {
        Label loseMessage = new Label(message);
        BorderPane endPane = new BorderPane(loseMessage);
        Scene endScene = new Scene(endPane, 300, 100);
        Stage endStage = new Stage();
        endStage.setScene(endScene);
        endStage.setTitle("Game ended");
        endStage.show();
    }

    private void moveALlMonsters() {
        for (Actor monster : currentMap.getMonsters()) {
            monster.move();
        }
    }
}
