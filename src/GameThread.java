import java.awt.*;

public class GameThread implements Runnable {
    private Level[] levels;
    private Level currentLevel;
    private Player player;
    private int fps;
    MyArrayList<Pew> pews;
    public GameThread (Player player) {
        this.player = player;
        levels = new Level[10];
        levels[0] = new Level(player);
        levels[0].loadFile("levels/level1.txt");
        currentLevel = levels[0];
        currentLevel.startLevel(player);
        fps = 0;
        pews = new MyArrayList<Pew>();
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
                pews.add(new Pew(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)), new Font("Arial", (int) (Math.random() * 3), (int) (Math.random() * 50) + 5), (int) (Math.random() * MainFrame.WIDTH), (int) (Math.random() * MainFrame.HEIGHT)));
            }
            if (player.isDead()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentLevel.loadFile("levels/level1.txt");
                currentLevel.startLevel(player);
            }
            if (player.isReversing()) {
                boolean foundReverse = false;
                MyArrayList<Stone> stones = currentLevel.getStones();
                for (int i = 0; i < stones.size() && !foundReverse; i++) {
                    if (stones.get(i).isReverse() && !stones.get(i).getObjectQueue().isEmpty()) {
                        stones.set(i, stones.get(i).getObjectQueue().pollLast());
                        foundReverse = true;
                    } else if (stones.get(i).isReverse()) {
                        stones.get(i).setReverse(false);
                        player.setReversing(false);
                    }
                }

                if (player.isReverse() && !foundReverse) {
                    if (!player.getObjectQueue().isEmpty()) {
                        player.clone(player.getObjectQueue().pollLast());
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
