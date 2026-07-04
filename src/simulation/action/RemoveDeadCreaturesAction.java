package simulation.action;

import simulation.Coordinates;
import simulation.entity.Herbivore;
import simulation.entity.Predator;
import simulation.world.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//Существ со здоровьем <= 0 убираем. И хищников умерших во время атаки
public class RemoveDeadCreaturesAction implements Action {
    @Override
    public void execute(WorldMap map, Random random) {
        List<Coordinates> dead = new ArrayList<>();

        for (Herbivore herbivore : map.getHerbivores()) {
            if (!herbivore.isAlive()) {
                dead.add(herbivore.getCoordinates());
            }
        }
        for (Predator predator : map.getPredators()) {
            if (!predator.isAlive()) {
                dead.add(predator.getCoordinates());
            }
        }

        for (Coordinates coordinates : dead) {
            map.removeEntity(coordinates);
        }
    }
}
