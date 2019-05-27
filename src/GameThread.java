import java.awt.*;

public class GameThread implements Runnable {
    private Level[] levels;
    private Level currentLevel;
    private Player player;
    private int fps;
    public GameThread (Player player) {
        this.player = player;
        levels = new Level[10];
        levels[0] = new Level();
        createLevel(levels[0]);
        currentLevel = levels[0];
        currentLevel.startLevel(player);
        fps = 0;
    }

    private void createLevel (Level level) {
        Terrain[][] terrain = level.getTerrain();
        MyArrayList<Stone> stones = level.getStones();
        level.setPlayerLoc(new Point(MainFrame.GRID_SCREEN_RATIO * 6, MainFrame.GRID_SCREEN_RATIO * 5));
        for (int i = 0; i < terrain.length; i++) {
            terrain[i][0] = new Wall(0,MainFrame.GRID_SCREEN_RATIO * i, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain[0].length; i++) {
            terrain[8][i] = new Wall(MainFrame.GRID_SCREEN_RATIO * i,8 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain[0].length; i++) {
            terrain[7][i] = new Wall(MainFrame.GRID_SCREEN_RATIO * i,7 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain.length; i++) {
            terrain[i][0] = new Wall(0,i * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        for (int i = 0; i < terrain.length; i++) {
            terrain[i][15] = new Wall(15*MainFrame.GRID_SCREEN_RATIO,i * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        }
        terrain[4][1] = new Wall(MainFrame.GRID_SCREEN_RATIO * 1,4 * MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO, MainFrame.GRID_SCREEN_RATIO);
        terrain[14][6] = new Exit(MainFrame.GRID_SCREEN_RATIO * 14, MainFrame.GRID_SCREEN_RATIO * 6);
        ((Exit)terrain[14][6]).setCurrentLevel(level);
        stones.add(new Stone(MainFrame.GRID_SCREEN_RATIO * 4, MainFrame.GRID_SCREEN_RATIO*2, terrain, stones));
        stones.add(new Stone(MainFrame.GRID_SCREEN_RATIO * 7, MainFrame.GRID_SCREEN_RATIO*2, terrain, stones));
    }
    public void run() {
        while (true) {
            //TODO: Game loop
            double time = System.nanoTime()/1000000000.0;
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.update();
            for (int i = 0; i < currentLevel.getStones().size(); i++) {
                currentLevel.getStones().get(i).update();
            }
            if (currentLevel.isLevelFinished()) {
                System.out.println("nice");
            }
            fps = (int)(1 / (System.nanoTime()/1000000000.0 - time));
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getFps() {
        return fps;
    }
}
