import java.awt.Rectangle;

/**
 * GameThread
 * Main game loop
 * @author Bryan Zhang
 * @since June 13/2019
 */
public class GameThread implements Runnable {
    private int level;
    private Level currentLevel;
    private Player player;
    private boolean menu;
    private boolean gameOver;
    private boolean pause;
    private Rectangle buttonHitbox;
    private boolean restartLevel;
    private boolean running;
    private static final int FINAL_LEVEL = 10;
    private static final double BUTTON_HITBOX_WIDTH_RATIO = 600 / 1920.0;
    private static final double BUTTON_HITBOX_X_RATIO =  660 / 1920.0;
    private static final double BUTTON_HITBOX_HEIGHT_RATIO = 250 / 1080.0;
    private static final double BUTTON_HITBOX_Y_RATIO = 615.55 / 1080.0;

    /**
     * Creates the game thread.
     * @param player Reference to player.
     */
    public GameThread (Player player) {
        level = 1;
        this.player = player;
        currentLevel = new Level(player);
        currentLevel.startLevel(player, level);
        menu = true;
        running = true;
        int boxX = (int)(BUTTON_HITBOX_X_RATIO * MainFrame.WIDTH);
        int boxY = (int)(BUTTON_HITBOX_Y_RATIO * MainFrame.HEIGHT);
        int boxW = (int)(BUTTON_HITBOX_WIDTH_RATIO * MainFrame.WIDTH);
        int boxH = (int)(BUTTON_HITBOX_HEIGHT_RATIO * MainFrame.HEIGHT);
        buttonHitbox = new Rectangle (boxX, boxY, boxW, boxH);

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
     * Checks if the game is over.
     * @return A boolean that represents if the game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the boolean that represents if the game is over.
     * @param gameOver A boolean that represents if the game is over.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Gets the hitbox of the play button on the menu.
     * @return A Rectangle that represents the hitbox of the play button.
     */
    public Rectangle getButtonHitbox() {
        return buttonHitbox;
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
     * Gets the level player is on.
     * @return An int that represents the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Starts the main game loop
     */
    public void run() {
        while (running) {
            // Delay between frames
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Run game if its not  or game over
            if (!menu && !gameOver) {
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
                        if (level + 1 > FINAL_LEVEL) {
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            player.getHitbox().setLocation(0,0);
                            currentLevel.setLevelFinished(false);
                            level = 1;
                            currentLevel.startLevel(player, level);
                            setGameOver(true);
                        } else {
                            currentLevel.startLevel(player, level + 1);
                            level++;
                            currentLevel.setLevelFinished(false);
                        }
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
        }
    }
}
