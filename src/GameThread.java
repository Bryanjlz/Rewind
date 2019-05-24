public class GameThread implements Runnable {
    private Level[] levels;
    private int currentLevel;
    private Player player;
    public GameThread () {
        player = new Player();
    }
    public void run() {
        while (true) {
            //TODO: Game loop
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.update();
        }
    }

    public Player getPlayer() {
        return player;
    }
}
