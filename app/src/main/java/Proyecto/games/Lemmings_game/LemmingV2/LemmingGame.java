package Proyecto.games.Lemmings_game.LemmingV2;

import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.*;

import com.entropyinteractive.JGame;



public class LemmingGame extends JGame {

    private static boolean fullScreen = false;

    public LemmingGame(String title, int width, int height) {
        super(title, width, height);
    }   

    public static void main(String[] args) {


        //configReader();

        System.out.println(fullScreen);

        if(!fullScreen){
            LemmingGame game = new LemmingGame("Lemmings", 800, 600);
            game.run(1.0 / 60.0); // 60 FPS
        }else{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            int width = screenSize.width;
            int height = screenSize.height;

            LemmingGame game = new LemmingGame("Lemmings", width, height);
            game.run(1.0 / 60.0); // 60 FPS
        }    
    }

    @Override
    public void gameStartup() {
    }

    @Override
    public void gameUpdate(double delta) {

    }

    @Override
    public void gameDraw(Graphics2D g) {

    }

    @Override
    public void gameShutdown() {
    }

}
