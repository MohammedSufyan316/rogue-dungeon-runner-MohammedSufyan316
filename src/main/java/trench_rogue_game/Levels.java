package trench_rogue_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import trench_rogue_game.utils.Roll;

import java.util.*;
import java.util.function.BiConsumer;

public class Levels {
    final List<double[]> rooms = new ArrayList<>();
    final List<double[]> walls = new ArrayList<>();
    final Set<double[]> visitedRooms = new HashSet<>();
    private final Set<double[]> revealedCorridors = new HashSet<>();
    private final Set<double[]> revealedWalls = new HashSet<>();
    private final Set<double[]> revealedDoorways = new HashSet<>();
    boolean keyPickup = false;
    boolean amuletPickup = false;

    private final List<double[]> corridors = new ArrayList<>();
    private final List<double[]> overlapPoints = new ArrayList<>();
    private final Random rand = new Random();
    private double[] stairPosition;
    double[] previousStairPosition;
    private double[] keyPosition;
    private double[] amuletPosition;
    private double playerX, playerY;
    int foodCount = 5; // Number of food items
    double[][] foodPositions = new double[foodCount][2];
    boolean[] foodAvailable = new boolean[foodCount];
    boolean[] foodVisiblePositions = new boolean[foodCount];
    int goldCount = 3; // Number of gold piles to spawn
    double[][] goldCoords = new double[goldCount][2];
    int[] goldAmounts = new int[goldCount];
    boolean[] goldVisible = new boolean[goldCount];
    boolean[] goldAvailable = new boolean[goldCount];
    public int itemCount = new Roll(1,5).roll();
    public double[][] itemPositions = new double[itemCount][2];
    public String[] itemTypes = new String[itemCount];
    public boolean[] itemAvailable = new boolean[itemCount];


    public Levels(boolean lastdungeonLevel,boolean firstdungeonLevel) {
        generateRooms();
        generateWalls();
        connectRoomsWithCorridors();

        markWallCorridorOverlaps();
        placeStairInRoom(lastdungeonLevel);
        placeKeyInRoom();
        initializeItems(rooms, walls);
        initializeFood(rooms, walls);
        initializeGold(rooms, walls);
        placeAmuletInRoom(lastdungeonLevel);

    }

    public void updatePlayerPosition(double x, double y) {
        this.playerX = x;
        this.playerY = y;
    }

    public double[][] getGoldCoords() {
        return goldCoords;
    }

    private void placeKeyInRoom() {
        for (int attempt = 0; attempt < 100; attempt++) {
            double[] room = rooms.get(rand.nextInt(rooms.size()));
            double x = room[0] + rand.nextInt((int) (room[2] / 10)) * 10;
            double y = room[1] + rand.nextInt((int) (room[3] / 10)) * 10;

            if (!isOverlappingWall(x, y)) {
            keyPosition = new double[] { x, y };
            return;
        }
            
        }
        keyPosition = new double[] { rooms.get(0)[0], rooms.get(0)[1] }; // fallback
    }

    private void placeAmuletInRoom(boolean lastdungeonLevel) {

//        if(!lastdungeonLevel) {
//            return;
//        }
        for (int attempt = 0; attempt < 100; attempt++) {
            double[] room = rooms.get(rand.nextInt(rooms.size()));
            double x = room[0] + rand.nextInt((int) (room[2] / 10)) * 10;
            double y = room[1] + rand.nextInt((int) (room[3] / 10)) * 10;

            if (!isOverlappingWall(x, y)) {
            amuletPosition = new double[] { x, y };
            return;
        }
          
        }
    }

    public void drawKey(GraphicsContext gc) {
        if (keyPosition != null && !keyPickup && isKeyOverlappingRoom()) {
            gc.setFill(Color.YELLOW);
            gc.setFont(new Font(14));
            gc.fillText("🔑", keyPosition[0], keyPosition[1] + 10);

        }
    }

    public void drawAmulet(GraphicsContext gc) {
        if(amuletPosition != null && !amuletPickup && isAmuletOverlappingRoom()) {
            gc.setFont(new Font("Arial", 14));
            gc.fillText("🧿", amuletPosition[0], amuletPosition[1] + 10);
        }
    }

        public void drawFood(GraphicsContext gc) {
        for (int i = 0; i < foodPositions.length; i++) {
            foodVisiblePositions[i] = isPositionInsideRooms(visitedRooms, foodPositions[i][0], foodPositions[i][1]);
        }

        gc.setFill(Color.ORANGE);

        for (int i = 0; i < foodCount; i++) {
            if (foodAvailable[i] && foodVisiblePositions[i]) {
                //gc.fillText("A", foodPositions[i][0], foodPositions[i][1]);
                gc.setFont(new Font(14));
                gc.fillText("🍞", foodPositions[i][0], foodPositions[i][1] + 10);

            }
        }
    }

        public void drawGold(GraphicsContext gc) {
        gc.setFill(Color.GOLD);
        gc.setFont(new Font("Arial", 14));

        for (int i = 0; i < goldCount; i++) {
            goldVisible[i] = isPositionInsideRooms(visitedRooms,  goldCoords[i][0], goldCoords[i][1]);

            if (goldAvailable[i] && goldVisible[i]) {
                gc.fillText("💰", goldCoords[i][0], goldCoords[i][1] + 10);
            }
        }
    }

    public void drawItems(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);
        gc.setFont(Font.font("Segoe UI Emoji", 12));
        for (int i = 0; i < itemCount; i++) {
            if (itemAvailable[i] && isPositionInsideRooms(visitedRooms, itemPositions[i][0], itemPositions[i][1])) {
                String emoji = switch (itemTypes[i]) {
                    case "Sword" -> "🗡";

                    case "Armor" -> "🛡"; 
                    default -> "❓"; 
                };
                gc.fillText(emoji, itemPositions[i][0], itemPositions[i][1] + 10);
            }
        }
    }

    public boolean isOnKey(double x, double y) {
        // keyPickup = true;
        return keyPosition != null && x == keyPosition[0] && y == keyPosition[1];
    }

    public boolean isOnAmulet(double x, double y, int level) {
        if(level < 26){
            return false;
        }
        return amuletPosition != null && x == amuletPosition[0] && y == amuletPosition[1];
    }

    private void generateRooms() {
        int roomCount = rand.nextInt(5) + 6; // 6 to 10 rooms
        int canvasWidth = 1000, canvasHeight = 750, margin = 50;
        int minSize = 60, maxSize = 180;

        while (rooms.size() < roomCount) {
            int w = rand.nextInt(maxSize - minSize + 1) + minSize;
            int h = rand.nextInt(maxSize - minSize + 1) + minSize;
            int x = rand.nextInt((canvasWidth - margin * 2 - w) / 10) * 10 + margin;
            int y = rand.nextInt((canvasHeight - margin * 2 - h) / 10) * 10 + margin;

            double[] newRoom = { x, y, w, h };
            if (!isOverlapping(newRoom)) {
                rooms.add(newRoom);
            }
        }

    }

    private boolean isOverlappingWall(double x, double y) {
    for (double[] wall : walls) {
        if (Math.abs(x - wall[0]) < 10 && Math.abs(y - wall[1]) < 10) {
            return true;
        }
    }
    return false;
}

   private boolean isOverlapping(double[] newRoom) {
    int buffer = 20; // minimum spacing in pixels
    for (double[] room : rooms) {
        if (newRoom[0] < room[0] + room[2] + buffer &&
                newRoom[0] + newRoom[2] + buffer > room[0] &&
                newRoom[1] < room[1] + room[3] + buffer &&
                newRoom[1] + newRoom[3] + buffer > room[1]) {
            return true;
        }
    }
    return false;
}

    private boolean isOverlappingCorridor(double x, double y) {
        for (double[] corridor : corridors) {
            if (x >= corridor[0] && x < corridor[0] + corridor[2] &&
                    y >= corridor[1] && y < corridor[1] + corridor[3]) {
                return true;
            }
        }
        return false;
    }

    private void generateWalls() {
        for (double[] room : rooms) {
            double x = room[0], y = room[1], w = room[2], h = room[3];

            // Top wall
            for (double i = x; i < x + w; i += 10) {
                if (!isOverlappingCorridor(i, y)) {
                    walls.add(new double[] { i, y, 10, 10 });
                }
            }

            // Bottom wall
            for (double i = x; i < x + w; i += 10) {
                if (!isOverlappingCorridor(i, y + h - 5)) {
                    walls.add(new double[] { i, y + h - 5, 10, 10 });
                }
            }

            // Left wall
            for (double i = y; i < y + h; i += 10) {
                if (!isOverlappingCorridor(x, i)) {
                    walls.add(new double[] { x, i, 10, 10 });
                }
            }

            // Right wall
            for (double i = y; i < y + h; i += 10) {
                if (!isOverlappingCorridor(x + w - 10, i)) {
                    walls.add(new double[] { x + w - 10, i, 10, 10 });
                }
            }
        }
    }

    public void placePreviousStairInRoom(boolean isFirstFloor,double x, double y) {
//        if(isFirstFloor) {
//            return;
//        }
        previousStairPosition = new double[]{x, y};
    }

    private void placeStairInRoom(boolean isLastFloor) {
        if (isLastFloor) {
            return;
        } // Last floor no stair!!!!
        double[] room = rooms.get(rand.nextInt(rooms.size()));
        double x, y;
        boolean validPosition = false;

        // Try up to 100 times to find a valid stair position not on a wall
        for (int i = 0; i < 100; i++) {
            x = room[0] + rand.nextInt((int) (room[2] / 10)) * 10;
            y = room[1] + rand.nextInt((int) (room[3] / 10)) * 10;

            if (!isOverlappingWall(x, y)) {
                stairPosition = new double[] { x, y };
                validPosition = true;
                break;
            }

        }

        // Fallback: if no valid position found, place in center of room
        if (!validPosition) {
            stairPosition = new double[] {
                    room[0] + room[2] / 2 - (room[0] + room[2] / 2) % 10,
                    room[1] + room[3] / 2 - (room[1] + room[3] / 2) % 10
            };
        }
    }

    public double[] generateValidRoomPosition(List<double[]> rooms, List<double[]> walls) {
    for (int attempt = 0; attempt < 100; attempt++) {
        double[] room = rooms.get(rand.nextInt(rooms.size()));
        double x = room[0] + rand.nextInt((int) (room[2] / 10)) * 10;
        double y = room[1] + rand.nextInt((int) (room[3] / 10)) * 10;

         if (!isOverlappingWall(x, y)) {
            return new double[]{x, y};
        }
    }

    return new double[]{rooms.get(0)[0], rooms.get(0)[1]}; // Fallback
}

       public void initializeItems(List<double[]> rooms, List<double[]> walls) {

    String[] weapons = { "Sword", "Armor", "Mace", "Longsword" };
    String[] items = { "Sword", "Armor" };
    for (int i = 0; i < itemCount; i++) {
        while (true) {
            double[] pos = generateValidRoomPosition(rooms, walls);
            if (!isOverlappingWall(pos[0], pos[1]) &&
                    isPositionInsideRooms(rooms, pos[0], pos[1])) {
                itemPositions[i][0] = pos[0];
                itemPositions[i][1] = pos[1];
                Random rand = new Random();
                int itemPick = rand.nextInt(items.length);
                itemTypes[i] = items[itemPick];
                itemAvailable[i] = true;
                break;
            }
        }
    }
}

   public void initializeFood(List<double[]> rooms, List<double[]> walls) {
    for (int i = 0; i < foodCount; i++) {
        while (true) {
            double[] pos = generateValidRoomPosition(rooms, walls);
            if (!isOverlappingWall(pos[0], pos[1]) &&
                    isPositionInsideRooms(rooms, pos[0], pos[1])) {
                foodPositions[i][0] = pos[0];
                foodPositions[i][1] = pos[1];
                foodAvailable[i] = true;
                break;
            }
        }

    }
}

    public void initializeGold(List<double[]> rooms, List<double[]> walls) {
    for (int i = 0; i < goldCount; i++) {
        double[] pos;
        boolean unique = false;
        while (!unique) {
            pos = generateValidRoomPosition(rooms, walls);
            unique = true;
            for (int j = 0; j < i; j++) {
                if (Math.abs(pos[0] - goldCoords[j][0]) < 10 &&
                    Math.abs(pos[1] - goldCoords[j][1]) < 10) {
                    unique = false;
                    break;
                }
            }
            if (unique && !isOverlappingWall(pos[0], pos[1]) &&
                isPositionInsideRooms(rooms, pos[0], pos[1])) {
                goldCoords[i][0] = pos[0];
                goldCoords[i][1] = pos[1];
                goldAmounts[i] = rand.nextInt(6) + 5;
                goldAvailable[i] = true;
            }

        }
    }
}

    private void connectRoomsWithCorridors() {
        Set<Integer> connected = new HashSet<>();
        List<Integer> unconnected = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++)
            unconnected.add(i);

        int current = unconnected.remove(0);
        connected.add(current);

        while (!unconnected.isEmpty()) {
            int nearest = -1;
            double minDist = Double.MAX_VALUE;
            for (int i : unconnected) {
                double dist = distance(rooms.get(current), rooms.get(i));
                if (dist < minDist) {
                    minDist = dist;
                    nearest = i;
                }
            }
            if (nearest != -1) {
                connectRooms(rooms.get(current), rooms.get(nearest));
                connected.add(nearest);
                unconnected.remove(Integer.valueOf(nearest));
                current = nearest;
            }
        }

        // Add 1–3 extra corridors
        int extras = rand.nextInt(3) + 1;
        for (int i = 0; i < extras; i++) {
            int a = rand.nextInt(rooms.size());
            int b = rand.nextInt(rooms.size());
            if (a != b) {
                connectRooms(rooms.get(a), rooms.get(b));
            }
        }
    }

    private double distance(double[] a, double[] b) {
        double ax = a[0] + a[2] / 2, ay = a[1] + a[3] / 2;
        double bx = b[0] + b[2] / 2, by = b[1] + b[3] / 2;
        return Math.hypot(ax - bx, ay - by);
    }

    private void connectRooms(double[] a, double[] b) {
        double ax = a[0] + a[2] / 2, ay = a[1] + a[3] / 2;
        double bx = b[0] + b[2] / 2, by = b[1] + b[3] / 2;

        ax = Math.floor(ax / 10) * 10;
        ay = Math.floor(ay / 10) * 10;
        bx = Math.floor(bx / 10) * 10;
        by = Math.floor(by / 10) * 10;

        if (rand.nextBoolean()) {
            // Horizontal then vertical
            double hx = Math.min(ax, bx);
            double hy = ay;
            double hw = Math.abs(ax - bx);
            double vx = bx;
            double vy = Math.min(ay, by);
            double vh = Math.abs(ay - by);

            corridors.add(new double[] { hx, hy, hw, 10 });
            corridors.add(new double[] { vx, vy, 10, vh });

            // Add overlap points where corridors touch rooms
            // if (isTouchingRoom(hx, hy)) overlapPoints.add(new double[]{hx, hy});
            // if (isTouchingRoom(vx, vy)) overlapPoints.add(new double[]{vx, vy});
        } else {
            // Vertical then horizontal
            double vx = ax;
            double vy = Math.min(ay, by);
            double vh = Math.abs(ay - by);
            double hx = Math.min(ax, bx);
            double hy = by;
            double hw = Math.abs(ax - bx);

            corridors.add(new double[] { vx, vy, 10, vh });
            corridors.add(new double[] { hx, hy, hw, 10 });

            // Add overlap points where corridors touch rooms
            // if (isTouchingRoom(vx, vy)) overlapPoints.add(new double[]{vx, vy});
            // if (isTouchingRoom(hx, hy)) overlapPoints.add(new double[]{hx, hy});
        }
    }

    public void drawRooms(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        for (double[] room : rooms) {
            if (isInsideRoom(playerX, playerY, room)) {
                visitedRooms.add(room);

            }
            gc.fillRoundRect(room[0], room[1], room[2], room[3], 8, 8);
        }

        for (double[] oP : overlapPoints) {
            if (isTileOverlappingVisitedRoom(oP)) {
                revealedDoorways.add(oP);
            }
        }

    }

    public void drawWalls(GraphicsContext gc) {
        Color wallColor = Color.DARKSLATEGRAY;
        gc.setFill(wallColor);
        for (double[] room : rooms) {
            if (isInsideRoom(playerX, playerY, room)) {
                for (double[] wall : walls) {
                    if (isNearRoom(playerX, playerY, wall)) {
                        // revealedWalls.add(wall);

                    }
                    if (isWallOverlappingRoom(wall, room)) {
                        revealedWalls.add(wall);
                    }
                }
            }
        }


        for (double[] wall : revealedWalls) {
            gc.fillRect(wall[0], wall[1], wall[2], wall[3]);
        }
    }


    public void drawCorridors(GraphicsContext gc) {
        Color corridorColor = Color.web("#2a2a2a");
        gc.setFill(corridorColor);
        for (double[] room : rooms) {
            for (double[] corridor : corridors) {

                // if(!isCorridorOverlappingRoom(corridor,room) &&
                // isInCorridor(playerX,playerY,10,10)){
                // revealedCorridors.add(corridor);
                // }
                if (isNear(corridor, playerX, playerY, 0.1)) {
                    // gc.fillRect(corridor[0], corridor[1], corridor[2], corridor[3]);
                    revealedCorridors.add(corridor);
                }
            }
        }

        for (double[] corridor : revealedCorridors) {
            gc.fillRoundRect(corridor[0], corridor[1], corridor[2], corridor[3], 6, 6);
        }

    }

    private boolean isNear(double[] rect, double x, double y, double radius) {
        return x + radius >= rect[0] && x - radius <= rect[0] + rect[2] &&
                y + radius >= rect[1] && y - radius <= rect[1] + rect[3];
    }


    public void drawOverlapPoints(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGRAY);
        for (double[] point : revealedDoorways) {
            gc.fillRect(point[0], point[1], 10, 10);
        }
    }

    public void drawStair(GraphicsContext gc) {
        if (stairPosition != null && isStairOverlappingRoom()) {
            gc.setFill(Color.SILVER);
            gc.setFont(new Font(14));
            gc.fillText("🏰", stairPosition[0], stairPosition[1] + 10);
        }

    }

    public void drawExitStair(GraphicsContext gc){
        if(previousStairPosition != null){
            gc.setFill(Color.GOLD);
            gc.setFont(new Font(14));
            gc.fillText("🏰", previousStairPosition[0], previousStairPosition[1] + 10);
        }
    }

    public boolean isInsideRoom(double x, double y, double[] room) {
        return x >= room[0] && x <= room[0] + room[2] &&
                y >= room[1] && y <= room[1] + room[3];
    }

    private boolean isNearRoom(double x, double y, double[] wall) {
        return Math.abs(x - wall[0]) <= 100 && Math.abs(y - wall[1]) <= 100;
    }

    public boolean isWallOverlappingRoom(double[] wall, double[] room) {
        double wx = wall[0], wy = wall[1], ww = wall[2], wh = wall[3];
        double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
        // for (double[] room : rooms) {
        // double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
        // boolean overlaps = wx < rx + rw && wx + ww > rx &&
        // wy < ry + rh && wy + wh > ry;
        // if (overlaps) {
        // return true;
        // }
        // }
        return wx < rx + rw && wx + ww > rx &&
                wy < ry + rh && wy + wh > ry;
    }

    public boolean isCorridorOverlappingRoom(double[] corridor, double[] room) {
        double wx = corridor[0], wy = corridor[1], ww = corridor[2], wh = corridor[3];
        double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
        return wx < rx + rw && wx + ww > rx &&
                wy < ry + rh && wy + wh > ry;
    }

    public boolean isAmuletOverlappingRoom() {
        return amuletPosition != null && isPositionInsideRooms(visitedRooms,amuletPosition[0],amuletPosition[1]);
    }

    public boolean isKeyOverlappingRoom() {
        return keyPosition != null && isPositionInsideRooms(visitedRooms, keyPosition[0], keyPosition[1]);
    }

    public boolean isStairOverlappingRoom() {
        return stairPosition != null && isPositionInsideRooms(visitedRooms, stairPosition[0], stairPosition[1]);
    }


    public boolean isTileOverlappingVisitedRoom(double[] tile) {
    return isPositionInsideRooms(visitedRooms, tile[0], tile[1]);
}


    public boolean checkCollision(double x, double y, double width, double height) {
        for (double[] wall : walls) {
            if (x < wall[0] + wall[2] && x + width > wall[0] &&
                    y < wall[1] + wall[3] && y + height > wall[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCorridor(double x, double y, double width, double height) {
        for (double[] corridor : corridors) {
            if (x < corridor[0] + corridor[2] && x + width > corridor[0] &&
                    y < corridor[1] + corridor[3] && y + height > corridor[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean isInRoom(double x, double y, double width, double height) {
        for (double[] room : rooms) {
            if (x < room[0] + room[2] && x + width > room[0] &&
                    y < room[1] + room[3] && y + height > room[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean isAtCorridorEntrance(double x, double y, double width, double height) {
        for (double[] corridor : corridors) {
            if ((x == corridor[0] || x + width == corridor[0] + corridor[2]) &&
                    (y >= corridor[1] && y <= corridor[1] + corridor[3])) {
                return true;
            }
            if ((y == corridor[1] || y + height == corridor[1] + corridor[3]) &&
                    (x >= corridor[0] && x <= corridor[0] + corridor[2])) {
                return true;
            }
        }
        return false;
    }

    public double[] getCorridorBounds(double x, double y) {
        for (double[] corridor : corridors) {
            if (x >= corridor[0] && x <= corridor[0] + corridor[2] &&
                    y >= corridor[1] && y <= corridor[1] + corridor[3]) {
                return corridor;
            }
        }
        return null;
    }

    private boolean isTouchingRoom(double x, double y) {
        for (double[] room : rooms) {
            double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
            if ((x >= rx - 10 && x <= rx + rw && y >= ry && y <= ry + rh) || // Left edge
                    (x >= rx && x <= rx + rw + 10 && y >= ry && y <= ry + rh) || // Right edge
                    (y >= ry - 10 && y <= ry + rh && x >= rx && x <= rx + rw) || // Top edge
                    (y >= ry && y <= ry + rh + 10 && x >= rx && x <= rx + rw)) { // Bottom edge
                return true;
            }
        }
        return false;
    }

    public boolean isOnStair(double x, double y) {
        return stairPosition != null && x == stairPosition[0] && y == stairPosition[1];
    }

    public boolean isOnExitStair(double x, double y) {
        return previousStairPosition != null && x == previousStairPosition[0] && y == previousStairPosition[1];
    }

    private void markWallCorridorOverlaps() {
        for (double[] wall : walls) {
            for (double[] corridor : corridors) {
                double x1 = Math.max(wall[0], corridor[0]);
                double y1 = Math.max(wall[1], corridor[1]);
                double x2 = Math.min(wall[0] + wall[2], corridor[0] + corridor[2]);
                double y2 = Math.min(wall[1] + wall[3], corridor[1] + corridor[3]);

                if (x1 < x2 && y1 < y2) {
                    // Overlapping region exists
                    for (double x = x1; x < x2; x += 10) {
                        for (double y = y1; y < y2; y += 10) {
                            overlapPoints.add(new double[] { x, y });
                        }
                    }
                }
            }
        }
    }

    private void computeOverlapPoints() {
        Map<String, Integer> grid = new HashMap<>();

        // Helper to mark grid cells
        BiConsumer<double[], String> markCells = (shape, type) -> {
            double xStart = shape[0], yStart = shape[1], w = shape[2], h = shape[3];
            for (double x = xStart; x < xStart + w; x += 10) {
                for (double y = yStart; y < yStart + h; y += 10) {
                    String key = (int) x + "," + (int) y;
                    grid.put(key, grid.getOrDefault(key, 0) | type.hashCode());
                }
            }
        };

        for (double[] room : rooms)
            markCells.accept(room, "room");
        for (double[] wall : walls)
            markCells.accept(wall, "wall");
        for (double[] corridor : corridors)
            markCells.accept(corridor, "corridor");

        // Detect overlaps (cells with more than one type)
        for (Map.Entry<String, Integer> entry : grid.entrySet()) {
            int value = entry.getValue();
            int count = Integer.bitCount(value);
            if (count > 1) {
                String[] parts = entry.getKey().split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                overlapPoints.add(new double[] { x, y });
            }
        }
    }

    public double[] getKeyPosition() {
        return keyPosition;
    }

    public double[] getStairPosition() {
        return stairPosition;
    }

    public List<double[]> getRooms() {
        return rooms;
    }

    public List<double[]> getWalls() {
        return walls;
    }

    public double[] getCurrentRoom(double x, double y) {
        for (double[] room : rooms) {
            if (x >= room[0] && x < room[0] + room[2] &&
                    y >= room[1] && y < room[1] + room[3]) {
                return room;
            }
        }
        return null;
    }

    public boolean isPositionInsideRooms(Collection<double[]> roomList, double x, double y) {
        for (double[] room : roomList) {
            double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
            if (x >= rx && x < rx + rw && y >= ry && y < ry + rh) {
                return true;
            }
        }
        return false;
    }

    public double[] getPreviousStairPosition() {
    return previousStairPosition;
}

}