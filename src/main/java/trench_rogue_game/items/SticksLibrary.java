package trench_rogue_game.items;

import javafx.scene.paint.Color;
import trench_rogue_game.utils.Roll;

import java.util.*;

public class SticksLibrary {
    private static SticksLibrary instance;
    final ItemTypes type = ItemTypes.STICK;
    final private List<String> woods = new ArrayList<>(List.of(
            "avocado wood", "balsa", "bamboo", "banyan",
            "birch", "cedar", "cherry", "cinnabar", "cypress", "dogwood", "driftwood", "ebony", "elm", "eucalyptus", "fall",
            "hemlock", "holly", "ironwood", "kukui wood", "mahogany", "manzanita", "maple", "oaken", "persimmon wood",
            "pecan", "pine", "poplar", "redwood", "rosewood", "spruce", "teak", "walnut", "zebrawood"
    ));
    final public List<String> names = new ArrayList<>(List.of(
            "Polymorph", "Haste Monster", "Light", "Lightning", "Magic Missile",
            "Teleport Away", "Teleport To", "Slow Monster", "Fire", "Striking", "Drain Life", "Cancellation"
    ));
    private Map<String, Boolean> discovered = new HashMap<>();
    final Map<String, String> sticks = new HashMap<>();

    public static SticksLibrary getInstance() {
        if (instance == null) {
            instance = new SticksLibrary();
        }
        return instance;
    }
    public String getRandomName() {
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }

    public int getStartingChange(String name) {
        if (name != "Light") {
            return new Roll(1,5).roll() + 3;
        } else return new Roll(1,10).roll() + 10;
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

    public SticksLibrary() {
        Collections.shuffle(woods);

        int i = 0;
        for (String name : names) {
            sticks.put(name, woods.get(i));
            i++;
        }
        for (String item : names) {
            discovered.put(item, false);
        }
    }
}
