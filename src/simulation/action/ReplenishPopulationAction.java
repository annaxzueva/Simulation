package simulation.action;

import simulation.Coordinates;
import simulation.entity.Herbivore;
import simulation.world.WorldMap;

import java.util.Optional;
import java.util.Random;



//Если травоядных стало меньше минимума - добавляем новых
public class ReplenishPopulationAction implements Action {
    private final int minHerbivores;

    public ReplenishPopulationAction(int minHerbivores) {
        this.minHerbivores = minHerbivores;
    }

    @Override
    public void execute(WorldMap map, Random random) {
        while (map.getHerbivores().size() < minHerbivores) {
            Optional<Coordinates> coordinates = map.findRandomEmptyCell();
            if (coordinates.isEmpty()) {
                break;
            }
            map.addEntity(new Herbivore(coordinates.get(), 1, 100));
        }
    }
}
