package Proyecto.games.Lemmings_game.Utils;

//import Proyecto.games.Lemmings_game.Model.LemmingModel;
import Proyecto.games.Lemmings_game.LemmingV2.Lemming;

public abstract class AbilityClass{

    private final Ability name;

    public AbilityClass(Ability name){
        this.name = name;
    }

    public void apply(Lemming lemming, double delta){}
    public Ability getName(){ return name; }
}
