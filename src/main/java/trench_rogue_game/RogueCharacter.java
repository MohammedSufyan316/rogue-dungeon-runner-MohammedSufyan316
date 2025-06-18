package trench_rogue_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import trench_rogue_game.player.PlayerStats;
import trench_rogue_game.items.Item;

import java.util.List;

public class RogueCharacter {
    private final double spawnX = 400;
    private final double spawnY = 400;
    double x = spawnX;
    double y = spawnY;
    private final double step = 10;
    private boolean isAlive = true;
    private String deathReason = "";
    int foodInventory = 0;
    private List<double[]> rooms;
    private PlayerStats playerStats;
    private final Inventory inventory = new Inventory();

    public void setPlayerStats(PlayerStats stats) {
        this.playerStats = stats;
    }

    void positionUpdate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawCharacter(GraphicsContext gc, Levels Level) {
        //gc.setFill(Color.BLACK);
        //gc.fillRect(0, 0, 600, 600); // Clear the canvas

        Level.drawFood(gc); // Draw food every time
        Level.drawGold(gc);
        Level.drawItems(gc);

        if (isAlive) {
            gc.setFill(Color.LIGHTGREEN);
            //gc.fillText("@", x, y); // Player as '@'
            gc.fillOval(x, y, 10, 10);
            //gc.setFont(new Font(14));
            //gc.fillText("🧝", x, y + 10);

        } else {
            gc.setFill(Color.RED);
            gc.setFont(new Font(20));
            drawGameOverStats(gc);
        }

    }

    public void decreaseHunger(Text hungerText) {
        playerStats.updateHunger(-1);
        printHungerStatus();
        if (playerStats.getHunger() <= 0 ) {
            isAlive = false;
            deathReason = "You starved to death!";
            AppMain.showMessage("You collapsed from hunger.");
        }
        hungerText.setText("Hunger: " + playerStats.getHunger());
    }


    public void eatFood() {
        if (foodInventory > 0) {
            //hunger = Math.min(50, hunger + 15);
            playerStats.updateHunger(+50);
            foodInventory--;
            AppMain.showMessage("You ate food. Inventory: " + foodInventory);
        } else {
            AppMain.showMessage("No food to eat!");
        }
    }

    public void printHungerStatus() {
        int hunger = playerStats.getHunger();
        if (hunger < 10)
            AppMain.showMessage("You feel dizzy...");
        else if (hunger < 20)
            AppMain.showMessage("You feel weak...");
        else if (hunger < 30)
            AppMain.showMessage("You are hungry.");
    }

    public void takeDamage(int amount, Text healthText, Text goldText) {
        playerStats.takeDamage(amount);

        if (playerStats.getCurrentHealth() <= 0) {
        isAlive = false;
        deathReason = "You were slain!";
        AppMain.showMessage("You were slain!");
        }

        healthText.setText("Health: " + playerStats.getCurrentHealth() + " / " + playerStats.getMaxHealth());
        goldText.setText("Gold: " + Math.floor(playerStats.getGold() * 0.9));

    }

    public String getDeathReason() {
        return deathReason;
    }

    public void win(){
    isAlive = false;
    deathReason = "You won the game!";
    AppMain.showMessage(deathReason);
    }

    public void drawGameOverStats(GraphicsContext gc) {
        gc.setFill(Color.GOLD);
        gc.setFont(new Font("Monospaced", 24));
        gc.fillText(deathReason, 400, 200);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Monospaced", 20));
        gc.fillText("Level: " + playerStats.getLevel(), 400, 240);
        gc.fillText("Exp: " + playerStats.getExp(), 400, 270);
        gc.fillText("Gold: " + playerStats.getGold(), 400, 300);
        gc.fillText("Strength: " + playerStats.getStrength(), 400, 330);
        gc.fillText("Food: " + foodInventory, 400, 360);
    }

    public boolean isAlive() {
    return isAlive;
}

public void addItem(Item item) {
    boolean success = inventory.addItem(item);
    if (success) {
        AppMain.showMessage("Picked up: " + item);
    } else {
        AppMain.showMessage("Inventory full! Can't carry " + item);
    }
}

public Inventory getInventory() {
    return inventory;
}

}
