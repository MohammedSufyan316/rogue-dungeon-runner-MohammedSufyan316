package trench_rogue_game.items;

public class Item {
    //public enum ItemType {WEAPON, ARMOR, POTION, RING, SCROLL}

    private final String name;
    private final ItemTypes type;
    private final int bonusValue; // e.g., +3 strength, +2 armor

    public Item(String name, ItemTypes type, int bonusValue) {
        this.name = name;
        this.type = type;
        this.bonusValue = bonusValue;
    }

    public String getName() {
        return name;
    }

    public ItemTypes getType() {
        return type;
    }

    public int getBonusValue() {
        return bonusValue;
    }

    @Override
    public String toString() {
        return name;
    }
}