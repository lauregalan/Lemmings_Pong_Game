package Proyecto.games.Lemmings_game.LemmingV2;

import Proyecto.games.Lemmings_game.Utils.Ability;
import Proyecto.games.Lemmings_game.Utils.AbilityClass;
import Proyecto.games.Lemmings_game.Utils.LemmingAnimationState;
//import Proyecto.games.Lemmings_game.Utils.LemmingState;

import java.awt.Color;

public class WallAbility extends AbilityClass {

    public WallAbility(){
        super(Ability.STOP);
    }


    @Override
    public void apply(Lemming lemming, double delta) {
        int tileX = (lemming.getX() + lemming.getLevel().getCamX()) / 8;
        int tileY = (lemming.getY()) / 8;

        lemming.setSpeed(0);

        //System.out.println(lemming.currentStateAnimation);
        //System.out.println(lemming.state);

        if(!lemming.getState().equals(new ExplodingState()) && !LemmingAnimationState.NUKE.equals(lemming.getCurrentStateAnimation())){

            lemming.setState(new WaitingState()); // cambia la lógica
            lemming.setCurrentStateAnimation(LemmingAnimationState.STOPING); // cambia cómo se ve
            ;

            //aca le seteo para que no pasen jajaja
            lemming.getLevel().getMap().getMapTiles()[tileY][tileX].setTileColor(Color.GREEN);
            lemming.getLevel().getMap().getMapTiles()[tileY - 1][tileX].setTileColor(Color.GREEN);
            lemming.getLevel().getMap().getMapTiles()[tileY - 2][tileX].setTileColor(Color.GREEN);
            lemming.getLevel().getMap().getMapTiles()[tileY - 3][tileX].setTileColor(Color.GREEN);
            lemming.getLevel().getMap().getMapTiles()[tileY - 4][tileX].setTileColor(Color.GREEN);//aca chusmear pq no me acuerdo que estado de animacion iba
        }else if(!lemming.getState().equals(new ExplodingState())  && LemmingAnimationState.NUKE.equals(lemming.getCurrentStateAnimation())){
            lemming.setCurrentStateAnimation(LemmingAnimationState.NUKE);
        }
        else {
            createExplosion(lemming.getLevel().getMap().getMapTiles(), tileX,tileY, 7);
            lemming.setState(new DeadState());
            lemming.clearAbility();
        }


    }

    private void createExplosion(Tile[][] tiles, int tileX, int tileY, int radius){

        for (int dy = 0; dy <= radius; dy++) { // solo hacia abajo
            int y = tileY + dy;
            int dxMax = (int) Math.sqrt(radius * radius - dy * dy); // círculo

            for (int dx = -dxMax; dx <= dxMax; dx++) {
                int x = tileX + dx;

                if (y >= 0 && y < tiles.length && x >= 0 && x < tiles[0].length) {
                    tiles[y][x].setTileColor(Color.BLACK);
                }
            }
        }

    }

}
