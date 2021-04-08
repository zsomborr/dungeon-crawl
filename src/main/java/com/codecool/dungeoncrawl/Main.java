package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.sql.SQLException;
import java.util.List;

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
    GameDatabaseManager dbManager;
    MenuBar menuBar = new MenuBar();
    Menu menuLoad;
    Player newPlayer;
    GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        createMaps();
        currentMap = maps.get(currentMapIndex);
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.setStyle("-fx-background-color:#472D3C");

        addMenuBar();
        addPickupButton();
        addNameButtonAndTextField();
        createLabels();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void addMenuBar() {
        Menu menuSave = new Menu("Save");
        MenuItem menuItemSave = new MenuItem("Save Game");
        menuSave.getItems().add(menuItemSave);
        menuItemSave.setOnAction(t -> displaySave());

        menuLoad = new Menu("Load");
        addLoadChoices(menuLoad);

        menuBar.getMenus().addAll(menuSave, menuLoad);
    }

    private void addLoadChoices(Menu menuLoad) {
        menuLoad.getItems().clear();
        List<GameState> gameStates = dbManager.getAllGameStates();
        for (GameState gameState : gameStates) {
            MenuItem menuItem = new MenuItem(gameState.getSaveName());
            menuLoad.getItems().add(menuItem);
            menuItem.setOnAction(value -> {
                loadSavedGame(gameState);
            });
        }
    }
    
    private void loadSavedGame(GameState gameState) {
        int stateId = gameState.getId();
        int playerId = gameState.getPlayerId();
        loadMaps(stateId);
        loadPlayer(playerId);
        refresh();
    }
    
    private void loadMaps(int stateId) {
        maps.clear();
        List<MapModel> mapModels = dbManager.getAllMapsFromStateId(stateId);
        int mapIndex = 0;
        for (MapModel mapModel : mapModels) {
            String mapLayout = mapModel.getMapLayout();
            GameMap map = MapLoader.loadMap(mapLayout);
            maps.add(map);
            if (mapModel.isCurrentMap()) {
                currentMap = map;
                currentMapIndex = mapIndex;

            }
            mapIndex++;
        }
    }
    
    private void loadPlayer(int playerId) {
        PlayerModel newPlayerModel = dbManager.getPlayerFromId(playerId);
        List<String> inventory = newPlayerModel.getInventory();
        String name = newPlayerModel.getPlayerName();
        int x = newPlayerModel.getX();
        int y = newPlayerModel.getY();
        int hp = newPlayerModel.getHp();
        int experience = newPlayerModel.getExperience();
        int strength = newPlayerModel.getStrength();
        int poisonCount = newPlayerModel.getPoisonCount();
        newPlayer = new Player(currentMap.getCell(x, y));
        newPlayer.setName(name);
        newPlayer.setHealth(hp);
        newPlayer.setExperience(experience);
        newPlayer.setStrength(strength);
        newPlayer.setPoisonCount(poisonCount);
        loadInventory(inventory);
        currentMap.setPlayer(newPlayer);
    }

    private void loadInventory(List<String> inventory){
        List<Item> newInventory = new ArrayList<>();
        for (String itemName : inventory) {
            switch(itemName) {
                case "boat":
                    newInventory.add(new Boat(itemName));
                    break;
                case "key":
                    newInventory.add(new Key(itemName));
                    break;
                case "potion":
                    newInventory.add(new Potion(itemName));
                    break;
                case "sword":
                    newInventory.add(new Sword(itemName));
                    break;
            }
        }
        newPlayer.setInventory(newInventory);
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

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
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
            case S:
                displaySave();
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

    public void displaySave() {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Save game");

        Label label1 = new Label("Provide a name for your save");
        TextField nameField = new TextField();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        nameField.setPrefWidth(10);

        saveButton.setOnAction(value -> {
            String saveName = nameField.getText();
            saveOrUpdateGame(saveName);
            popupWindow.close();
            addLoadChoices(menuLoad);
        });

        cancelButton.setOnAction(e -> popupWindow.close());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1, nameField, saveButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        popupWindow.setScene(scene1);

        popupWindow.showAndWait();
    }

    private void saveOrUpdateGame(String saveName) {
        newPlayer = currentMap.getPlayer();
        this.gameState = dbManager.checkIfSaveNameExists(saveName);
        if (this.gameState != null) {
            displayConfirmSave();
        } else {
            dbManager.savePlayer(newPlayer);
            dbManager.saveGameState(saveName);
            dbManager.saveMaps(maps, currentMap);
        }
    }

    private void UpdateGame(){
        int oldPlayerId = this.gameState.getPlayerId();
        int oldGameStateId = this.gameState.getId();
        dbManager.updatePlayer(newPlayer, oldPlayerId);
        dbManager.updateGameState(oldGameStateId);
        dbManager.updateMaps(maps, currentMap, oldGameStateId);
    }

    private void displayConfirmSave() {
        Stage confirmWindow = new Stage();

        confirmWindow.initModality(Modality.APPLICATION_MODAL);
        confirmWindow.setTitle("Confirmation");

        Label label1 = new Label("This name is already used, \n do You want to override it?");
        Button yesButton = new Button("Yes");
        Button cancelButton = new Button("Cancel");

        yesButton.setOnAction(value -> {
            UpdateGame();
            confirmWindow.close();
        });

        cancelButton.setOnAction(e -> confirmWindow.close());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1, yesButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        confirmWindow.setScene(scene1);

        confirmWindow.showAndWait();
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

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
