package Proyecto.games.Lemmings_game.LemmingV2;

import java.awt.Color;

import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;

public class WalkingState implements LemmingState {
    @Override
    public void update(Lemming l, double delta) {
        int tileX = l.getX() / LemmingConstants.TILE_WIDTH;
        int tileY = l.getY() / LemmingConstants.TILE_HEIGHT;
        
        if (l.isWalkingToRight()) {
            l.setCurrentStateAnimation(LemmingAnimationState.WALKING_RIGHT);
        
            // Pregunto: ¿hay pared justo adelante?
            Color front = l.getLevel().getMap().getTileColor(tileY-2, tileX + 1);
        
            if (!Color.BLACK.equals(front)) {
                // Si hay pared, me doy vuelta
                l.setWalkingToRight(false);
                return;
            }
        
            // Si no hay pared, sigo caminando a la derecha
            l.setX(l.getX() + l.getSpeed());
        } else {
            l.setCurrentStateAnimation(LemmingAnimationState.WALKING_LEFT);
        
            // Pregunto: ¿hay pared adelante a la izquierda?
            Color front = l.getLevel().getMap().getTileColor(tileY-2, tileX - 1);
        
            if (!Color.BLACK.equals(front)) {
                // Si hay pared, me doy vuelta
                l.setWalkingToRight(true);
                return;
            }
        
            // Si no hay pared, sigo caminando a la izquierda
            l.setX(l.getX() - l.getSpeed());
        }
        

        // Lógica de cambio de dirección o caída
        if (shouldFall(l)) {
            l.setState(new FallingState());
            return;
        }

        // Lógica de detección de salida
        if (l.getLevel().getExit().checkLemming(l)) {
            l.setState(new SavedState());
        }
    }

    private boolean shouldFall(Lemming l) {
        int tileX = l.getX() / LemmingConstants.TILE_WIDTH;
        int tileY = l.getY() / LemmingConstants.TILE_HEIGHT;
        return l.getLevel().getMap().getTileColor(tileY + 1, tileX) == Color.BLACK;
    }

    @Override 
    public void onEnter(Lemming l) {
        //nada por ahora
    }
    @Override public void onExit(Lemming l) {
        //nada por ahora
    }
}
