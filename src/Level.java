import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;

public class Level {
    private Terrain[][] terrain;
    private MyArrayList<Stone> stones;
    private MyArrayList<MovingWall> movingWalls;
    private boolean levelFinished;
    private Player player;

    public Level (Player player) {
        terrain = new Terrain[25][25];
        stones = new MyArrayList<Stone>();
        movingWalls = new MyArrayList<MovingWall>();
        this.player = player;
        player.setTerrain(terrain);
        this.levelFinished = false;
        for (int i = 0; i < terrain.length; i++) {
            terrain[8][i] = new Wall(MainFrame.GRID_SCREEN_RATIO * i,8 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain.length; i++) {
            terrain[7][i] = new Wall(MainFrame.GRID_SCREEN_RATIO * i,7 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain[0].length; i++) {
            terrain[i][0] = new Wall(0,i * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        stones.add(new Stone(MainFrame.GRID_SCREEN_RATIO * 4, MainFrame.GRID_SCREEN_RATIO*2, terrain));
        terrain[4][1] = new Wall(MainFrame.GRID_SCREEN_RATIO * 1,4 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
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

    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }
}
