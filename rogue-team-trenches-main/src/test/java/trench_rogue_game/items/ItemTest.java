package trench_rogue_game.items;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item sword = new Item("Sword", ItemTypes.WEAPON, 3);
    Item armor = new Item("Armor", ItemTypes.ARMOR, 5);

    @Test
    void getName() {
        assertEquals("Sword", sword.getName());
        assertEquals("Armor", armor.getName());
    }

    @Test
    void getType() {
        assertEquals(sword.getType(), ItemTypes.WEAPON);
        assertEquals(armor.getType(), ItemTypes.ARMOR);
    }

    @Test
    void getBonusValue() {
        assertEquals(sword.getBonusValue(), 3);
        assertEquals(armor.getBonusValue(), 5);
    }
}