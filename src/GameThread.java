import java.awt.Rectangle;

/**
 * GameThread
 * Main game loop
 * @author Bryan Zhang
 */
public class GameThread implements Runnable {
    private int level;
    private Level currentLevel;
    private Player player;
    private int fps;
    private boolean menu;
    private Rectangle playHitbox;
    private boolean restartLevel;
    private boolean running;
    private static final double PLAY_HITBOX_WIDTH_RATIO = 600 / 1920.0;
    private static final double PLAY_HITBOX_X_RATIO =  660 / 1920.0;
    private static final double PLAY_HITBOX_HEIGHT_RATIO = 250 / 1080.0;
    private static final double PLAY_HITBOX_Y_RATIO = 615.55 / 1080.0;

    /**
     * Creates the game thread.
     * @param player Reference to player.
     */
    public GameThread (Player player) {
        level = 6;
        this.player = player;
        currentLevel = new Level(player);
        currentLevel.startLevel(player, level);
        fps = 0;
        menu = true;
        running = true;
        int boxX = (int)(PLAY_HITBOX_X_RATIO * MainFrame.WIDTH);
        int boxY = (int)(PLAY_HITBOX_Y_RATIO * MainFrame.HEIGHT);
        int boxW = (int)(PLAY_HITBOX_WIDTH_RATIO * MainFrame.WIDTH);
        int boxH = (int)(PLAY_HITBOX_HEIGHT_RATIO * MainFrame.HEIGHT);
        playHitbox = new Rectangle (boxX, boxY, boxW, boxH);
    }

    /**
     * Gets the player object.
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current level.
     * @return A Level that's the current level.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the fps of the game.
     * @return An integer representing the fps.
     */
    public int getFps() {
        return fps;
    }

    /**
     * Checks if the menu is being shown.
     * @return A boolean that represents if the menu is being shown.
     */
    public boolean isMenu() {
        return menu;
    }

    /**
     * Sets the boolean that represents if the menu is being shown.
     * @param menu A boolean that represents if the menu is being shown.
     */
    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    /**
     * Gets the hitbox of the play button on the menu.
     * @return A Rectangle that represents the hitbox of the play button.
     */
    public Rectangle getPlayHitbox() {
        return playHitbox;
    }

    /**
     * Checks if the level is being restarted.
     * @return A boolean that represents if the level is being restarted.
     */
    public boolean isRestartLevel() {
        return restartLevel;
    }

    /**
     * Sets the boolean that represents if the level is being restarted.
     * @param restartLevel A boolean that represents if the level is being restarted.
     */
    public void setRestartLevel(boolean restartLevel) {
        this.restartLevel = restartLevel;
    }

    /**
     * Sets the boolean that represents if the game loop should be running.
     * @param running A boolean that represents if the game loop should be running.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Starts the main game loop
     */
    public void run() {
        while (running) {
            // Gets the current time
            double time = System.nanoTime()/1000000000.0;

            // Delay between frames
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Run game if its not menu
            if (!menu) {
                // If player is not reversing time
                if (!player.isReversing()) {

                    // Unpress every button
                    Terrain[][] terrain = currentLevel.getTerrain();
                    for (int i = 0; i < terrain.length; i++) {
                        for (int j = 0; j < terrain[0].length; j++) {
                            if (terrain[i][j] instanceof Button) {
                                ((Button)terrain[i][j]).setPressed(false);
                            }
                        }
                    }

                    // Update player
                    player.update();

                    // Update crates
                    for (int i = 0; i < currentLevel.getCrates().size(); i++) {
                        currentLevel.getCrates().get(i).update();
                    }

                    // Update moving walls
                    for (int i = 0; i < currentLevel.getMovingWalls().size(); i++) {
                        currentLevel.getMovingWalls().get(i).update();
                    }

                    // Check if level is finished or player is dead or level is restarted
                    if (currentLevel.isLevelFinished()) {
                        level++;
                        currentLevel.startLevel(player, level);
                        currentLevel.setLevelFinished(false);
                    }
                    if (player.isDead() || isRestartLevel()) {
                        currentLevel.startLevel(player, level);
                        setRestartLevel(false);
                    }

                // If player is reversing time
                } else {

                    // Find what is being reversed
                    boolean foundReverse = false;
                    MyArrayList<Crate> crates = currentLevel.getCrates();
                    for (int i = 0; i < crates.size() && !foundReverse; i++) {
                        // Check if current crate is being reversed and there is a previous state to revert to
                        if (crates.get(i).isReverse() && !crates.get(i).getPreviousStateQueue().isEmpty()) {

                            // If reversed crate is picked up by player, place it down
                            if (crates.get(i).isPickedUp()) {
                                crates.get(i).setPickedUp(false);
                                player.placeDownCrate();
                            }

                            // Get previous state of crate and set it to current crate
                            crates.set(i, crates.get(i).getPreviousStateQueue().pollLast());

                            foundReverse = true;

                        // If the previous states queue is empty, stop reverse
                        } else if (crates.get(i).isReverse()) {
                            crates.get(i).setReverse(false);
                            player.setReversing(false);
                        }
                    }

                    // Check if player is reversing them self
                    if (player.isReverse() && !foundReverse) {
                        if (!player.getPreviousStateQueue().isEmpty()) {
                            if (player.getHeldCrate() != null) {
                                player.placeDownCrate();
                            }
                            player.clone(player.getPreviousStateQueue().pollLast());

                        // Stop reverse if previous states queue if empty
                        } else {
                            player.setHoldLeft(false);
                            player.setHoldRight(false);
                            player.setReverse(false);
                            player.setReversing(false);
                        }
                    }
                }
            }

            // Calculate fps based on how long it took to run through one iteration of loop
            fps = (int)(1 / (System.nanoTime()/1000000000.0 - time));
        }
    }
}
