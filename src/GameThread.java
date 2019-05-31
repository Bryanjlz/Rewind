import java.awt.*;

public class GameThread implements Runnable {
    private int level;
    private Level currentLevel;
    private Player player;
    private int fps;
    private boolean menu;
    private Rectangle playHitbox;
    MyArrayList<Pew> pews;

    public GameThread (Player player) {
        level = 1;
        this.player = player;
        currentLevel = new Level(player);
        currentLevel.loadFile("levels/level" + level + ".txt");
        currentLevel.startLevel(player);
        fps = 0;
        pews = new MyArrayList<Pew>();
        menu = true;
        playHitbox = new Rectangle (550, 500, 250, 100);
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

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    public Rectangle getPlayHitbox() {
        return playHitbox;
    }

    public void setPlayHitbox(Rectangle playHitbox) {
        this.playHitbox = playHitbox;
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
            if (!menu) {
                if (!player.isReversing()) {
                    player.update();
                    for (int i = 0; i < currentLevel.getStones().size(); i++) {
                        currentLevel.getStones().get(i).update();
                    }
                    if (currentLevel.isLevelFinished()) {
                        //pews.add(new Pew(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)), new Font("Arial", (int) (Math.random() * 3), (int) (Math.random() * 50) + 5), (int) (Math.random() * MainFrame.WIDTH), (int) (Math.random() * MainFrame.HEIGHT)));
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        level++;
                        currentLevel.loadFile("levels/level" + level + ".txt");
                        currentLevel.startLevel(player);
                        currentLevel.setLevelFinished(false);
                    }
                    if (player.isDead()) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        currentLevel.loadFile("levels/level" + level + ".txt");
                        currentLevel.startLevel(player);
                    }
                } else {
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
            }
            fps = (int)(1 / (System.nanoTime()/1000000000.0 - time));
        }
    }
}
