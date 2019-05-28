import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Level {
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private MyArrayList<MovingWall> movingWalls;
    private boolean levelFinished;
    private Player player;
    private Point playerLoc;

    public Level (Player player) {
        terrain = new Terrain[18][32];
        stones = new MyArrayList<Stone>();
        movingWalls = new MyArrayList<MovingWall>();
        this.levelFinished = false;
        this.player = player;
    }

    public void startLevel (Player player) {
        this.player = player;
        player.setTerrain(terrain);
        player.setStones(stones);
        player.getHitbox().setLocation(playerLoc);
        player.setDead(false);
        player.startLevel();
    }

    public void loadFile (String path) {
        try {
            File file = new File(path);
            Scanner fileReader = new Scanner(file);
            int terrainWidth = fileReader.nextInt();
            int terrainHeight = fileReader.nextInt();
            terrain = new Terrain[terrainHeight][terrainWidth];
            stones = new MyArrayList<>();
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
                        stones.add(new Stone (j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, terrain, stones, player));
                    } else if (terrainString.equals("p")) {
                        playerLoc = new Point(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);
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

    public MyArrayList<Stone> getStones() {
        return stones;
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
