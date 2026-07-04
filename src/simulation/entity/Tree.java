package simulation.entity;

import simulation.Coordinates;

public class Tree extends Entity {
    public Tree(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public String getSymbol() {
        return "\uD83C\uDF33";
    }
}
