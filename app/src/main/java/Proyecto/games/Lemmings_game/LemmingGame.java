// Refactor de Lemmings.java
// Usa GameState para manejar el estado del juego, incluyendo pantalla de PreLevel

package Proyecto.games.Lemmings_game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;

import Proyecto.games.Lemmings_game.Model.GameSettingsModel;
import Proyecto.games.Lemmings_game.Utils.Ability;
import Proyecto.games.Lemmings_game.Utils.GameState;
import Proyecto.games.Lemmings_game.Utils.ScoreDatabase;
import Proyecto.games.Lemmings_game.View.GameMenuView;
import Proyecto.games.Lemmings_game.View.GamePauseView;
import Proyecto.games.Lemmings_game.View.GameScoreView;
import Proyecto.games.Lemmings_game.View.GameSettingsView;
import Proyecto.games.Lemmings_game.View.GameWinView;
import Proyecto.games.Pong_game.Model.SettingsModel;
import Proyecto.utils.SoundPlayer;
import Proyecto.games.Lemmings_game.LemmingV2.*;
import Proyecto.games.Lemmings_game.Levels.LoadFromFiles;

public class LemmingGame extends JGame {
    private GameMenuView gameMenu;
    private GamePauseView gamePauseView;
    private GameSettingsView gameSettingsView;
    private GameScoreView gameScoreView;
    private GameWinView gameWinView;
    private Stock stock;
    private List<Level> levels = new ArrayList<>();
    private int currentLevel = 0;
    private Spawn spawn;
    private Exit exit;
    private Cursor cursor;
    private LoadFromFiles loadFromFiles; 
    private SettingsModel.Settings settings, backupSettings;
    private static boolean fullScreen = false;
    private boolean musicOff = true;

    private GameState gameState = GameState.MENU;

    private int screenWidth = getWidth();
    private int screenHeight = getHeight();

    public LemmingGame(String title, int width, int height) {
        super(title, width, height);
    }

    public static void main(String[] args) {
        LemmingGame game = new LemmingGame("Lemmings", fullScreen ? 1366 : 800, fullScreen ? 768 : 600);
        game.run(1.0 / 60.0);
    }

    @Override
    public void gameStartup() {
        getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                SoundPlayer.stopSound();
            }
        });

        ScoreDatabase.createTable();
        initSettings();
        backupSettings();
        playTrack();

        if (fullScreen) setFullScreen();

        stock = new Stock(
            new HashMap<>(Map.of(
                Ability.DIGGER, 5,
                Ability.CLIMB, 0,
                Ability.STOP, 3,
                Ability.UMBRELLA, 0
            ))
        );

        try {
            loadLevels();
            cursor = new Cursor(stock, getMouse(), screenWidth, screenHeight, fullScreen);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        gameMenu = new GameMenuView(getWidth(), getHeight(), this);
        gamePauseView = new GamePauseView(screenWidth, screenHeight);
        gameSettingsView = new GameSettingsView(screenWidth, screenHeight, this);
        gameScoreView = new GameScoreView(screenWidth, screenHeight, this);
        gameWinView = new GameWinView(screenWidth, screenHeight);
    }

    private void initSettings() {
        settings = SettingsModel.loadSettings();
        musicOff = settings.musicOff;
        fullScreen = settings.fullScreen;
    }

    private void backupSettings() {
        backupSettings = new SettingsModel.Settings();
        backupSettings.fullScreen = fullScreen;
        backupSettings.musicOff = musicOff;
    }

    private void playTrack() {
        if (!musicOff) SoundPlayer.playSound("app/src/main/resources/cantinadelpela.wav");
    }

    @Override
    public void gameUpdate(double delta) {
        switch (gameState) {
            case MENU:
                gameMenu.update(delta);
                if (detectPlay(getMouse()) || detectPlay(getKeyboard())) gameState = GameState.PRELEVEL;
                if (detectSetting(getMouse()) || getKeyboard().isKeyPressed(KeyEvent.VK_C)) gameState = GameState.SETTINGS;
                if (detectScore(getMouse()) || getKeyboard().isKeyPressed(KeyEvent.VK_S)) gameState = GameState.SCORES;
                break;
            case SETTINGS:
            case SCORES:
                break;
            case PRELEVEL:
                if (getKeyboard().isKeyPressed(KeyEvent.VK_ENTER) ) 
                gameState = GameState.PLAYING;
                break;
            case END_SCREEN:
                if (getKeyboard().isKeyPressed(KeyEvent.VK_ENTER)&& !levels.get(currentLevel).isLevelWon()){
                    levels.get(currentLevel).reset();
                    gameState = GameState.PLAYING;
                } else if (getKeyboard().isKeyPressed(KeyEvent.VK_ENTER)&& levels.get(currentLevel).isLevelWon()) {
                    nextLevel();
                    gameState = GameState.PLAYING;
                }
            case PLAYING:
                if (getKeyboard().isKeyPressed(KeyEvent.VK_P)) gameState = GameState.PAUSED;
                Level current = levels.get(currentLevel);
                cursor.setCurrentLemmings(current.getLemmings());
                cursor.setCamX(current.getCamX());
                current.update(delta);
                cursor.update();
                if (current.isLevelFinished()) {
                    gameState = GameState.END_SCREEN;
                }
                break;
            case PAUSED:
                if (getKeyboard().isKeyPressed(KeyEvent.VK_M)) gameState = GameState.PLAYING;
                if (getKeyboard().isKeyPressed(KeyEvent.VK_ENTER)) gameState = GameState.MENU;
                break;
            case WIN_SCREEN:
                if (getKeyboard().isKeyPressed(KeyEvent.VK_ENTER)) gameState = GameState.MENU;
                break;
            default:
                break;
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        switch (gameState) {
            case MENU:
                gameMenu.drawmenu(g);
                break;
            case SETTINGS:
                gameSettingsView.drawmenu(g);
                break;
            case SCORES:
                gameScoreView.draw(g);
                break;
            case PRELEVEL:
                levels.get(currentLevel).drawPreLevelScreen(g);
                break;
            case PLAYING:
                levels.get(currentLevel).drawLevel(g, screenWidth, screenHeight);
                //cursor.draw(g);
                break;
            case PAUSED:
                levels.get(currentLevel).drawLevel(g, screenWidth, screenHeight);
                gamePauseView.draw(g);
                break;
            case WIN_SCREEN:
                gameWinView.draw(g);
                break;
            case END_SCREEN:
                levels.get(currentLevel).drawEndScreen(g);
                break;
            default:
                break;
        }
    }

    private void nextLevel() {
        if (currentLevel < levels.size() - 1) {
            currentLevel++;
            gameState = GameState.PRELEVEL;
        } else {
            gameState = GameState.WIN_SCREEN;
        }
    }

    private void setFullScreen() {
        JFrame frame = getFrame();
        frame.dispose();
        frame.setUndecorated(true);
        frame.setResizable(false);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
    }
    /*
    private void loadLevels() throws IOException {
        //spawn = new Spawn(600, 100, 300);
        //exit = new Exit(1000, 300, 300);
        //Mapp map = new Mapp(1, 300, null, 700, 100, spawn, exit);
        //levels.add(new Level(map, stock, 4, 100, currentLevel, "Just digging", exit, 600, 110));
        loadFromFiles = new LoadFromFiles();
        levels.add(loadFromFiles.loadLevelFromFile("src/Proyecto/games/Lemmings_game/Levels/1.txt"));
    } */
    private void loadLevels() throws IOException {
        loadFromFiles = new LoadFromFiles();
        File folder = new File("src/Proyecto/games/Lemmings_game/Levels");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
    
        if (files != null) {
            for (File file : files) {
                Level level = loadFromFiles.loadLevelFromFile(file.getPath());
                levels.add(level);
            }
        } else {
            System.out.println("No se encontraron archivos en la carpeta de niveles.");
        }
    }
    
    public boolean detectPlay(Mouse m) {
        return mouseTracker(screenWidth / 2 - 100, 300, 200, 60, m);
    }

    public boolean detectPlay(Keyboard k) {
        return k.isKeyPressed(KeyEvent.VK_ENTER);
    }

    public boolean detectSetting(Mouse m) {
        return mouseTracker(screenWidth - 250, screenHeight - 110, 150, 80, m);
    }

    public boolean detectScore(Mouse m) {
        return mouseTracker(250, screenHeight - 110, 150, 80, m);
    }

    public boolean mouseTracker(int x, int y, int width, int height, Mouse m) {
        int mx = m.getX();
        int my = m.getY();
        return mx >= x && mx <= x + width && my >= y && my <= y + height && m.isLeftButtonPressed();
    }

    @Override
    public void gameShutdown() {
        GameSettingsModel.saveSettings(musicOff, fullScreen);
    }

    public boolean getIsinMenu() {
        return gameState == GameState.MENU;
    }

    public boolean getIsinScore() {
        return gameState == GameState.SCORES;
    }

    public boolean getIsinsettings() {
        return gameState == GameState.SETTINGS;
    }
}
