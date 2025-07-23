package Proyecto.games.Lemmings_game.LemmingV2;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

public class Exit extends Gate {
    private int camX;
    private int x;
    private int y;
    public int savedLemmings = 0;
    private final int width = 100;
    private final int height = 100;
    //private int width;
    //private int height; 
    
    public Exit(int x, int y, int camX) {

        super(x,y,33, 25, 1, "/lemming_exit.png", camX);

        try {
            this.loadAnimations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int camX, int camY) {
        int drawX = x - camX;
        int drawY = y - camY;
        if (frames != null && frames[currentFrameIndex] != null) {
            updateAnimation();
            g.drawImage(frames[currentFrameIndex], drawX, drawY,100 ,80,null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x- camX, y, width, height);
    }

    public boolean checkLemming(Lemming lemming){
        if(getBounds().intersects(lemming.getX() - camX, lemming.getY(),16,16)){
            //System.out.println("llege a la salida");
        }
        return getBounds().intersects(lemming.getX() - camX, lemming.getY(),16,16);
    }

    public void sumLemming(Lemming lemming){
        if (checkLemming(lemming) && !lemming.isSaved()) {

            //TODO: SUMAR 10 PUNTOS AL JUGADOR POR LEMMING.

            savedLemmings++;
            lemming.setSaved(true);
            //Aca tengo que modificar para que se actualice el estado del lemming actual
            //lemming.setStateLemming(LemmingState.EXITED);
            System.out.println("lemming salido: " + savedLemmings);
        }
    }

    public int getSavedLemmings() {
        return savedLemmings;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
