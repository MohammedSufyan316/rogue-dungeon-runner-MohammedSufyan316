package trench_rogue_game.items;

import java.util.*;

public class ScrollsLibrary {
    private static ScrollsLibrary instance;
    final ItemTypes type = ItemTypes.SCROLL;
    final public List<String> names = new ArrayList<>(List.of(
            "Confuse Monster", "Enchant Armor", "Hold Monster", "Sleep", "Create Monster", "Identify",
            "Magic Mapping", "Food Detection", "Teleportation", "Remove Curse", "Enchant Weapon",
            "Scare Monster", "Aggravate Monster", "Vorpal Enchant Weapon", "Nothing"
    ));

    private Map<String, Boolean> discovered = new HashMap<>();

    public static ScrollsLibrary getInstance() {
        if (instance == null) {
            instance = new ScrollsLibrary();
        }
        return instance;
    }
    public ScrollsLibrary() {}
    public String getRandomName() {
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
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
                if (i == 3) { alias.append(" "); }
            }
        }
        return alias.toString();
    }
}
