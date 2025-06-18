package com.example.team_trenches_rogue_game;

import org.junit.jupiter.api.Test;
import trench_rogue_game.MonsterBehaviour;
import trench_rogue_game.MonsterSpawnLogic;
import trench_rogue_game.monster.Flags;
import trench_rogue_game.monster.Monster;
import trench_rogue_game.monster.MonsterDTO;
import trench_rogue_game.utils.Roll;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MonsterBehaviourTest {


    private MonsterBehaviour monsterBehaviour = new MonsterBehaviour();

    private MonsterSpawnLogic mockSpawnLogic = new MonsterSpawnLogic();
    //private Levels mockLevel = mock(Levels.class);
    //private RogueCharacter mockRogueCharacter = mock(RogueCharacter.class);
    private Monster mockMonster = new Monster("Aquator", 0, Flags.M, 20, 5, 9,1, 26,
            List.of(new Roll(0,0)));



    @Test
    void monsterAndPlayerSameRoom() {



        double x = 50;
        double y = 30;
        MonsterDTO dto = mockMonster.makeMonsterDTO(x, y);
        double playerX = 50;
        double playerY = 50;
        double[] room = {0, 0, 100, 100};


        boolean result = monsterBehaviour.MonsterAndPlayerSameRoom(playerX, playerY, dto, room);
        assertTrue(result);

    }

    @Test
    void distance() {

        double playerX = 50;
        double playerY = 50;

        double x = 50;
        double y = 30;
        MonsterDTO dto = mockMonster.makeMonsterDTO(x, y);
        double[] result = monsterBehaviour.distance(playerX, playerY, dto);
        assertArrayEquals(new double[]{0.0, 20.0}, result, 0.01);

        playerX = 100;
        playerY = 60;

        result = monsterBehaviour.distance(playerX, playerY, dto);
        assertArrayEquals(new double[]{50.0, 30.0}, result, 0.01);

    }

    @Test
    void monsterNearPlayer() {


        double playerX = 50;
        double playerY = 50;

        double x = 50;
        double y = 30;
        MonsterDTO dto = mockMonster.makeMonsterDTO(x, y);
        boolean result = monsterBehaviour.monsterNearPlayer(playerX, playerY, dto);
        assertFalse(result);

        playerX = 50;
        playerY = 20;
        result = monsterBehaviour.monsterNearPlayer(playerX, playerY, dto);
        assertTrue(result);

        playerX = 40;
        playerY = 30;
        result = monsterBehaviour.monsterNearPlayer(playerX, playerY, dto);
        assertTrue(result);
    }

    @Test
    void monsterNearObject() {

        double[] stairPosition = {10, 10};
        double[] keyPosition = {20, 20};
        double[][] foodPositions = {{30, 30}, {40, 40}};
        String direction = "UP";
        double x = 50;
        double y = 30;
        MonsterDTO dto = mockMonster.makeMonsterDTO(x, y);


        boolean result = monsterBehaviour.monsterNearObject(stairPosition, keyPosition, foodPositions, dto, direction);
        assertFalse(result);

        stairPosition = new double[]{50, 40};
        direction = "DOWN";
        result = monsterBehaviour.monsterNearObject(stairPosition, keyPosition, foodPositions, dto, direction);
        assertTrue(result);



    }
}