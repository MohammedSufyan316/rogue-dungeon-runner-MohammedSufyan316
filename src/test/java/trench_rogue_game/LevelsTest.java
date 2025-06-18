package trench_rogue_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelsTest {

    private Levels level;

    @BeforeEach
    void setUp() {
        level = new Levels(false,true); // This will generate rooms, walls, etc.
        level.initializeFood(level.rooms, level.walls); // Pass room and wall data to initializeFood
        level.initializeGold(level.rooms, level.walls); // Pass room and wall data to
        level.initializeItems(level.rooms, level.walls);
        level.placePreviousStairInRoom(false,40,40);
    }

    @Test
    void testIsOnKey() {
        double[] keyPos = level.getKeyPosition();
        if (keyPos != null) {
            assertTrue(level.isOnKey(keyPos[0], keyPos[1]));
        }
    }


    @Test
    void testIsOnWall() {
        double[] keyPos = level.getKeyPosition();
        if (keyPos != null) {
            assertTrue(level.isOnKey(keyPos[0], keyPos[1]));
        }
    }

    @Test
    void testIsOnStair() {
        double[] stairPos = level.getStairPosition();
        if (stairPos != null) {
            assertTrue(level.isOnStair(stairPos[0], stairPos[1]));
        }
    }

    @Test
    void testIsPositionInsideRoom() {

    }

    @Test
    void testCollisionDetection() {

        boolean found = false;
        for (double[] wall : level.getWalls()) {
            if (level.checkCollision(wall[0], wall[1], wall[2], wall[3])) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testRoomDetection() {
        for (double[] room : level.getRooms()) {
            assertTrue(level.isInRoom(room[0] + 5, room[1] + 5, 10, 10));
        }
    }
}
