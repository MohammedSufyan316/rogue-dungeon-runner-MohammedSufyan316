package trench_rogue_game;

import java.util.ArrayList;
import java.util.List;

import trench_rogue_game.items.Item;
import trench_rogue_game.items.ItemTypes;
import trench_rogue_game.player.PlayerStats;

public class Inventory {

    PlayerStats stats = PlayerStats.getInstance();

    private static final int MAX_CAPACITY = 24;
    private final List<Item> items = new ArrayList<>();
    private Item equippedWeapon;
    private Item equippedArmor;

     public boolean addItem(Item item) {
        if (items.size() >= MAX_CAPACITY) return false;
        items.add(item);
        return true;
    }

    public Item useItem() {
        if (items.isEmpty())
            return null;
        Item item = items.remove(0);

        if (item.getType() == ItemTypes.WEAPON) {
            equippedWeapon = item;
            PlayerStats.getInstance().increaseStrength(item.getBonusValue());
            AppMain.showMessage("Equipped weapon: " + item.getName());
        } else if (item.getType() == ItemTypes.ARMOR) {
            equippedArmor = item;
            PlayerStats.getInstance().increaseArmor(item.getBonusValue());
            AppMain.showMessage("Equipped armor: " + item.getName());
        } else {
            AppMain.showMessage("Used item: " + item.getName());
        }

        return item;
    }

    public Item dropItem() {
        PlayerStats stats = PlayerStats.getInstance();

        // Drop equipped weapon first if it exists
        if (equippedWeapon != null) {
            Item dropped = equippedWeapon;
            equippedWeapon = null;
            stats.decreaseStrength(dropped.getBonusValue());
            return dropped;
        }

        // Then drop equipped armor if it exists
        if (equippedArmor != null) {
            Item dropped = equippedArmor;
            equippedArmor = null;
            stats.decreaseArmor(dropped.getBonusValue());
            return dropped;
        }

        // Then drop from inventory
        if (!items.isEmpty()) {
            return items.remove(0);
        }

        return null;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

     public boolean isEquipped(String itemName) {
         return (equippedWeapon != null && equippedWeapon.getName().equals(itemName)) ||
               (equippedArmor != null && equippedArmor.getName().equals(itemName));
    }

    public List<String> getEquippedItems() {
        List<String> equipped = new ArrayList<>();
        if (equippedWeapon != null) equipped.add(equippedWeapon.getName());
        if (equippedArmor != null) equipped.add(equippedArmor.getName());
        return equipped;
    }

    public int getWeaponBonus() {
        return equippedWeapon != null ? equippedWeapon.getBonusValue() : 0;
    }

    public int getArmorBonus() {
        return equippedArmor != null ? equippedArmor.getBonusValue() : 0;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getRemainingSlots() {
        return MAX_CAPACITY - items.size();
    }
    
}
