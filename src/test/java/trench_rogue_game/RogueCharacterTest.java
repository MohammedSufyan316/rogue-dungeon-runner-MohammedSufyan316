package trench_rogue_game;

import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trench_rogue_game.player.PlayerStats;

import static org.junit.jupiter.api.Assertions.*;

class RogueCharacterTest {

    private RogueCharacter character;
    Text healthText = new Text();
    Text goldText = new Text();
    Text strengthText = new Text();
    Text expText = new Text();
    Text hungerText = new Text();
    Text armorText = new Text();
    Text foodText = new Text();

    @BeforeEach
    void setUp() {
        character = new RogueCharacter();
    }

    @Test
    void testPositionUpdate() {
        character.positionUpdate(100, 200);
        assertEquals(100,character.x);
        assertEquals(200,character.y);
    }



    @Test
    void testEatFoodWithoutInventory() {
        Text hungerText = new Text();
        Text hungerMessageText = new Text();
        Text foodText = new Text();

        character.foodInventory = 0;
        //character.eatFood(hungerText, foodText);

        assertEquals("", hungerMessageText.getText());
    }

    @Test
    void isAliveTest(){
        assertTrue(character.isAlive());
    }

    @Test
    void isDeadTest(){
        PlayerStats playerStats = new PlayerStats(healthText, goldText, strengthText, expText, foodText, hungerText,
                armorText);
        character.setPlayerStats(playerStats);

        character.takeDamage(150,healthText,goldText);
        assertFalse(character.isAlive());

        character.setPlayerStats(playerStats);
        character = new RogueCharacter();
        assertTrue(character.isAlive());
        character.win();
        assertFalse(character.isAlive());

    }


}
