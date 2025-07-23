package Proyecto.games.Lemmings_game.LemmingV2;

import Proyecto.games.Lemmings_game.Utils.Ability;
import Proyecto.games.Lemmings_game.Utils.AbilityClass;
import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;

import java.awt.*;

public class UmbrellaAbility extends AbilityClass {

    boolean isUsingUmbrella = false;

    public UmbrellaAbility(){
        super(Ability.UMBRELLA);
    }

    @Override
    public void apply(Lemming lemming, double delta) {

        int tileX = (lemming.getX() + lemming.getLevel().getCamX()) / 8;
        int tileY = (lemming.getY()) / 8;
        boolean goingRight = lemming.isWalkingToRight();

        LemmingAnimationState umbrellaState = goingRight
                ? LemmingAnimationState.UMBRELLA_RIGHT
                : LemmingAnimationState.UMBRELLA_LEFT;

        if(lemming.isGoingToDieFromFall() || isUsingUmbrella){

            isUsingUmbrella = true;

            lemming.setCurrentStateAnimation(umbrellaState);
            lemming.setY(lemming.getY() + 1);

            if(!Color.BLACK.equals(lemming.getLevel().getMap().getMapTiles()[tileY + 1][tileX].getColor())) lemming.setAbility(null);
        }
        else {
            lemming.clearAbility();
        }
    }

}
