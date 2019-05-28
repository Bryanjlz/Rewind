import java.awt.*;

public class GameThread implements Runnable {
    private Level[] levels;
    private Level currentLevel;
    private Player player;
    private int fps;
    public GameThread (Player player) {
        this.player = player;
        levels = new Level[10];
        levels[0] = new Level(player);
        levels[0].loadFile("testlevel.txt");
        currentLevel = levels[0];
        currentLevel.startLevel(player);
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
            for (int i = 0; i < currentLevel.getStones().size(); i++) {
                currentLevel.getStones().get(i).update();
            }
            if (currentLevel.isLevelFinished()) {
                System.out.println("nice");
            }
            if (player.isDead()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentLevel.loadFile("testlevel.txt");
                currentLevel.startLevel(player);
            }
            if (player.isReversing()) {
                boolean foundReverse = false;
                MyArrayList<Stone> stones = currentLevel.getStones();
                for (int i = 0; i < stones.size() && !foundReverse; i++) {
                    if (stones.get(i).isReverse() && !stones.get(i).getObjectQueue().isEmpty()) {
                        stones.set(i, stones.get(i).getObjectQueue().pollLast());
                        foundReverse = true;
                    } else {
                        stones.get(i).setReverse(false);
                        player.setReversing(false);
                    }
                }

                if (player.isReverse() && !foundReverse) {
                    if (!player.getObjectQueue().isEmpty()) {
                        player = player.getObjectQueue().pollLast();
                        foundReverse = true;
                    } else {
                        player.setReverse(false);
                        player.setReversing(false);
                    }
                }
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
