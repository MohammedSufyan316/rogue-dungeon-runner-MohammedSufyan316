package trench_rogue_game;

import java.util.List;
import java.util.Random;

import trench_rogue_game.monster.Flags;
import trench_rogue_game.monster.MonsterDTO;
import trench_rogue_game.player.PlayerStats;
import javafx.scene.text.Text;

public class MonsterBehaviour {

    public void monsterMovesToPlayer(MonsterSpawnLogic monsterSpawnLogic, double PlayerX, double PlayerY,
            Levels level, RogueCharacter rogueCharacter) {

        for (double[] room : level.getRooms()) {

            for (MonsterDTO monster : monsterSpawnLogic.monsterClasses) {
                if (monster.flag.name().contains("R")) {
                    regenerateMonster(monster);
                }
                if (MonsterAndPlayerSameRoom(PlayerX, PlayerY, monster, room)) {
                    String direction = "";

                    if (monster.flag == Flags.G) {
                        // Greedy monsters look for nearby gold
                        double[] nearestGold = null;
                        double minDist = Double.MAX_VALUE;

                        double[][] goldCoords = level.getGoldCoords();

                        for (int i = 0; i < level.goldCount; i++) {
                            if (!level.goldAvailable[i])
                                continue;
                            double gx = goldCoords[i][0];
                            double gy = goldCoords[i][1];

                            if (!level.isInsideRoom(gx, gy, room))
                                continue;

                            double dx = gx - monster.getXPosition();
                            double dy = gy - monster.getYPosition();
                            double dist = Math.sqrt(dx * dx + dy * dy);

                            if (dist < minDist) {
                                minDist = dist;
                                nearestGold = level.goldCoords[i];
                            }
                        }

                        if (nearestGold != null) {
                            double dx = nearestGold[0] - monster.getXPosition();
                            double dy = nearestGold[1] - monster.getYPosition();

                            // If reached gold, take it
                            // If reached gold, take it and skip chasing it further
                            if (Math.abs(dx) <= 10 && Math.abs(dy) <= 10) {
                                for (int i = 0; i < level.goldCount; i++) {
                                    if (level.goldAvailable[i] &&
                                            level.goldCoords[i][0] == nearestGold[0] &&
                                            level.goldCoords[i][1] == nearestGold[1]) {
                                        level.goldAvailable[i] = false;
                                        AppMain.showMessage(monster.name + " took the gold!");
                                        return;
                                    }
                                }
                            }

                            dx = nearestGold[0] - monster.getXPosition();
                            dy = nearestGold[1] - monster.getYPosition();
                            direction = Math.abs(dx) > Math.abs(dy) ? (dx < 0 ? "LEFT" : "RIGHT")
                                    : (dy < 0 ? "UP" : "DOWN");
                        } else {
                            double[] distances = distance(PlayerX, PlayerY, monster);
                            if (Math.abs(distances[0]) > Math.abs(distances[1])) {
                                direction = distances[0] < 0 ? "LEFT" : "RIGHT";
                            } else {
                                direction = distances[1] < 0 ? "UP" : "DOWN";
                            }
                        }

                    } else {
                        double[] distances = distance(PlayerX, PlayerY, monster);
                        // For vertical movement if player and monster are on the same x position.
                        if (monster.getXPosition() == PlayerX && monster.getYPosition() != PlayerY) {
                            String XorY = "y";

                            if (distances[1] < 0) {
                                direction = "UP";
                            } else {
                                direction = "DOWN";
                            }

                            if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(), level.foodPositions,
                                    monster, direction)) {
                                if (distances[0] < 0) {
                                    direction = "LEFT";
                                } else {
                                    direction = "RIGHT";
                                }
                            }

                        } else if (monster.getYPosition() == PlayerY && monster.getXPosition() != PlayerX) {
                            String XorY = "x";

                            if (distances[0] < 0) {
                                direction = "LEFT";
                            } else {
                                direction = "RIGHT";
                            }

                            if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(), level.foodPositions,
                                    monster, direction)) {
                                if (distances[1] < 0) {
                                    direction = "UP";
                                } else {
                                    direction = "DOWN";
                                }
                            }
                        }

                        // For vertical movement if not on the same x. This will ensure to close the gap
                        // with the greater
                        // distance of y than x
                        else if (Math.abs(distances[0]) < Math.abs(distances[1])) {
                            // double xDistance = PlayerX = monster.getXPosition();

                            if (distances[1] < 0) {
                                direction = "UP";
                            } else {
                                direction = "DOWN";
                            }

                            if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(), level.foodPositions,
                                    monster, direction)) {
                                if (distances[0] < 0) {
                                    direction = "LEFT";
                                } else {
                                    direction = "RIGHT";
                                }
                            }

                        } else if (Math.abs(distances[0]) > Math.abs(distances[1])) {

                            if (distances[0] < 0) {
                                direction = "LEFT";
                            } else {
                                direction = "RIGHT";
                            }

                            if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(), level.foodPositions,
                                    monster, direction)) {
                                if (distances[1] < 0) {
                                    direction = "UP";
                                } else {
                                    direction = "DOWN";
                                }
                            }
                        } else if (distances[0] == distances[1]) {
                            Random rand = new Random();
                            double[] index = new double[2];
                            index[0] = 0;
                            index[1] = 1;
                            double chosen = index[(int) rand.nextDouble(2)];

                            if (chosen == 0) {
                                if (distances[0] < 0) {
                                    direction = "LEFT";
                                } else {
                                    direction = "RIGHT";
                                }

                                if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(),
                                        level.foodPositions, monster, direction)) {
                                    if (distances[1] < 0) {
                                        direction = "UP";
                                    } else {
                                        direction = "DOWN";
                                    }
                                }
                            } else {
                                if (distances[1] < 0) {
                                    direction = "UP";
                                } else {
                                    direction = "DOWN";
                                }

                                if (monsterNearObject(level.getStairPosition(), level.getKeyPosition(),
                                        level.foodPositions, monster, direction)) {
                                    if (distances[0] < 0) {
                                        direction = "LEFT";
                                    } else {
                                        direction = "RIGHT";
                                    }
                                }
                            }
                        }

                    }
                    if (!monsterNearPlayer(PlayerX, PlayerY, monster)) {
                        newMonsterPosition(monster, direction, monsterSpawnLogic.getMonsterClasses(), level);
                    }

                }
            }
        }
    }

    public boolean MonsterAndPlayerSameRoom(double PlayerX, double PlayerY, MonsterDTO monster, double[] room) {

        double mx = monster.getXPosition(), my = monster.getYPosition();
        double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
        boolean monsterCheck = mx >= rx && mx < rx + rw && my >= ry && my < ry + rh;
        boolean PlayerCheck = PlayerX >= rx && PlayerX < rx + rw && PlayerY >= ry && PlayerY < ry + rh;
        return monsterCheck && PlayerCheck;
    }

    public double[] distance(double PlayerX, double PlayerY, MonsterDTO monster) {
        double[] distance = new double[2];
        double monsterX = monster.getXPosition(), monsterY = monster.getYPosition();
        distance[0] = (PlayerX - monsterX);
        distance[1] = (PlayerY - monsterY);
        return distance;
    }

    public void newMonsterPosition(MonsterDTO monster, String direction, List<MonsterDTO> monsterList, Levels level) {
        double newX = monster.getXPosition();
        double newY = monster.getYPosition();

        switch (direction) {
            case "UP" -> newY -= 10;
            case "DOWN" -> newY += 10;
            case "LEFT" -> newX -= 10;
            case "RIGHT" -> newX += 10;
        }

        if (level.checkCollision(newX, newY, 10, 10)) {
            return;
        }

        // Prevent overlap with other monsters
        for (MonsterDTO other : monsterList) {
            if (other != monster && other.isAlive && other.getXPosition() == newX && other.getYPosition() == newY) {
                return;
            }
        }

        monster.newPosition(newX, newY);
    }

    public boolean monsterNearPlayer(double PlayerX, double PlayerY, MonsterDTO monster) {
        if (monster.getXPosition() - 10 == PlayerX) {
            return true;
        } else if (monster.getXPosition() + 10 == PlayerX) {
            return true;
        } else if (monster.getYPosition() - 10 == PlayerY) {
            return true;
        } else
            return monster.getYPosition() + 10 == PlayerY;
    }

    public void monsterAttackPlayer(MonsterSpawnLogic spawnLogic, double playerX, double playerY,
            RogueCharacter rogueCharacter, Text healthText, Text goldText) {
        for (MonsterDTO monster : spawnLogic.getMonsterClasses()) {
            if (!monster.isAlive)
                continue;

            double mx = monster.getXPosition();
            double my = monster.getYPosition();

            boolean adjacent = (mx == playerX && Math.abs(my - playerY) == 10) ||
                    (my == playerY && Math.abs(mx - playerX) == 10);

            if (adjacent) {
                int rawDamage = monster.getDMG();
                int reducedDamage = Math.max(0, rawDamage - PlayerStats.getInstance().getBonusArmor());
                
                // int damage = monster.getAttackPower();
                rogueCharacter.takeDamage(reducedDamage, healthText, goldText);

                AppMain.showMessage(monster.name + " hit you for " + reducedDamage + "!");

                if (monster.flag == Flags.G && PlayerStats.getInstance().getGold() > 0) {
                    int stolen = Math.min(5, PlayerStats.getInstance().getGold());
                    PlayerStats.getInstance().addGold(-stolen);
                    AppMain.showMessage(monster.name + " stole " + stolen + " gold from you!");
                    PlayerStats.getInstance().updateGoldText();
                }
            }
        }
    }

    public boolean monsterNearObject(double[] stairPosition, double[] keyPosition, double[][] foodPositions,
            MonsterDTO monster, String direction) {

        double newX = monster.getXPosition();
        double newY = monster.getYPosition();

        switch (direction) {
            case "UP" -> newY -= 10;
            case "DOWN" -> newY += 10;
            case "LEFT" -> newX -= 10;
            case "RIGHT" -> newX += 10;
        }

        if ((stairPosition[0] == newX && stairPosition[1] == newY) ||
                (keyPosition[0] == monster.getXPosition() && keyPosition[1] == newY)) {
            return true;

        }
        for (int i = 0; i < foodPositions.length; i++) {

            if (foodPositions[i][0] == newX && foodPositions[i][1] == newY) {
                return true;
            }
        }

        return false;
    }

    private void regenerateMonster(MonsterDTO monster) {
        if (monster.CURR_HPT < monster.INIT_HPT && monster.isAlive) {
            monster.CURR_HPT += 1;

            if (monster.CURR_HPT > monster.INIT_HPT) {
                monster.CURR_HPT = monster.INIT_HPT;
            } 
                AppMain.showMessage(monster.name + " regenerates some health!");
            
        }
    }

}
