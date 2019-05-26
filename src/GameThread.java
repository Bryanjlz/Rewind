import sun.applet.Main;

public class GameThread implements Runnable {
    private Level[] levels;
    private Level currentLevel;
    private Player player;
    private int fps;
    public GameThread () {
        player = new Player(MainFrame.GRID_SCREEN_RATIO * 6, MainFrame.GRID_SCREEN_RATIO * 5);
        currentLevel = new Level(player);
        fps = 0;
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
