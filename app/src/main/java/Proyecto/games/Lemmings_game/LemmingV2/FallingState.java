package Proyecto.games.Lemmings_game.LemmingV2;

import java.awt.Color;

import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;

public class FallingState implements LemmingState {

    private int startFallTileY;

    @Override
    public void onEnter(Lemming lemming) {
        lemming.startFalling();
        startFallTileY = lemming.getTileY();  // guardás desde dónde empezó a caer
        lemming.setAnimationState(LemmingAnimationState.FALLING);
    }

    @Override
    public void update(Lemming lemming, double delta) {
        int tileX = lemming.getTileX();
        int tileY = lemming.getTileY();
        
        // lo hacés caer
        lemming.setY(lemming.getY() + lemming.getSpeed());

        //chequeamos el piso de abajo
        Color tileBelow = lemming.getLevel().getMap().getTileColor(tileY + 1, tileX);

        // Si el color debajo NO es negro, es porque hay piso
        if (!Color.BLACK.equals(tileBelow)) {
            int fallDistance = tileY - startFallTileY;
        
            if (fallDistance > 20 && !lemming.hasUmbrella()) {
                lemming.setState(new DeadState());
            } else {
                lemming.setState(new WalkingState());
            }
        }
        
    }

    @Override
    public void onExit(Lemming lemming) {
        lemming.stopFalling();
    }
}
