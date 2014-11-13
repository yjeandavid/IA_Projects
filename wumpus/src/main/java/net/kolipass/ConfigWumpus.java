package main.java.net.kolipass;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;

public class ConfigWumpus implements Serializable {
    private static final String CONFIGURE_FILE_NAME = "config.properties";
    private static final String MAPS_FOLDER_NAME = "maps";
    public static boolean DEFAULT = false;
    private static transient Log log = new Log();
    // keyboard mappings
    int VK_UP;
    int VK_DOWN;
    int VK_LEFT;
    int VK_RIGHT;
    int VK_SHOT;
    int VK_CLIMB;
    int VK_ENTER;
    int VK_ESC;
    int VK_PAUSE;

    boolean MIDI_PLAY;
    boolean BACKGROUND_MUSIC_PLAY;
    int VOLUME;
    String located = "";

    public int getVK_UP() {
        return VK_UP;
    }

    public void setVK_UP(int VK_UP) {
        this.VK_UP = VK_UP;
    }

    public int getVK_DOWN() {
        return VK_DOWN;
    }

    public void setVK_DOWN(int VK_DOWN) {
        this.VK_DOWN = VK_DOWN;
    }

    public int getVK_LEFT() {
        return VK_LEFT;
    }

    public void setVK_LEFT(int VK_LEFT) {
        this.VK_LEFT = VK_LEFT;
    }

    public int getVK_RIGHT() {
        return VK_RIGHT;
    }

    public void setVK_RIGHT(int VK_RIGHT) {
        this.VK_RIGHT = VK_RIGHT;
    }

    public int getVK_SHOT() {
        return VK_SHOT;
    }

    public void setVK_SHOT(int VK_SHOT) {
        this.VK_SHOT = VK_SHOT;
    }

    public int getVK_CLIMB() {
        return VK_CLIMB;
    }

    public void setVK_CLIMB(int VK_CLIMB) {
        this.VK_CLIMB = VK_CLIMB;
    }

    public int getVK_ENTER() {
        return VK_ENTER;
    }

    public void setVK_ENTER(int VK_ENTER) {
        this.VK_ENTER = VK_ENTER;
    }

    public int getVK_ESC() {
        return VK_ESC;
    }

    public void setVK_ESC(int VK_ESC) {
        this.VK_ESC = VK_ESC;
    }

    public boolean isMIDI_PLAY() {
        return MIDI_PLAY;
    }

    public void setMIDI_PLAY(boolean MIDI_PLAY) {
        this.MIDI_PLAY = MIDI_PLAY;
    }

    public boolean isBACKGROUND_MUSIC_PLAY() {
        return BACKGROUND_MUSIC_PLAY;
    }

    public void setBACKGROUND_MUSIC_PLAY(boolean BACKGROUND_MUSIC_PLAY) {
        this.BACKGROUND_MUSIC_PLAY = BACKGROUND_MUSIC_PLAY;
    }

    public int getVOLUME() {
        return VOLUME;
    }

    public void setVOLUME(int VOLUME) {
        this.VOLUME = VOLUME;
    }

    public ConfigWumpus() {
        defaultConfig();
    }

    public ConfigWumpus load() {
        if (!DEFAULT) {
            if (!new File(getConfigureFileName()).exists()) {
                saveConfig(this, getConfigureFileName());
            } else {
                try {

                    Properties prop = new Properties();
                    prop.load(new FileInputStream(getConfigureFileName()));

                    //  values for keyboard configuration. Comment this part out when keyboard configuration is implemented.
                    VK_UP = Integer.parseInt(prop.getProperty("VK_UP"));
                    VK_DOWN = Integer.parseInt(prop.getProperty("VK_DOWN"));
                    VK_LEFT = Integer.parseInt(prop.getProperty("VK_LEFT"));
                    VK_RIGHT = Integer.parseInt(prop.getProperty("VK_RIGHT"));
                    VK_SHOT = Integer.parseInt(prop.getProperty("VK_SHOT"));
                    VK_CLIMB = Integer.parseInt(prop.getProperty("VK_CLIMB"));
                    VK_ENTER = Integer.parseInt(prop.getProperty("VK_ENTER"));
                    VK_ESC = Integer.parseInt(prop.getProperty("VK_ESC"));
                    VK_PAUSE = Integer.parseInt(prop.getProperty("VK_PAUSE"));


                    MIDI_PLAY = Boolean.parseBoolean(prop.getProperty("MIDI_PLAY"));
                    BACKGROUND_MUSIC_PLAY = Boolean.parseBoolean(prop.getProperty("BACKGROUND_MUSIC_PLAY"));
                    VOLUME = Integer.parseInt(prop.getProperty("VOLUME"));

                    located = prop.getProperty("LOCATED");
                    if (!new File(located).exists()) {
                        File binFolder = findFile(new File(""), "bin");
                        if (binFolder != null && binFolder.exists()) {
                            File rootFolder = new File(binFolder.getAbsolutePath() + "/../");
                            located = rootFolder.getAbsolutePath();
                        }
                    }

                    log.d("Config file read successful");

                } catch (Exception e) {
                    log.printStackTrace(e);
                    log.d("No Config file exists. Initialized default configurations.");
                }
            }
        }
        return this;
    }

    private String getConfigureFileName() {
        return located + CONFIGURE_FILE_NAME;
    }

    private void defaultConfig() {
        VK_UP = KeyEvent.VK_UP;
        VK_DOWN = KeyEvent.VK_DOWN;
        VK_LEFT = KeyEvent.VK_LEFT;
        VK_RIGHT = KeyEvent.VK_RIGHT;
        VK_SHOT = KeyEvent.VK_SHIFT;
        VK_CLIMB = KeyEvent.VK_CONTROL;
        VK_ENTER = KeyEvent.VK_ENTER;
        VK_ESC = KeyEvent.VK_ESCAPE;

        VK_PAUSE = KeyEvent.VK_F1;

        MIDI_PLAY = true;
        BACKGROUND_MUSIC_PLAY = false;
        VOLUME = 100;

        located = findRootFolder();

        if (!new File(getConfigureFileName()).exists()) {
            saveConfig(this, getConfigureFileName());
        }
    }


    public static void saveConfig(ConfigWumpus wumpus, String fileName) {
        try {
            Properties prop = new Properties();
            prop.setProperty("VK_UP", String.valueOf(wumpus.VK_UP));
            prop.setProperty("VK_DOWN", String.valueOf(wumpus.VK_DOWN));
            prop.setProperty("VK_LEFT", String.valueOf(wumpus.VK_LEFT));
            prop.setProperty("VK_RIGHT", String.valueOf(wumpus.VK_RIGHT));
            prop.setProperty("VK_SHOT", String.valueOf(wumpus.VK_SHOT));
            prop.setProperty("VK_CLIMB", String.valueOf(wumpus.VK_CLIMB));
            prop.setProperty("VK_ENTER", String.valueOf(wumpus.VK_ENTER));
            prop.setProperty("VK_ESC", String.valueOf(wumpus.VK_ESC));
            prop.setProperty("VK_PAUSE", String.valueOf(wumpus.VK_PAUSE));
            prop.setProperty("MIDI_PLAY", String.valueOf(wumpus.MIDI_PLAY));
            prop.setProperty("BACKGROUND_MUSIC_PLAY", String.valueOf(wumpus.BACKGROUND_MUSIC_PLAY));
            prop.setProperty("VOLUME", String.valueOf(wumpus.VOLUME));
            prop.setProperty("LOCATED", String.valueOf(wumpus.located));

            //save properties to project root folder
            prop.store(new FileOutputStream(fileName), null);
            log.d("Config file write success.");
        } catch (Exception e) {
            log.d("Config file write failed: \n" + e.getMessage());
            log.printStackTrace(e);
        }
    }

    private String findRootFolder() {
        String folder = "./";
        File binFolder = findFile(new File(folder), "bin");
        if (binFolder != null && binFolder.exists()) {
            File rootFolder = new File(binFolder.getAbsolutePath() + "/../");
            folder = rootFolder.getAbsolutePath();
        }
        return folder;
    }

    public File findMapsFolder() {
        File maps = new File(located + MAPS_FOLDER_NAME);
        if (maps.exists() && maps.isDirectory()) {
            return maps;
        } else {
            File rootFolder = new File("../");
            log.d("rootFolder " + rootFolder.getAbsolutePath());
            return findFile(rootFolder, MAPS_FOLDER_NAME);
        }
    }

    private File findFile(File rootFolder, final String dirName) {
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory() && name.equals(dirName);
            }
        };
        return findFile(rootFolder, fileFilter);
    }

    private File findFile(File rootFolder, FilenameFilter fileFilter) {
        File[] mapsFiles = rootFolder.listFiles(fileFilter);
        if (mapsFiles == null || mapsFiles.length == 0) {
            File[] files = rootFolder.listFiles();
            if (files != null && files.length != 0) {
                for (File file : files) {
                    File foundedFile = findFile(file, fileFilter);
                    if (foundedFile != null) {
                        return foundedFile;
                    }
                }

            }
        } else {
            return mapsFiles[0];
        }
        return null;
    }

    public int getVK_PAUSE() {
        return VK_PAUSE;
    }
}
