package trench_rogue_game.monster;

import trench_rogue_game.utils.Roll;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Monster {
    public final String name;
    final double carry;
    final Flags flag;
    final int EXP;
    final int INIT_LVL;
    int CURR_LVL;
    final int AMR;
    final double INIT_HPT;
    double CURR_HPT;
    final List<Roll> attackRolls;
    public final int MIN_LVLFound;
    public final int MAX_LVLFound;
    boolean isAlive;

    public Monster(String name, double carry, Flags flag, int EXP, int INIT_LVL, int AMR,
                   int MIN_LVLFound, int MAX_LVLFound, List<Roll> attackRolls) {
        this.name = name;
        this.carry = carry;
        this.flag = flag;
        this.EXP = EXP;
        this.INIT_LVL = INIT_LVL;
        this.CURR_LVL = INIT_LVL;
        this.AMR = AMR;
        this.INIT_HPT = getHPT(INIT_LVL);
        this.CURR_HPT = INIT_HPT;
        this.attackRolls = attackRolls;
        this.MIN_LVLFound = MIN_LVLFound;
        this.MAX_LVLFound = MAX_LVLFound;
        this.isAlive = true;
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

    public MonsterDTO makeMonsterDTO(double xPosition, double yPosition) {
        return new MonsterDTO(this.name,
                this.carry,
                this.flag,
                this.EXP,
                this.INIT_LVL,
                this.CURR_LVL,
                this.AMR,
                this.INIT_HPT,
                this.CURR_HPT,
                this.attackRolls,
                this.MIN_LVLFound,
                this.MAX_LVLFound,
                this.isAlive,
                xPosition,
                yPosition
        );
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", carry=" + carry +
                ", flag=" + flag +
                ", EXP=" + EXP +
                ", INIT_LVL=" + INIT_LVL +
                ", CURR_LVL=" + CURR_LVL +
                ", AMR=" + AMR +
                ", INIT_HPT=" + INIT_HPT +
                ", CURR_HPT=" + CURR_HPT +
                ", attackRolls=" + attackRolls +
                ", MIN_LVLFound=" + MIN_LVLFound +
                ", MAX_LVLFound=" + MAX_LVLFound +
                '}';
    }
}