package simulation.entity;

import simulation.Coordinates;

public class Flower extends Entity {
    public Flower(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public String getSymbol() {
        return "\uD83C\uDF38";
    }
}
