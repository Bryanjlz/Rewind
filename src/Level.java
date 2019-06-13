import java.awt.Point;
import java.io.File;
import java.util.Scanner;

/**
 * Level
 * A level for the game
 * @author Bryan Zhang
 */
public class Level {
    private Terrain[][] terrain;
    private MyArrayList<Crate> crates;
    private MyArrayList<MovingWall> movingWalls;
    private MyArrayList<Key> keys;
    private boolean levelFinished;
    private Player player;

    /**
     * Constructor for a level.
     * @param player Used to be able to reference the player.
     */
    public Level (Player player) {
        terrain = new Terrain[18][32];
        crates = new MyArrayList<Crate>();
        movingWalls = new MyArrayList<MovingWall>();
        keys = new MyArrayList<Key>();
        this.levelFinished = false;
        this.player = player;
    }

    /**
     * Loads the level file and starts the level.
     * @param player Used to reference the player.
     * @param level The level number to load.
     */
    public void startLevel (Player player, int level) {
        try {
            Thread.sleep(450);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadFile("levels/level" + level + ".txt");
        this.player = player;
        player.setKeys(keys);
        player.setTerrain(terrain);
        player.setCrates(crates);
        player.setMovingWalls(movingWalls);
        player.setVel(new MyVector(0,0));
        player.setAcc(new MyVector(0, 0));
        player.setPreviousStateQueue(new MyQueue<Player>());
        player.setDead(false);
        player.setHoldingCrate(false);
        player.startLevel();
    }

    /**
     * Loads a level from a file.
     * @param path The String path to the file.
     */
    private void loadFile (String path) {
        try {
            // Set up scanner to read file
            File file = new File(path);
            Scanner fileReader = new Scanner(file);

            // Get size of map and setup grid to screen ratio
            int terrainWidth = fileReader.nextInt();
            int terrainHeight = fileReader.nextInt();
            MainFrame.gridScreenRatio = MainFrame.HEIGHT / terrainHeight;
            player.getHitbox().setSize(MainFrame.gridScreenRatio, MainFrame.gridScreenRatio);

            // Initialize terrain
            MyArrayList<Button> buttons = new MyArrayList<Button>();
            terrain = new Terrain[terrainHeight][terrainWidth];
            crates = new MyArrayList<>();
            keys.clear();
            for (int i = 0; i < terrainHeight; i++) {
                for (int j = 0; j < terrainWidth; j++) {
                    String terrainString = fileReader.next();
                    // Add wall
                    if (terrainString.equals("w")) {
                        terrain[i][j] = new Wall(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);

                    // Add exit
                    } else if (terrainString.equals("e")) {
                        Exit exit = new Exit(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio);
                        exit.setCurrentLevel(this);
                        terrain[i][j] = exit;

                    // Add crate
                    } else if (terrainString.equals("c")) {
                        crates.add(new Crate (j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, terrain, crates, movingWalls, player));

                    // Move player to specified location
                    } else if (terrainString.equals("p")) {
                        player.getHitbox().setLocation(new Point(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio));

                    // Add key with the specified id
                    } else if (terrainString.charAt(0) == 'k') {
                        int id = Integer.parseInt(terrainString.substring(1));
                        keys.add(new Key(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, id));

                    // Add door with the specified id
                    } else if (terrainString.charAt(0) == 'd') {
                        int id = Integer.parseInt(terrainString.substring(1));
                        terrain[i][j] = new Door(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, id);

                    /* Adds moving walls with the specified id, tens digit represents connection to what button, ones
                       digit specifies which path to take, which is given later in the file
                       */
                    } else if (terrainString.charAt(0) == 'm') {
                        int id = Integer.parseInt(terrainString.substring(1));
                        movingWalls.add(new MovingWall(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, id, player, crates));

                    // Adds a button with the specified id
                    } else if (terrainString.charAt(0) == 'b') {
                        int id = Integer.parseInt(terrainString.substring(1));
                        terrain[i][j] = new Button(j * MainFrame.gridScreenRatio, i * MainFrame.gridScreenRatio, id);
                        buttons.add((Button)terrain[i][j]);
                    }
                }
            }

            while (fileReader.hasNext()) {
                int id = fileReader.nextInt();
                Point[] wallPath = new Point[2];
                for (int i = 0; i < 2; i++) {
                    int x = fileReader.nextInt();
                    int y = fileReader.nextInt();
                    wallPath[i] = new Point(x, y);
                }
                for (int i = 0; i < movingWalls.size(); i++) {
                    if (movingWalls.get(i).getId() == id) {
                        movingWalls.get(i).setPath(wallPath);
                        for (int j = 0; j < buttons.size(); j++) {
                            if (id / 10 == buttons.get(j).getId()) {
                                movingWalls.get(i).setButton(buttons.get(j));
                            }
                        }
                    }
                }
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MyArrayList<MovingWall> getMovingWalls() {
        return movingWalls;
    }

    /**
     * Gets the crates of the level.
     * @return The ArrayList of crates.
     */
    public MyArrayList<Crate> getCrates() {
        return crates;
    }

    /**
     * gets the terrain of the level.
     * @return The 2D array of terrain.
     */
    public Terrain[][] getTerrain() {
        return terrain;
    }

    /**
     * Sets the boolean that represents if the level is finished.
     * @param levelFinished A boolean that represents if the level is finished.
     */
    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }

    /**
     * Checks if the level is finished.
     * @return A boolean that represents if the level is finished.
     */
    public boolean isLevelFinished() {
        return levelFinished;
    }
}
