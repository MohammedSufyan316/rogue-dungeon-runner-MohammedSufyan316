package trench_rogue_game.items;

import trench_rogue_game.utils.Roll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeaponLibrary {
    private static WeaponLibrary instance;
    final ItemTypes type = ItemTypes.WEAPON;
    final Map<String, List<Roll>> weapons = new HashMap<>();

    public static WeaponLibrary getInstance() {
        if (instance == null) {
            instance = new WeaponLibrary();
        }
        return instance;
    }

    public WeaponLibrary() {
        weapons.put("Mace", List.of(new Roll(2, 4), new Roll(1, 2)));
        weapons.put("Longsword", List.of(new Roll(3, 4), new Roll(1, 3)));
        weapons.put("Dagger", List.of(new Roll(1, 6), new Roll(1, 4)));
        weapons.put("Two-Handed Sword", List.of(new Roll(4, 4), new Roll(1, 2)));
        weapons.put("Spear", List.of(new Roll(2, 3), new Roll(1, 6)));
    }
    public int getDamgeWielded(String name) {
        return weapons.get(name).get(0).roll();

    }
    public int getDamgeThrown(String name) {
        return weapons.get(name).get(1).roll();
    }
}
