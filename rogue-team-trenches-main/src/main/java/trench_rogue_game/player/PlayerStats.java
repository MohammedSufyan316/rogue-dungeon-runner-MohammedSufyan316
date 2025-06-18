package trench_rogue_game.player;

import trench_rogue_game.Inventory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import trench_rogue_game.AppMain;

public class PlayerStats {
    private final Text healthText;
    private final Text goldText;
    private final Text strengthText;
    private final Text expText;
    private final Text foodText;
    private final Text hungerText;
    private final Text armorText;

    private final int maxHealth = 12;
    private int currentHealth = 12;
    private int gold = 0;
    private int hunger = 1000;
    private int strength = 16;
    private int armor = 5;
    private int exp = 8;
    private int level = 1; 
    private Inventory inventory = new Inventory();
    private static PlayerStats instance;

//    public PlayerStats() {
//        this.maxHealth = 100;
//        this.currentHealth = 100;
//        this.gold = 0;
//        this.hunger = 50;
//        this.strength = 16;
//        this.armor = 0;
//        this.exp = 1;
//    }

    public PlayerStats(Text healthText, Text goldText, Text strengthText, Text expText,
                       Text foodText, Text hungerText, Text armorText) {
        this.healthText = healthText;
        this.goldText = goldText;
        this.strengthText = strengthText;
        this.expText = expText;
        this.foodText = foodText;
        this.hungerText = hungerText;
        this.armorText = armorText;
        instance = this;
    }

    public void drawStats(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Monospaced", 20));

        double yOffset = canvasHeight - 20; // Start drawing from the bottom of the canvas

        gc.fillText(healthText.getText(), 10, yOffset);
        gc.fillText(goldText.getText(), 10, yOffset - 20);
        gc.fillText(strengthText.getText(), 10, yOffset - 40);
        gc.fillText(expText.getText(), 10, yOffset - 60);
        gc.fillText(foodText.getText(), 10, yOffset - 80);
        gc.fillText(armorText.getText(), 10, yOffset - 100);
        
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getGold() {
        return gold;
    }

    public int getHunger() {
        return hunger;
    }

    public int getStrength() {
        return strength;
    }

    public int getArmor() {
        return armor;
    }

    public int getExp() {
        return exp;
    }

    public void takeDamage(int amount) {
        int reduced = Math.max(1, amount - getBonusArmor());
        currentHealth = Math.max(0, currentHealth - reduced);
    }

    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public void updateHunger(int amount) {
        hunger = Math.max(0, hunger + amount);
        hungerText.setText("Hunger: " + hunger);
    }


    public void increaseStrength(int amount) {
        strength += amount;
        if (strengthText != null)
            strengthText.setText("Strength: " + strength);
    }

    public void gainExp(int amount) {
        exp += Math.min(amount, 25); // cap per kill
        int requiredExp = level * 20;

        while (exp >= requiredExp) {
            exp -= requiredExp;
            level++;
            strength += 2;
            currentHealth = Math.min(maxHealth, currentHealth + 10);
            AppMain.showMessage("Level up! You're now level " + level);
            requiredExp = level * 20;
        }
        expText.setText(getExpDisplayText());
    }

    public int getLevel() {
        return level;
    }


    public void reset() {
        currentHealth = maxHealth;
        gold = 0;
        hunger = 50;
        strength = 16;
        armor = 5;
        exp = 8;
    }

    public String getExpDisplayText() {
        return "Level: " + level + " | Exp: " + exp + " / " + (level * 20);
    }

    public static PlayerStats getInstance() {
        return instance;
    }

    public void updateGoldText() {
        goldText.setText("Gold: " + gold);
    }

    public int getBonusDamage() {
        return inventory.getWeaponBonus();
    }

    public int getBonusArmor() {
        return inventory.getArmorBonus();
    }

    public void decreaseStrength(int amount) {
        strength -= amount;
        if (strengthText != null)
            strengthText.setText("Strength: " + strength);
    }

    public void increaseArmor(int amount) {
        armor += amount;
        if (armorText != null)
            armorText.setText("Armor: " + armor);
    }

    public void decreaseArmor(int amount) {
        armor -= amount;
        if (armorText != null)
            armorText.setText("Armor: " + armor);
    }

}
