package simulation.entity;

import simulation.Coordinates;

//Корневой класс для всех существ и объектов, находящихся в мире.
//Инкапсулирует координатыб наружу они доступны только через геттер/сеттер,а не как открытое поле

public abstract class Entity {
    protected Coordinates coordinates;

    protected Entity(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public abstract String getSymbol();
}
