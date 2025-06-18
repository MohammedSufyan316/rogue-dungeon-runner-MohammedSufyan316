package trench_rogue_game;

import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trench_rogue_game.items.Item;
import trench_rogue_game.items.ItemTypes;
import trench_rogue_game.player.PlayerStats;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inv;
    Item sword = new Item("Sword", ItemTypes.WEAPON, 3);
    Item armor = new Item("Armor", ItemTypes.ARMOR,5);

    @BeforeEach
    void setUp() {
        inv = new Inventory();
        sword = new Item("Sword", ItemTypes.WEAPON, 3);
        armor = new Item("Armor", ItemTypes.ARMOR,5);

    }

    @Test
    void addItem() {

        assertFalse(inv.getItems().contains(sword));
        inv.addItem(sword);
        assertTrue(inv.getItems().contains(sword));
        inv.addItem(armor);
        assertTrue(inv.getItems().contains(armor));
    }

    @Test
    void useItem() {
        //inv.addItem(sword);
        //inv.addItem(armor);
        assertNull(inv.useItem());

    }

    @Test
    void dropItem() {
        assertNull(inv.dropItem());
        inv.addItem(sword);

        assertSame(inv.dropItem(), sword);
        inv.addItem(armor);
        assertEquals(inv.dropItem(), armor);
    }

    @Test
    void getItems() {
        //assertNull(inv.getItems());
        inv.addItem(sword);
        assertTrue(inv.getItems().contains(sword));
        inv.addItem(armor);
        assertTrue(inv.getItems().contains(armor));
    }





    @Test
    void getWeaponBonus() {
        inv.addItem(sword);
        inv.getWeaponBonus();
        //inv.useItem();
        assertEquals(0, inv.getWeaponBonus());
    }

    @Test
    void getArmorBonus() {
    }

    @Test
    void isEmpty() {
        inv.isEmpty();
        assertTrue(inv.getItems().isEmpty());
        inv.addItem(sword);
        assertFalse(inv.getItems().isEmpty());
    }


}