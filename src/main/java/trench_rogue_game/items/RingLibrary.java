package trench_rogue_game.items;

import java.util.*;

public class RingLibrary {
    private static RingLibrary instance;
    final ItemTypes type = ItemTypes.RING;
    final private List<String> stones = new ArrayList<>(List.of(
            "agate", "alexandrite", "amethyst", "carnelian", "diamond", "emerald", "germanium",
            "granite", "garnet", "jade", "kryptonite", "lapis lazuli", "moonstone", "obsidian", "onyx",
            "opal", "pearl", "peridot", "ruby", "sapphire", "stibotantalite", "tiger eye", "topaz",
            "turquoise", "taaffeite", "zircon"
    ));

    final public List<String> names = new ArrayList<>(List.of(
            "Slow Digestion", "Sustain Armor", "Sustain Strength", "Regeneration", "Increase Damage",
            "Stealth", "Protection", "Teleportation", "Searching", "Add Strength", "See Invisible", "Aggravate Monster"
    ));
    private Map<String, Boolean> discovered = new HashMap<>();

    final Map<String, String> scrolls = new HashMap<>();
    public static RingLibrary getInstance() {
        if (instance == null) {
            instance = new RingLibrary();
        }
        return instance;
    }

    public RingLibrary() {
        Collections.shuffle(stones);

        int i = 0;
        for (String name : names) {
            scrolls.put(name, stones.get(i));
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
