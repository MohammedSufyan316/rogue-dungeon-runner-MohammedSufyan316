package trench_rogue_game;

public class PreviousLevelsObject {
    private final MonsterSpawnLogic monsterSpawnLogic;
    private final Levels levels;
    private final int levelNumber;

    public PreviousLevelsObject(MonsterSpawnLogic monsterSpawnLogic, Levels levels, int levelNumber) {
        this.monsterSpawnLogic = monsterSpawnLogic;
        this.levels = levels;
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public Levels getLevels() {
        return levels;
    }
    public MonsterSpawnLogic getMonsterSpawnLogic() {
        return monsterSpawnLogic;
    }
}
