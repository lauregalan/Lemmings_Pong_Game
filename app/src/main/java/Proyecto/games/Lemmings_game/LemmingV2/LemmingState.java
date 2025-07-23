package Proyecto.games.Lemmings_game.LemmingV2;   
//import Proyecto.games.LemmingV2.Lemming;

public interface LemmingState {
    void update(Lemming lemming, double delta);
    void onEnter(Lemming lemming); // opcional
    void onExit(Lemming lemming);  // opcional
}
