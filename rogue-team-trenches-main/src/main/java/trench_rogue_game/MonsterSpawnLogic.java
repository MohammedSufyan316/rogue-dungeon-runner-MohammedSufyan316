package trench_rogue_game;

import trench_rogue_game.monster.Flags;
import trench_rogue_game.monster.Monster;
import trench_rogue_game.monster.MonsterDTO;
import trench_rogue_game.monster.MonsterLibrary;
import trench_rogue_game.utils.AppInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

public class MonsterSpawnLogic {

    ArrayList<MonsterDTO> monsterClasses = new ArrayList<>();
    public ArrayList<double[]> monsterPositions = new ArrayList<>();
    public ArrayList<double[]> goldDrops = new ArrayList<>();
    MonsterLibrary monsterLibrary = MonsterLibrary.getInstance();
    private final double step = 10;
    private final Random rand = new Random();


    public void createMonsterPositions(List<double[]> rooms, List<double[]> walls,
            ArrayList<String> monsterTypes, double PlayerX, double PlayerY,
            int count, int dungeonLevel) {
        monsterClasses = new ArrayList<>();
        monsterPositions = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            boolean validPositionFound = false;
            double x = 0, y = 0;

            //String monsterName = monsterTypes.get(rand.nextInt(monsterTypes.size()));
            //int randIndex = rand.nextInt(26);
            Monster monster = monsterLibrary.getMonster(rand.nextInt(26));
            while (dungeonLevel < monster.MIN_LVLFound || dungeonLevel > monster.MAX_LVLFound) {
                monster = monsterLibrary.getMonster(rand.nextInt(26));
            }

            while (!validPositionFound) {
                double[] room = rooms.get(rand.nextInt(rooms.size()));
                x = room[0] + rand.nextInt((int) (room[2] / 10)) * 10;
                y = room[1] + rand.nextInt((int) (room[3] / 10)) * 10;

                boolean onWall = false;
                for (double[] wall : walls) {
                    if (Math.abs(x - wall[0]) < 10 && Math.abs(y - wall[1]) < 10) {
                        onWall = true;
                        break;
                    }
                }

                boolean onPlayer = (x == PlayerX && y == PlayerY);

                boolean onOtherMonster = false;
                for (MonsterDTO monsterInstance : monsterClasses) {
                    if (x == monsterInstance.getXPosition() && y == monsterInstance.getYPosition()) {
                        onOtherMonster = true;
                        break;
                    }
                }

                if (!onWall && !onPlayer && !onOtherMonster) {
                    validPositionFound = true;
                }
            }

            // int health = MonsterHealthGenerator(monsterName);
            // int attackPower = MonsterAttackStat(monsterName);
            // MonsterClass monster = new MonsterClass(monsterName, health, true, x, y, attackPower);
            // monsterClasses.add(monster);

            MonsterDTO monsterDTO = monster.makeMonsterDTO(x, y);
            double hp = monsterDTO.getHPT(dungeonLevel);
            monsterDTO.INIT_HPT = hp;
            monsterDTO.CURR_HPT = hp;
            monsterDTO.goldDrop = rand.nextInt(21) + 5; // Drop 5–25 gold
            monsterClasses.add(monsterDTO);
            monsterPositions.add(new double[]{x, y});
        }
    }

      public void drawMonster(GraphicsContext gc, Set<double[]> visitedRooms) {
        for (MonsterDTO monster : monsterClasses) {
            if (!monster.isAlive)
                continue;

            boolean shouldDraw = false;

            // Distance to player
            double dx = monster.getXPosition() - AppMain.getPlayerX();
            double dy = monster.getYPosition() - AppMain.getPlayerY();
            double distance = Math.sqrt(dx * dx + dy * dy);

              // switch (monster.getName().toLowerCase()) {
            // case "snake": symbol = "🐍"; break;
            // case "bear": symbol = "🐻"; break;
            // case "dragon": symbol = "🐉"; break;
            // case "vampire": symbol = "🧛‍♀️"; break;
            // case "goblin": symbol = "👹"; break;
            // default: symbol = "?"; break;
            // }

              if (monster.flag == Flags.I) {
                  shouldDraw = distance < 30;
              } else {
                  // Normal: only show if in visited room
                  for (double[] room : visitedRooms) {
                      double rx = room[0], ry = room[1], rw = room[2], rh = room[3];
                      if (monster.getXPosition() >= rx && monster.getXPosition() < rx + rw &&
                              monster.getYPosition() >= ry && monster.getYPosition() < ry + rh) {
                          shouldDraw = true;
                          break;
                      }
                  }
              }

              if (shouldDraw) {
                  gc.setFont(new Font("Arial", 14)); // Set font inside the loop
                  gc.setFill(Color.RED);
                  String symbol = monster.name.substring(0, 1);
                  if (Objects.equals(monster.name, "Venus Flytrap")) {
                      symbol = "F";
                  }

                  gc.fillText(symbol, monster.getXPosition(), monster.getYPosition() + 10);
                  // gc.fillRect(monster.getXPosition(), monster.getYPosition(), 10, 10);
              }
          }

        gc.setFill(Color.GOLD);
        gc.setFont(new Font("Arial", 14));
        for (double[] g : goldDrops) {
            gc.fillText("💰", g[0], g[1] + 10);
        }
    }

    public boolean checkMonsterCollision(double newX, double newY) {
        for (MonsterDTO monster : monsterClasses) {
            if (monster.getXPosition() == newX && monster.getYPosition() == newY) {
                return true;
            }

            // if (x < wall[0] + wall[2] && x + width > wall[0] &&
            // y < wall[1] + wall[3] && y + height > wall[1]) {
            // return true;
            // }

            if (newX < monster.getXPosition() + 10 && newX + 10 > monster.getXPosition() &&
                    newY < monster.getYPosition() + 10 && newY + 10 > monster.getYPosition()) {
                return true;
            }
        }
        return false;
    }

public AppInfo MonsterHealthDecrease(double damage, double playerX, double playerY) {
    for (MonsterDTO monster : monsterClasses) {
            if (!monster.isAlive) continue;

        boolean adjacent = (monster.getXPosition() == playerX && Math.abs(monster.getYPosition() - playerY) == 10) ||
         (monster.getYPosition() == playerY && Math.abs(monster.getXPosition() - playerX) == 10);

        if (adjacent) {
            boolean hit = Math.random() < 0.75; // 75% hit chance
            if (hit) {
                monster.CURR_HPT -= damage;
                String msg = "You hit the " + monster.name + " for " + (int) damage + " damage!";
                if (monster.CURR_HPT <= 0) {
                    monster.isAlive = false;
                    monsterClasses.remove(monster);
                    goldDrops.add(new double[]{monster.getXPosition(), monster.getYPosition(), monster.goldDrop});

                    return new AppInfo(msg + " You killed it!", monster.EXP);
                } else {
                    return new AppInfo(msg, 0);
                }
            } else {
                return new AppInfo("You missed the attack!", 0);
            }
        }
    }
    return new AppInfo("No monster in range to attack.", 0);
}

    public ArrayList<MonsterDTO> getMonsterClasses() {
        return monsterClasses;
    }
    
}
