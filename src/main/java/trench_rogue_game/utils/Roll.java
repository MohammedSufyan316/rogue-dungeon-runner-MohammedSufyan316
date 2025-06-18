package trench_rogue_game.utils;

import java.util.concurrent.ThreadLocalRandom;

public class Roll {
    private final int numDice;
    private final int diceSides;

    public Roll(int numDice, int diceSides) {
        this.numDice = numDice;
        this.diceSides = diceSides;
    }
    public int roll() {
        int total = 0;
        for (int i = 0; i < numDice; i++) {
            total += ThreadLocalRandom.current().nextInt(1, diceSides + 1);
        }
        return total;
    }

    @Override
    public String toString() {
        return numDice + "d" + diceSides;
    }
}
