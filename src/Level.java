import java.awt.*;
import java.io.File;
import java.util.Scanner;

/**
 * Level
 * A level for the game
 * @author Bryan Zhang
 * @since
 */
public class Level {
    private Terrain[][] terrain;
    private MyArrayList<Crate> crates;
    private MyArrayList<MovingWall> movingWalls;
    private MyArrayList<Key> keys;
    private boolean levelFinished;
    private Player player;
    private Point playerLoc;

    public Level (Player player) {
        terrain = new Terrain[18][32];
        crates = new MyArrayList<Crate>();
        movingWalls = new MyArrayList<MovingWall>();
        keys = new MyArrayList<Key>();
        this.levelFinished = false;
        this.player = player;
    }

    public void startLevel (Player player, int level) {
        try {
            Thread.sleep(450);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadFile("levels/level" + level + ".txt");
        this.player = player;
        player.setVel(new MyVector(0,0));
        player.setAcc(new MyVector(0, 0));
        player.setKeys(keys);
        player.setTerrain(terrain);
        player.setCrates(crates);
        player.getHitbox().setLocation(playerLoc);
        player.setObjectQueue(new MyQueue<Player>());
        player.setDead(false);
        player.startLevel();
    }

    private void loadFile (String path) {
        try {
            File file = new File(path);
            Scanner fileReader = new Scanner(file);
            int terrainWidth = fileReader.nextInt();
            int terrainHeight = fileReader.nextInt();
            terrain = new Terrain[terrainHeight][terrainWidth];
            crates = new MyArrayList<>();
            MainFrame.gridScreenRatio = MainFrame.HEIGHT / terrainHeight;
            player.getHitbox().setSize(MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);
            for (int i = 0; i < terrainHeight; i++) {
                for (int j = 0; j < terrainWidth; j++) {
                    String terrainString = fileReader.next();
                    if (terrainString.equals("w")) {
                        terrain[i][j] = new Wall(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);
                    } else if (terrainString.equals("e")) {
                        Exit exit = new Exit(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);
                        exit.setCurrentLevel(this);
                        terrain[i][j] = exit;
                    } else if (terrainString.equals("s")) {
                        crates.add(new Crate (j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, terrain, crates, player));
                    } else if (terrainString.equals("p")) {
                        playerLoc = new Point(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);
                    } else if (terrainString.charAt(0) == 'k') {
                        keys.add(new Key(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, Character.getNumericValue(terrainString.charAt(1))));
                    } else if (terrainString.charAt(0) == 'd') {
                        terrain[i][j] = new Door(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, Character.getNumericValue(terrainString.charAt(1)));
                    }
                }
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MyArrayList<MovingWall> getMovingWalls() {
        return movingWalls;
    }

    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }

    public void setPlayerLoc(Point playerLoc) {
        this.playerLoc = playerLoc;
    }

    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }
}
