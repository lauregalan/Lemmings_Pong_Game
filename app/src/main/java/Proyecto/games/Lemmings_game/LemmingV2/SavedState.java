package Proyecto.games.Lemmings_game.LemmingV2;

public class SavedState implements LemmingState{

    @Override
    public void update(Lemming lemming, double delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnter(Lemming lemming) {
        lemming.getLevel().sumSavedLemmings();
    }

    @Override
    public void onExit(Lemming lemming) {
        // TODO Auto-generated method stub
    }
    
}
