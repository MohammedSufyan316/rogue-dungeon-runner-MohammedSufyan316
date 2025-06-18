package com.example.team_trenches_rogue_game;

import org.junit.jupiter.api.Test;
import trench_rogue_game.MonsterSpawnLogic;
import trench_rogue_game.monster.Monster;
import trench_rogue_game.monster.MonsterDTO;
import trench_rogue_game.monster.MonsterLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class MonsterSpawnLogicTest {

    private MonsterSpawnLogic logic = new MonsterSpawnLogic();
    @Test
    void createMonsterPositions() {


        List<double[]> rooms = new ArrayList<>();
        rooms.add(new double[]{0, 0, 100, 100});
        List<double[]> walls = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        types.add("snake");

        logic.createMonsterPositions(rooms, walls, types, 50, 50, 5,1);
        assertEquals(5, logic.getMonsterClasses().size());

    }



    @Test
    void checkMonsterCollision() {
        List<double[]> rooms = new ArrayList<>();
        rooms.add(new double[]{0, 0, 100, 100});
        List<double[]> walls = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        types.add("snake");
        Random rand = new Random();
        MonsterLibrary monsterLibrary = MonsterLibrary.getInstance();
        Monster monster = monsterLibrary.getMonster(rand.nextInt(26));
        logic.createMonsterPositions(rooms, walls, types, 50, 50, 5,1);
        MonsterDTO monsterDTO = monster.makeMonsterDTO(40, 50);
        double hp = monsterDTO.getHPT(1);
        monsterDTO.INIT_HPT = hp;
        monsterDTO.CURR_HPT = hp;
        monsterDTO.goldDrop = rand.nextInt(21) + 5; // Drop 5–25 gold
        ArrayList<MonsterDTO> monsters = new ArrayList<>();
        monsters = logic.getMonsterClasses();

        logic.monsterPositions.add(new double[]{60, 60});
        boolean check = false;


        for(int i = 0; i < logic.monsterPositions.size(); i++){
            double[] array = logic.monsterPositions.get(i);
            double x = array[0];
            double y = array[1];
            if(logic.checkMonsterCollision(x,y)){
                //System.out.println("Collision Detected");
                check = true;
                break;
            }
        }

        if (check) {
            System.out.println("Collision Detected");
            assertTrue(check);
        }
        else{
            System.out.println("No Collision Detected");
            assertFalse(check);
        }



        //assertTrue(logic.checkMonsterCollision(x,y));





    }

}