package trench_rogue_game.player;

import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsTest {

    private PlayerStats stats;

    @BeforeEach
    void setUp() {
        stats = new PlayerStats(
                new Text("Health: 100"),
                new Text("Gold: 0"),
                new Text("Strength: 16"),
                new Text("Exp: 1"),
                new Text("Food: 0"),
                new Text("Hunger: 50"),
                new Text("Armor: 5")
        );
    }

    @Test
    void testTakeDamage() {
        stats.takeDamage(12);
        assertEquals(0, stats.getCurrentHealth());
    }

    @Test
    void testTakeDamageWithArmor(){


    }

    @Test
    void testTakeDamageBeyondZero() {
        stats.takeDamage(150);
        assertEquals(0, stats.getCurrentHealth());
    }

    @Test
    void testHeal() {
        stats.takeDamage(50);
        stats.heal(30);
        assertEquals(12, stats.getCurrentHealth());
    }

    @Test
    void testHealBeyondMax() {
        stats.heal(50);
        assertEquals(12, stats.getCurrentHealth());
    }

    @Test
    void testAddGold() {
        stats.addGold(100);
        assertEquals(100, stats.getGold());
    }

    @Test
    void testConsumeFood() {
        //stats.consumeFood(20);
        assertEquals(1000, stats.getHunger());
    }

    @Test
    void testIncreaseStrength() {
        assertEquals(16, stats.getStrength());
        stats.increaseStrength(5);
        assertEquals(21, stats.getStrength());
        stats.decreaseStrength(10);
        assertEquals(11, stats.getStrength());
    }

    @Test
    void testGainExp() {
        stats.gainExp(3);
        assertEquals(11, stats.getExp());
    }

    @Test
    void testReset() {
        stats.takeDamage(50);
        stats.addGold(100);
        //stats.consumeFood(30);
        stats.increaseStrength(10);
        stats.gainExp(5);
        stats.reset();

        assertEquals(12, stats.getCurrentHealth());
        assertEquals(0, stats.getGold());
        assertEquals(50, stats.getHunger());
        assertEquals(16, stats.getStrength());
        assertEquals(5, stats.getArmor());
        assertEquals(8, stats.getExp());

    }

    @Test
    void testGetMaxHealth() {
        assertEquals(12,stats.getMaxHealth());
    }

    @Test
    void testGetArmor() {
        assertEquals(5,stats.getArmor());
        stats.increaseArmor(10);
        assertEquals(15,stats.getArmor());
        stats.decreaseArmor(6);
        assertEquals(9,stats.getArmor());

    }

    @Test
    void testGetHunger() {
        assertEquals(1000,stats.getHunger());
        stats.updateHunger(-20);
        assertEquals(980,stats.getHunger());
        stats.updateHunger(10);
        assertEquals(990,stats.getHunger());
    }

    @Test
    void testGetLevel() {
        assertEquals(1,stats.getLevel());
        stats.gainExp(20);
        assertEquals(2,stats.getLevel());
        stats.gainExp(20);
        stats.gainExp(20);
        assertEquals(3,stats.getLevel());
    }

    @Test
    void getExpText(){
       // "Level: " + level + " | Exp: " + exp + " / " + (level * 20)
        assertEquals("Level: 1 | Exp: 8 / 20",stats.getExpDisplayText());
        stats.gainExp(20);
        assertEquals("Level: 2 | Exp: 8 / 40",stats.getExpDisplayText());
    }

    @Test
    void getBonusDamageAndArmor(){
        stats.getBonusDamage();
        assertEquals(0, stats.getBonusDamage());
        assertEquals(5, stats.getArmor());
    }
}
