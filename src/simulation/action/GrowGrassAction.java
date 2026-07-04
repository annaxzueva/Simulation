package simulation.action;

import simulation.Coordinates;
import simulation.entity.Grass;
import simulation.world.WorldMap;

import java.util.Optional;
import java.util.Random;


//Каждый ход с заданной вероятностью добавляет на карту новую траву, чтобы у травоядных всегда оставался шанс найти еду.
public class GrowGrassAction implements Action {
    private final double growChance;

    public GrowGrassAction(double growChance) {
        this.growChance = growChance;
    }

    @Override
    public void execute(WorldMap map, Random random) {
        if (random.nextDouble() < growChance) {
            Optional<Coordinates> coordinates = map.findRandomEmptyCell();
            coordinates.ifPresent(c -> map.addEntity(new Grass(c)));
        }
    }
}
