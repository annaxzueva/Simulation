package simulation.entity;

import simulation.Coordinates;

public class Grass extends Entity {
    public Grass(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public String getSymbol() {
        return "\uD83C\uDF3F";
    }
}
