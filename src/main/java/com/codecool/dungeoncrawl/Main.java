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

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
//    Canvas canvas = new Canvas(
//            map.getWidth() * Tiles.TILE_WIDTH,
//            map.getHeight() * Tiles.TILE_WIDTH);
    Canvas canvas = new Canvas((map.getPlayer().getX() + 10) * Tiles.TILE_WIDTH,
        (map.getPlayer().getY() + 10) * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Label strengthLabel = new Label();
    Button button = new Button("Pick up");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        addPickupButton();
        addNameButtonAndTextField();
        addLabels();

        BorderPane borderPane = new BorderPane();
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
        ui.add(nameTextField, 0, 1);
        ui.add(nameButton, 1, 1);


//        nameButton.setDisable(true);
//        nameTextField.setOnMouseClicked(e -> {
//            nameButton.setDisable(false);
//        });

        nameButton.setOnAction(value -> {
            map.getPlayer().setName(nameTextField.getText());
            refresh();
            nameTextField.setDisable(true);
            nameButton.setDisable(true);
        });
    }

    private void addLabels(){
        ui.add(new Label("Name: "), 0, 0);
        ui.add(nameLabel, 1, 0);

        ui.add(new Label("Health: "), 0, 2);
        ui.add(healthLabel, 1, 2);

        ui.add(new Label("Strength: "), 0, 3);
        ui.add(strengthLabel, 1, 3);

        ui.add(new Label("Inventory: "), 0, 4);
        ui.add(inventoryLabel, 1, 4);
    }

    private void addPickupButton() {
        ui.add(pickupButton, 0, 5);
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
        map.getPlayer().addToInventory();
        pickupButton.setDisable(true);
        refresh();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
                moveALlMonsters();
                enablePickUp();
                refresh();
                break;
            default:
                break;
        }
    }

    private void enablePickUp() {
        if (map.getPlayer().isOnItem()) {
            pickupButton.setDisable(false);
        }
    }

    private void refresh() {

        canvas.setWidth((map.getPlayer().getX() + 10) * Tiles.TILE_WIDTH);
        canvas.setHeight((map.getPlayer().getY() + 10) * Tiles.TILE_WIDTH);

        if (map.getPlayer().isOnStairs()){
            Player currentPlayer = map.getPlayer();
            map = MapLoader.loadMap("/map2.txt");
            Cell currentCell = map.getPlayer().getCell();
            map.setPlayer(currentPlayer);
            map.getPlayer().setCell(currentCell);
        }
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventoryLabel.setText("" + map.getPlayer().getInventory());
        strengthLabel.setText("" + map.getPlayer().getStrength());
        nameLabel.setText("" + map.getPlayer().getName());
    }

    private void moveALlMonsters() {
        for (Actor monster : map.getMonsters()) {
            monster.move();
        }
    }
}
