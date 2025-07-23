package Proyecto.games.Lemmings_game.LemmingV2;

import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;

public class WaitingState implements LemmingState {
    

    @Override
    public void update(Lemming lemming, double delta) {
        lemming.setCurrentStateAnimation(LemmingAnimationState.STOPING);
        lemming.setSpeed(0);
    }

    @Override
    public void onEnter(Lemming lemming) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onExit(Lemming lemming) {
        // TODO Auto-generated method stub
    }
    
}
