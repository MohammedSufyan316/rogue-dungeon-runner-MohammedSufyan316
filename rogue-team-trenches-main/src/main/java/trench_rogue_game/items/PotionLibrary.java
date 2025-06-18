package trench_rogue_game.items;

import javafx.scene.paint.Color;
import trench_rogue_game.monster.MonsterLibrary;

import java.util.*;

public class PotionLibrary {
    private static PotionLibrary instance;
    final ItemTypes type = ItemTypes.POTION;
    final private List<Color> colors = new ArrayList<>(List.of(
            Color.BLACK, Color.BLUE, Color.BROWN, Color.TRANSPARENT, Color.CRIMSON, Color.CYAN, Color.THISTLE,
            Color.GOLD, Color.GREEN, Color.GREY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.PLUM, Color.PURPLE,
            Color.RED, Color.SILVER, Color.TAN, Color.WHEAT, Color.CORAL, Color.TURQUOISE, Color.LIME, Color.VIOLET,
            Color.WHITE, Color.YELLOW, Color.AQUAMARINE, Color.YELLOWGREEN
    ));

    final public List<String> names = new ArrayList<>(List.of(
            "Blindness", "Confusion", "Extra Healing", "Gain Strength", "Haste Self", "Healing",
            "Magic Detection", "Monster Detection", "Paralysis", "Poison", "Raise Level",
            "Restore Strength", "See Invisible", "Thirst Quenching", "Other Potion"
    ));
    private Map<String, Boolean> discovered = new HashMap<>();

    final Map<String, Color> potions = new HashMap<>();
    public static PotionLibrary getInstance() {
        if (instance == null) {
            instance = new PotionLibrary();
        }
        return instance;
    }

    public PotionLibrary() {
        Collections.shuffle(colors);

        int i = 0;
        for (String name : names) {
            potions.put(name, colors.get(i));
            i++;
        }
        for (String item : names) {
            discovered.put(item, false);
        }
    }
    public String getRandomName() {
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }
    public Color getColor(String name) {
        return potions.get(name);
    }
    public void itemDiscovered(String name) {
        discovered.put(name, true);
    }
    public boolean isDiscovered(String name) {
        return discovered.get(name);
    }
    public String getName(String name) {
        StringBuilder alias = new StringBuilder();
        if (isDiscovered(name)) {
            Random rand = new Random();
            for (int i = 0; i < 6; i++) {
                char c = (char) (rand.nextInt(26) + 97);
                alias.append(c);
            }
        }
        return alias.toString();
    }
}