package trench_rogue_game.monster;

import trench_rogue_game.utils.Roll;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterDTO {
    public double xPosition, yPosition;
    public final String name;
    public double carry;
    public final Flags flag;
    public int EXP;
    public final int INIT_LVL;
    public int CURR_LVL;
    public int AMR;
    public double INIT_HPT;
    public double CURR_HPT;
    public List<Roll> attackRolls;
    public final int MIN_LVLFound;
    public final int MAX_LVLFound;
    public boolean isAlive;
    public int attackType = 0;
    public int goldDrop = 0;

    public MonsterDTO(String name, double carry, Flags flag, int EXP, int INIT_LVL, int CURR_LVL, int AMR,
                      double INIT_HPT, double CURR_HPT, List<Roll> attackRolls, int MIN_LVLFound, int MAX_LVLFound,
                      boolean isAlive, double xPosition, double yPosition) {
        this.name = name;
        this.carry = carry;
        this.flag = flag;
        this.EXP = EXP;
        this.INIT_LVL = INIT_LVL;
        this.CURR_LVL = CURR_LVL;
        this.AMR = AMR;
        this.INIT_HPT = INIT_HPT;
        this.CURR_HPT = CURR_HPT;
        this.attackRolls = attackRolls;
        this.MIN_LVLFound = MIN_LVLFound;
        this.MAX_LVLFound = MAX_LVLFound;
        this.isAlive = isAlive;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    public int getLVL(int currLevel) {
        return (currLevel - 26) + INIT_LVL;
    }
    public int getHPT(int currLevel) {
        int HPT = 0;
        int LVL = getLVL(currLevel);
        for (int i = 0; i <= LVL; i++) {
            HPT += ThreadLocalRandom.current().nextInt(1, 9); //1d8
        }
        return HPT;
    }
    public int getDMG() {
        int total = 0;
        for (Roll roll : attackRolls) {
            total += roll.roll();
        }
        return total;
    }
    public double getXPosition() {return xPosition;}
    public double getYPosition() {return yPosition;}
    public void newPosition(double xPosition, double yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}