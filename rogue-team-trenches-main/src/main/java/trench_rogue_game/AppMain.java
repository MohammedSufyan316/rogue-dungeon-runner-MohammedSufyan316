package trench_rogue_game;

import trench_rogue_game.monster.MonsterDTO;
import trench_rogue_game.monster.MonsterLibrary;
import trench_rogue_game.player.PlayerStats;
import trench_rogue_game.items.Item;
import trench_rogue_game.items.ItemTypes;
import trench_rogue_game.utils.AppInfo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;


public class AppMain extends Application {
    private final RogueCharacter rogueCharacter = new RogueCharacter();
    //private ArrayList<MonsterClass> monsterClasses = new ArrayList<>();
    private final MonsterSpawnLogic monsterSpawnLogic = new MonsterSpawnLogic();
    private final ArrayList<String> monsterNames = new ArrayList<>();
    private final MonsterBehaviour monsterBehaviour = new MonsterBehaviour();
    private final ArrayList<PreviousLevelsObject> previousLevelsObjects = new ArrayList<>();
    private final ArrayList<Object[]> droppedItems = new ArrayList<>();
    private boolean win  = false;
    //boolean isLastFloor = false;
    private Levels level = new Levels(false,true);
    private final int MIN_STR = 3;
    private final int MAX_STR = 31;
    private double x, y;
    private int dungeonLevel = 1;
    private final double step = 10;
    private int MonsterCount = 3;
    private boolean hasKey = false;
    private boolean hasAmulet = false;
    private String direction = "";
    private static AppMain instance;

    private static final Text gameMessageText = new Text();
    private static final Text monsterStatsText = new Text();

    public static void showMessage(String msg) {
        gameMessageText.setText(msg);
        new Thread(() -> {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ignored) {
            }
            gameMessageText.setText("");
        }).start();
    }

    public static void showMonsterStats(String msg) {
        monsterStatsText.setText(msg);
    }

    public static void clearMonsterStats() {
        monsterStatsText.setText("");
    }

    private void renderAll(GraphicsContext gc, PlayerStats playerStats) {
    gc.clearRect(0, 0, 1000, 750);
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, 1000, 750);

    level.drawCorridors(gc);
    level.drawRooms(gc);
    level.drawWalls(gc);
    level.drawOverlapPoints(gc);
    level.drawStair(gc);
    level.drawExitStair(gc);
    level.drawKey(gc);
    level.drawGold(gc);
    level.drawItems(gc);
    if(dungeonLevel == 26){
        level.drawAmulet(gc);
    }
        //level.drawAmulet(gc);

    
    
  for (Object[] drop : droppedItems) {
        double dx = (double) drop[0];
        double dy = (double) drop[1];
        String name = ((Item) drop[2]).getName();  

        gc.setFill(Color.LIGHTBLUE);
        gc.setFont(Font.font("Segoe UI Emoji", 12));
          String emoji = switch (name) {
        case "Sword" -> "🗡";
        case "Armor" -> "🛡";
        default -> name; 
    };

    gc.fillText(emoji, dx, dy + 10);
    }

    monsterSpawnLogic.drawMonster(gc, level.visitedRooms);
    rogueCharacter.drawCharacter(gc, level);
    playerStats.drawStats(gc, 1000, 750);
}

    private void nextLevel() {

    }

    @Override
    public void start(Stage stage) {
        instance = this;
        Canvas canvas = new Canvas(1000, 750);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        MonsterLibrary monsterLibrary = MonsterLibrary.getInstance();

        for (int i = 0; i < 26; i++) {
            monsterNames.add(monsterLibrary.getMonster(i).name);
        }

//        monsterNames.add("Snake");
//        monsterNames.add("Bear");
//        monsterNames.add("Goblin");
//        monsterNames.add("Dragon");
//        monsterNames.add("Vampire");

        level.drawCorridors(gc);
        level.drawRooms(gc);
        level.drawWalls(gc); // Draw walls
        // Draw corridors
        level.drawOverlapPoints(gc); // Draw overlap points
        level.drawStair(gc); // Draw the purple stair tile
        level.drawExitStair(gc);
        level.drawKey(gc);
        //level.drawAmulet(gc);


        // Choose a random room to spawn in
        double[] spawnRoom = level.generateValidRoomPosition(level.rooms, level.walls);
        x = spawnRoom[0];
        y = spawnRoom[1];
        rogueCharacter.positionUpdate(x, y);
        level.initializeFood(level.rooms, level.walls); // Pass room and wall data to initializeFood
        level.initializeGold(level.rooms, level.walls); // Pass room and wall data to
        level.initializeItems(level.rooms, level.walls); // Pass room and wall data to initializeItems
        level.placePreviousStairInRoom(false,x,y);
        rogueCharacter.drawCharacter(gc, level);

        Text healthText = new Text();
        Text goldText = new Text();
        Text strengthText = new Text();
        Text expText = new Text();
        Text hungerText = new Text();
        Text armorText = new Text();
        Text foodText = new Text();
        foodText.setText("Food: 0");


        PlayerStats playerStats = new PlayerStats(healthText, goldText, strengthText, expText, foodText, hungerText,
                armorText);
        rogueCharacter.setPlayerStats(playerStats);

        // Initialize text content after stats object is ready
        healthText.setText("Health: " + playerStats.getCurrentHealth() + " / " + playerStats.getMaxHealth());
        playerStats.updateGoldText();
        strengthText.setText("Strength: " + playerStats.getStrength());
        expText.setText(playerStats.getExpDisplayText());
        hungerText.setText("Hunger: " + playerStats.getHunger());
        armorText.setText("Armor: " + playerStats.getArmor());


        gameMessageText.setFill(Color.LIGHTGREEN);
        gameMessageText.setFont(new Font("Monospaced", 18));
        gameMessageText.setX((canvas.getWidth() - 400) / 2); // Center-ish
        gameMessageText.setY(canvas.getHeight() - 30);

        monsterStatsText.setFill(Color.LIGHTSKYBLUE);
        monsterStatsText.setFont(new Font("Monospaced", 18));
        monsterStatsText.setX(700);
        monsterStatsText.setY(700);

        level.updatePlayerPosition(x, y);

        monsterSpawnLogic.createMonsterPositions(level.getRooms(), level.getWalls(), monsterNames, x, y, MonsterCount, dungeonLevel);

        level.drawCorridors(gc);
        level.drawRooms(gc);
        level.drawWalls(gc); // Draw walls
        // Draw corridors
        level.drawOverlapPoints(gc); // Draw overlap points
        level.drawStair(gc); // Draw the purple stair tile
        level.drawExitStair(gc);
        level.drawKey(gc);
        //level.drawAmulet(gc);
        monsterSpawnLogic.drawMonster(gc, level.visitedRooms);
        rogueCharacter.drawCharacter(gc, level);

        Pane root = new Pane(canvas, gameMessageText, monsterStatsText);
        Scene scene = new Scene(root, 1000, 750);
        scene.setOnKeyPressed(event -> {
            boolean moved = false;
            double newX = x;
            double newY = y;
            double oldX = x;
            double oldY = y;
            if (!rogueCharacter.isAlive()) {
                renderAll(gc, playerStats);
                return;
            }
            
            switch (event.getCode()) {
                case UP -> {
                    newY -= step;
                    direction = "UP";
                    moved = true;
                }
                case DOWN -> {
                    newY += step;
                    direction = "DOWN";
                    moved = true;
                }
                case LEFT -> {
                    newX -= step;
                    direction = "LEFT";
                    moved = true;
                }
                case RIGHT -> {
                    newX += step;
                    direction = "RIGHT";
                    moved = true;
                }
                case W -> { // Diagonal up-left
                    x -= step;
                    y -= step;
                    moved = true;
                }
                case E -> { // Diagonal up-right
                    x += step;
                    y -= step;
                    moved = true;
                }
                case Z -> { // Diagonal down-left
                    x -= step;
                    y += step;
                    moved = true;
                }
                case C -> { // Diagonal down-right
                    x += step;
                    y += step;
                    moved = true;
                }
                case H -> rogueCharacter.printHungerStatus();
                case V -> {
                    rogueCharacter.eatFood(); 
                    hungerText.setText("Hunger: " + playerStats.getHunger());
                    foodText.setText("Food: " + rogueCharacter.foodInventory);
                }
                //case Q -> rogueCharacter.
                case A -> {
                    int damage = playerStats.getStrength() + playerStats.getBonusDamage();
                    AppInfo result = monsterSpawnLogic.MonsterHealthDecrease(damage, x, y);
                    showMessage(result.msg);
                    playerStats.gainExp(result.data);
                    expText.setText(playerStats.getExpDisplayText());
                    //playerStats.drawStats(gc, canvas.getWidth(), canvas.getHeight());
                }
                case I -> {
                    AppMain.showMessage("Inventory: " + rogueCharacter.getInventory().getItems());
                }
                case U -> {
                    Item used = rogueCharacter.getInventory().useItem(); 
                    if (used != null) {
                        AppMain.showMessage("You used: " + used.getName()); 
                        playerStats.drawStats(gc, canvas.getWidth(), canvas.getHeight());
                    } else {
                        AppMain.showMessage("No items to use.");
                    }
                }
                case D -> {
                    Item dropped = rogueCharacter.getInventory().dropItem();
                    if (dropped != null) {
                          double dropX = x + (Math.random() < 0.5 ? -10 : 10);
                          double dropY = y + (Math.random() < 0.5 ? -10 : 10);
                          droppedItems.add(new Object[]{dropX, dropY, dropped});
                          AppMain.showMessage("You dropped: " + dropped);
                        renderAll(gc, playerStats);
                    } else {
                        AppMain.showMessage("No items to drop.");
                    }
                }

                case N -> {
                    moved = true;
                    nextLevel();
                }
            }


            if (moved) {
                level.updatePlayerPosition(x, y);
            }


            boolean inCorridor = level.isInCorridor(x, y, 10, 10);
            boolean atCorridorEntrance = level.isAtCorridorEntrance(newX, newY, 10, 10);
            boolean inRoom = level.isInRoom(newX, newY, 10, 10);
            //boolean monsterCollision = monsterSpawnLogic.checkMonsterCollision(newX,newY);

//            if(!monsterSpawnLogic.checkMonsterCollision(newX,newY)){
//                x = newX;
//                y = newY;
//                rogueCharacter.positionUpdate(x, y);
//                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//                gc.setFill(Color.BLACK);
//                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
//                level.drawRooms(gc);
//                level.drawWalls(gc);
//                level.drawCorridors(gc);
//                monsterSpawnLogic.drawMonster(gc);
//                rogueCharacter.drawCharacter(gc,level);
//            }


            if (atCorridorEntrance || inRoom || inCorridor) {
                if (!level.checkCollision(newX, newY, 10, 10)) {
                    x = newX;
                    y = newY;
                    rogueCharacter.positionUpdate(x, y);
                    renderAll(gc, playerStats);
                }
            }


            // Check if player is on food
            for (int i = 0; i < level.foodCount; i++) {
                if (level.foodAvailable[i] &&
                        Math.abs(x - level.foodPositions[i][0]) < step &&
                        Math.abs(y - level.foodPositions[i][1]) < step) {

                    // Adjust player position to visually align with food
                    x = level.foodPositions[i][0];
                    y = level.foodPositions[i][1];

                    rogueCharacter.foodInventory++;
                    level.foodAvailable[i] = false;
                    AppMain.showMessage("You picked up food! (Inventory: " + rogueCharacter.foodInventory + ")");
                    foodText.setText("Food: " + rogueCharacter.foodInventory);
                }
            }

            // Check if player is on gold dropped by monster
            for (int i = 0; i < monsterSpawnLogic.goldDrops.size(); i++) {
                double[] gold = monsterSpawnLogic.goldDrops.get(i);
                if (Math.abs(x - gold[0]) < step && Math.abs(y - gold[1]) < step) {
                    int amount = (int) gold[2];
                    playerStats.addGold(amount);
                    AppMain.showMessage("You picked up " + amount + " gold!");
                    monsterSpawnLogic.goldDrops.remove(i);
                    playerStats.updateGoldText();
                    break;

                }
            }

            // Check if player is on gold pile from room loot
            for (int i = 0; i < level.goldCount; i++) {
                if (level.goldAvailable[i] &&
                        Math.abs(x - level.goldCoords[i][0]) < step &&
                        Math.abs(y - level.goldCoords[i][1]) < step) {

                    int amount = level.goldAmounts[i];
                    playerStats.addGold(amount);
                    AppMain.showMessage("You picked up " + amount + " gold from the floor!");
                    level.goldAvailable[i] = false;
                    playerStats.updateGoldText();
                    break;
                }
            }

            // Check if player walks over an item
            for (int i = 0; i < droppedItems.size(); i++) {
                Object[] drop = droppedItems.get(i);
                double dx = (double) drop[0];
                double dy = (double) drop[1];
                Item item = (Item) drop[2];

                if (Math.abs(x - dx) < step && Math.abs(y - dy) < step) {
                    boolean success = rogueCharacter.getInventory().addItem(item);
                    if (success) {
                        AppMain.showMessage("You picked up: " + item.getName());
                        droppedItems.remove(i);
                    } else {
                        AppMain.showMessage("Inventory full! Can't pick up " + item);
                    }
                    break;
                }
            }

            // Check if player walks over a Sword or Armor placed on the map
            for (int i = 0; i < level.itemCount; i++) {
                if (level.itemAvailable[i] &&
                        Math.abs(x - level.itemPositions[i][0]) < step &&
                        Math.abs(y - level.itemPositions[i][1]) < step) {

                    String type = level.itemTypes[i]; // This line was missing in your original code
                    Item item = null;

                    if (type.equals("Sword")) {
                        item = new Item("Sword", ItemTypes.WEAPON, 3);
                    } else if (type.equals("Armor")) {
                        item = new Item("Armor", ItemTypes.ARMOR, 2);
                    }

                    if (item != null) {
                        boolean success = rogueCharacter.getInventory().addItem(item);

                        if (success) {
                            AppMain.showMessage("You picked up: " + item.getName());
                            level.itemAvailable[i] = false;
                        } else {
                            AppMain.showMessage("Inventory full! Can't pick up " + item.getName());
                        }
                    }
                    break;
                }
            }
//            boolean inCorridor = level.isInCorridor(x, y, 10, 10);
//            boolean atCorridorEntrance = level.isAtCorridorEntrance(newX, newY, 10, 10);
//            boolean inRoom = level.isInRoom(newX, newY, 10, 10);
            if (inCorridor) {
                double[] corridorBounds = level.getCorridorBounds(x, y);
                if (inRoom) {
                    if (!level.checkCollision(newX, newY, 10, 10)) {
                        x = newX;
                        y = newY;
                        rogueCharacter.positionUpdate(x, y);
                        renderAll(gc, playerStats);

                    }
                } else if (level.isInCorridor(newX, newY, 10, 10)) {
                    x = newX;
                    y = newY;
                    rogueCharacter.positionUpdate(x, y);
                   renderAll(gc, playerStats);

                } else if (level.checkCollision(newX, newY, 10, 10)) {
                    x = newX;
                    y = newY;
                    rogueCharacter.positionUpdate(x, y);
                   renderAll(gc, playerStats);
                    moved = false;
                }
            }
            if (atCorridorEntrance) {
                x = newX;
                y = newY;
                rogueCharacter.positionUpdate(x, y);
                renderAll(gc, playerStats);

            } else if (!level.checkCollision(newX, newY, 10, 10) && (inRoom || inCorridor)
            ) {
                x = newX;
                y = newY;
                rogueCharacter.positionUpdate(x, y);
                renderAll(gc, playerStats);
            }


            //This ensures that the player does not pass through monsters.
            if (monsterSpawnLogic.checkMonsterCollision(x, y)) {
                x = oldX;
                y = oldY;
                moved = false;
                rogueCharacter.positionUpdate(x, y);
               renderAll(gc, playerStats);
            }


            playerStats.drawStats(gc, canvas.getWidth(), canvas.getHeight()); // Draw stats at the bottom


            if (level.isOnKey(x, y) && !hasKey) {
                hasKey = true;
                AppMain.showMessage("You picked up The Key!");
                level.keyPickup = true;
                //level.keyDisappear(gc);
            }

            if(level.isOnAmulet(x,y,dungeonLevel) && !hasAmulet){
                hasAmulet = true;
                AppMain.showMessage("You picked up The Amulet!");
                level.amuletPickup = true;
            }


            if (level.isOnStair(x, y) && hasKey && !level.amuletPickup) {

                //Save levels for later visits

                previousLevelsObjects.add(new PreviousLevelsObject(monsterSpawnLogic,level,dungeonLevel));
                // Generate a new level

                if (dungeonLevel == 26) {
                    level = new Levels(true,false);
                } else {
                    level = new Levels(false,false);
                }

                droppedItems.clear();

                if(dungeonLevel < 10){
                    MonsterCount += 1;
                }

                monsterSpawnLogic.createMonsterPositions(level.getRooms(), level.getWalls(), monsterNames, x, y, MonsterCount, dungeonLevel);
                hasKey = false;
                level.initializeFood(level.rooms, level.walls);
                level.initializeGold(level.rooms, level.walls);
                level.initializeItems(level.rooms, level.walls);

              moved = false;

                // Choose a new spawn point
                double[] spawnRoom2 = level.rooms.get(new Random().nextInt(level.rooms.size()));
                x = spawnRoom2[0] + spawnRoom2[2] / 2;
                y = spawnRoom2[1] + spawnRoom2[3] / 2;
                x = Math.floor(x / 10) * 10;
                y = Math.floor(y / 10) * 10;
                rogueCharacter.positionUpdate(x, y);
                level.placePreviousStairInRoom(false,x,y);
                if (dungeonLevel < 26) {
                    dungeonLevel += 1;
                }
            }

            if(level.isOnExitStair(x, y) && hasAmulet){
                PreviousLevelsObject pLO = null;


                for(PreviousLevelsObject previousLevel: previousLevelsObjects){
                    if(dungeonLevel - 1 == previousLevel.getLevelNumber()){
                        pLO = previousLevel;
                        break;
                    }
                }
                dungeonLevel -= 1;
                if(pLO != null){
                    double[] stairs = pLO.getLevels().getStairPosition();
                    x = stairs[0];
                    y = stairs[1];
                    //monsterSpawnLogic = pLO.getMonsterSpawnLogic();
                    level = pLO.getLevels();
                    monsterSpawnLogic.createMonsterPositions(level.getRooms(), level.getWalls(), monsterNames, x, y, MonsterCount, dungeonLevel);
                    moved = false;
                }

                if(dungeonLevel == 0){
                    win = true;
                }

            }

            if(win){
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                rogueCharacter.win();

            }

            else{
                if (moved) {
                    playerStats.updateHunger(-1);
                    rogueCharacter.printHungerStatus();

                    level.updatePlayerPosition(x, y);

                    monsterBehaviour.monsterMovesToPlayer(monsterSpawnLogic, x, y, level, rogueCharacter);

                    renderAll(gc, playerStats);

                    monsterBehaviour.monsterAttackPlayer(monsterSpawnLogic, x, y, rogueCharacter, healthText, goldText);

                }

                // Show stats of monster in current room
                double[] currentRoom = level.getCurrentRoom(x, y);
                if (currentRoom != null) {
                    StringBuilder statsText = new StringBuilder();
                    boolean monsterFound = false;

                    for (MonsterDTO monster : monsterSpawnLogic.getMonsterClasses()) {
                        if (monster.isAlive &&
                                level.isInsideRoom(monster.getXPosition(), monster.getYPosition(), currentRoom)) {

                            statsText.append(monster.name).append(" | HP: ").append((int) monster.CURR_HPT).append("\n");
                         // " | DMG: " + monster.getAttackPower() + "\n";
                            monsterFound = true;
                        }
                    }

                    if (monsterFound) {
                            showMonsterStats(statsText.toString());
                        } else {
                            clearMonsterStats();
                        }
                    } else {
                        clearMonsterStats(); // Player not in a room
                    }
                }

                // Redraw everything
        });

        playerStats.drawStats(gc, canvas.getWidth(), canvas.getHeight());
        stage.setTitle("Rogue Character");
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();
    }

    public static double getPlayerX() {
        return instance != null ? instance.x : 0;
    }

    public static double getPlayerY() {
        return instance != null ? instance.y : 0;
    }

    public static void main(String[] args) {

        launch(args);
    }
}