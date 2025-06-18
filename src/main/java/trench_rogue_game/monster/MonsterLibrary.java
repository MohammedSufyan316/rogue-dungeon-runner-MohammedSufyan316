package trench_rogue_game.monster;

import trench_rogue_game.utils.Roll;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterLibrary {
    private static MonsterLibrary instance;
    private final List<Monster> monsters = List.of(
            new Monster("Aquator", 0, Flags.M, 20, 5, 9,1, 26,
                    List.of(new Roll(0,0))),
            new Monster("Bat", 0, Flags.F, 1, 1, 8, 1, 6,
                    List.of(new Roll(1,2))),
            new Monster("Centaur", .15, Flags.NULL, 25, 4, 7, 7,7,
                    List.of(new Roll(1,6), new Roll(1,6))),
            new Monster("Dragon", 1, Flags.M, 6800, 10, 1,1,26,
                    List.of(new Roll(1,8), new Roll(1,8), new Roll(3,10))),
            new Monster("Emu",0,Flags.M,2,1,10,1,4,
                    List.of(new Roll(1,2))),
            new Monster("Venus Flytrap",0,Flags.M,80,8,3,1,26,
                    List.of(new Roll(5,20))),
            new Monster("Griffon", .20, Flags.MFR, 2000, 13, -2,1,26,
                    List.of(new Roll(4,3),new Roll(2,5),new Roll(4,3))),
            new Monster("Hobogblin",0, Flags.M, 15, 1, 10, 1,7,
                    List.of(new Roll(1,8))),
            new Monster("Ice Monster",0,Flags.M,15,1,10,6,6,
                    List.of(new Roll(1,2))),
            new Monster("Jabberwock",.7,Flags.NULL,4000,15,-4,1,26,
                    List.of(new Roll(2,12), new Roll(2,4))),
            new Monster("Kestral",0,Flags.MF,1,1,10,1,4,
                    List.of(new Roll(1,4))),
            new Monster("Leprechaun",1,Flags.G, 10,3,8,6,6,
                    List.of(new Roll(1,2))),
            new Monster("Medusa",.4, Flags.M, 200, 8,9,1,26,
                    List.of(new Roll(3,4),new Roll(3,4),new Roll(2,5))),
            new Monster("Nymph",1,Flags.NULL,37,3,2,1,26,
                    List.of(new Roll(0,0))),
            new Monster("Orc",.15,Flags.G,5,1,5,3,6,
                    List.of(new Roll(1,8))),
            new Monster("Phantom", 0, Flags.I,120,8,8,1,26,
                    List.of(new Roll(4,4))),
            new Monster("Quagga",.3,Flags.M,32,3,9,1,26,
                    List.of(new Roll(1,2),new Roll(1,2), new Roll(1,4))),
            new Monster("Rattlesnake",0,Flags.M,9,2,8,4,7,
                    List.of(new Roll(1,6))),
            new Monster("Snake",0,Flags.M, 1,2,3,1,6,
                    List.of(new Roll(1,3))),
            new Monster("Troll",.5,Flags.RM,120,7,13,1,26,
                    List.of(new Roll(1,8),new Roll(1,8),new Roll(2,6))),
            new Monster("Ur-vile",0,Flags.M,190,7,13,1,26,
                    List.of(new Roll(1,3),new Roll(1,3),
                            new Roll(1,3),new Roll(4,6))),
            new Monster("Vampire",.2,Flags.RM,350,8,10,1,26,
                    List.of(new Roll(1,10))),
            new Monster("Wraith",0,Flags.NULL,55,5,7,1,26,
                    List.of(new Roll(1,6))),
            new Monster("Xeroc",.3,Flags.NULL,100,7,4,1,26,
                    List.of(new Roll(3,4))),
            new Monster("Yeti",.3,Flags.NULL,50,4,5,1,26,
                    List.of(new Roll(1,6),new Roll(1,6))),
            new Monster("Zombie",0,Flags.M,6,2,3,5,5,
                    List.of(new Roll(1,4)))
    );
    public static MonsterLibrary getInstance() {
        if (instance == null) {
            instance = new MonsterLibrary();
        }
        return instance;
    }
    private MonsterLibrary()
    {
    }
    public Monster getRandomMonster(int currLevel) {
        int index = ThreadLocalRandom.current().nextInt(monsters.size());
        while ( monsters.get(index).MIN_LVLFound > currLevel || currLevel > monsters.get(index).MAX_LVLFound) {
            index = ThreadLocalRandom.current().nextInt(monsters.size());
        }
        return monsters.get(index);
    }
    public Monster getMonster(int index) {
        return monsters.get(index);
    }
}
