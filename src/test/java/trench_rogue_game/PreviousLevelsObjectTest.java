package trench_rogue_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PreviousLevelsObjectTest {

    private PreviousLevelsObject previousLevelsObject;
    ArrayList<PreviousLevelsObject> previousLevelsObjects = new ArrayList<>();
    private Levels level;
    private final MonsterSpawnLogic monsterSpawnLogic = new MonsterSpawnLogic();
    private final ArrayList<String> monsterNames = new ArrayList<>();
    private int MonsterCount = 3;
    private int dungeonLevel = 1;
    @BeforeEach
    void setUp() {

        for(int i = 0; i < 26; i ++){
            level = new Levels(false,true); // This will generate rooms, walls, etc.
            level.initializeFood(level.rooms, level.walls); // Pass room and wall data to initializeFood
            level.initializeGold(level.rooms, level.walls); // Pass room and wall data to
            level.initializeItems(level.rooms, level.walls);
            level.placePreviousStairInRoom(false,40,40);

            monsterSpawnLogic.createMonsterPositions(level.getRooms(), level.getWalls(), monsterNames, 30, 40, MonsterCount, dungeonLevel);

            previousLevelsObjects.add(new PreviousLevelsObject(monsterSpawnLogic,level,dungeonLevel));
            dungeonLevel++;

            MonsterCount++;

        }

    }
    @Test
    void getLevelNumber() {
        int levelNumber = 0;
        for (PreviousLevelsObject previousLevelsObject : previousLevelsObjects) {
            if (previousLevelsObject.getLevelNumber() == 26) {
                levelNumber = previousLevelsObject.getLevelNumber();

            }
        }
        assertEquals(26, levelNumber);

        for (PreviousLevelsObject previousLevelsObject : previousLevelsObjects) {
            if (previousLevelsObject.getLevelNumber() == 13) {
                levelNumber = previousLevelsObject.getLevelNumber();

            }
        }
        assertEquals(13, levelNumber);


        for (PreviousLevelsObject previousLevelsObject : previousLevelsObjects) {
            if (previousLevelsObject.getLevelNumber() == 10) {
                levelNumber = previousLevelsObject.getLevelNumber();

            }
        }
        assertNotEquals(13, levelNumber);

    }

    @Test
    void getLevels() {

        assert previousLevelsObjects.get(1).getLevels() != null;
        //PreviousLevelsObject previousLevelsObject = previousLevelsObjects.get(0);
        assertNotEquals(null, previousLevelsObjects.get(1).getLevels());
        assertNotNull(previousLevelsObjects.get(15).getLevels());
    }

    @Test
    void roomCount() {
        int roomCount = previousLevelsObjects.get(1).getLevels().rooms.size();

        assertTrue(roomCount < 11 && roomCount > 5);

    }

    @Test
    void wallCount() {
        int wallCount = previousLevelsObjects.get(1).getLevels().walls.size();
        int roomCount = previousLevelsObjects.get(1).getLevels().rooms.size();
        assertTrue(roomCount<wallCount);
        assertFalse(roomCount > wallCount);

        //assertEquals(wallCount, roomCount * 4);


    }

    @Test
    void levelCount() {
        int levelCount = previousLevelsObjects.size();
        assertEquals(26, levelCount);



    }



    @Test
    void foodCount() {

        PreviousLevelsObject previousLevelsObject = previousLevelsObjects.get(4);
        double [][]foodPositions = previousLevelsObject.getLevels().foodPositions;
        assertNotEquals(null, foodPositions);

        int foodCount = previousLevelsObject.getLevels().foodCount;

        assertEquals(5, foodCount);

        previousLevelsObject.getLevels().foodCount--;
        assertEquals(4, previousLevelsObject.getLevels().foodCount);

        previousLevelsObject = previousLevelsObjects.get(8);
        foodPositions = previousLevelsObject.getLevels().foodPositions;
        assertNotEquals(null, foodPositions);

        foodCount = previousLevelsObject.getLevels().foodCount;

        assertEquals(5, foodCount);

        previousLevelsObject.getLevels().foodCount--;
        previousLevelsObject.getLevels().foodCount--;
        assertEquals(3, previousLevelsObject.getLevels().foodCount);

    }

    @Test
    void goldCount() {
        PreviousLevelsObject previousLevelsObject = previousLevelsObjects.get(4);
        int goldCount = previousLevelsObject.getLevels().goldCount;
        assertEquals(3, goldCount);
        previousLevelsObject.getLevels().goldCount--;
        assertEquals(2, previousLevelsObject.getLevels().goldCount);
        previousLevelsObject = previousLevelsObjects.get(8);
        goldCount = previousLevelsObject.getLevels().goldCount;
        assertNotEquals(null,previousLevelsObject.getLevels().goldCoords);
        previousLevelsObject.getLevels().goldCount--;
        previousLevelsObject.getLevels().goldCount--;
        assertEquals(1, previousLevelsObject.getLevels().goldCount);
    }

    @Test
    void getMonsterSpawnLogic() {
        PreviousLevelsObject previousLevelsObject = previousLevelsObjects.get(4);
        previousLevelsObject.getMonsterSpawnLogic();
        assertNotNull(previousLevelsObject.getMonsterSpawnLogic());
    }
}