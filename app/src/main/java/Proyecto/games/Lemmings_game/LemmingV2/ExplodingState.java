package Proyecto.games.Lemmings_game.LemmingV2;

import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;

public class ExplodingState implements LemmingState {

    private int tickCounter = 0;
    private final int explosionDuration = 60; // duración de la explosión en ticks

    @Override
    public void update(Lemming lemming, double delta) {
        tickCounter++;

        // Mientras explota, mostrás la animación de explosión
        lemming.setCurrentStateAnimation(LemmingAnimationState.NUKE);

        // Cuando termina la animación, lo sacás del juego
        if (tickCounter >= explosionDuration) {
            lemming.setState(new DeadState());
        }
    }

    @Override
    public void onEnter(Lemming lemming) {
        tickCounter = 0;
        lemming.setCurrentStateAnimation(LemmingAnimationState.NUKE);
    }

    @Override
    public void onExit(Lemming lemming) {
        // Por lo general nada, porque lo borrás antes
    }
}
