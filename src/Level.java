import java.awt.*;
import java.util.Arrays;

public class Level {
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private MyArrayList<MovingWall> movingWalls;
    private boolean levelFinished;
    private Player player;
    private Point playerLoc;

    public Level () {
        terrain = new Terrain[18][32];
        stones = new MyArrayList<Stone>();
        movingWalls = new MyArrayList<MovingWall>();
        this.levelFinished = false;
    }

    public Level (Level level) {
        copyTerrain(level.getTerrain(), terrain);

    }

    private void copyTerrain (Terrain[][] ori, Terrain[][] dest) {
        for (int i = 0; i < ori.length; i++) {
            for (int j = 0; j < ori[0].length; j++) {
                ori[i][j] = dest[i][j];
            }
        }
    }

    public void startLevel (Player player) {
        this.player = player;
        player.setTerrain(terrain);
        player.setStones(stones);
        player.getHitbox().setLocation(playerLoc);
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
