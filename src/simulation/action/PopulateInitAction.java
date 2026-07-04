package simulation.action;

import simulation.Coordinates;
import simulation.entity.Entity;
import simulation.entity.Grass;
import simulation.entity.Herbivore;
import simulation.entity.Predator;
import simulation.entity.Flower;
import simulation.entity.Tree;
import simulation.world.WorldMap;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

//Начальная расстановка: камни, деревья, трава, травоядные и хищники разбрасываются по случайным свободным клеткам
public class PopulateInitAction implements Action {
    private final int flowerCount;
    private final int treeCount;
    private final int grassCount;
    private final int herbivoreCount;
    private final int predatorCount;

    public PopulateInitAction(int flowerCount, int treeCount, int grassCount, int herbivoreCount, int predatorCount) {
        this.flowerCount = flowerCount;
        this.treeCount = treeCount;
        this.grassCount = grassCount;
        this.herbivoreCount = herbivoreCount;
        this.predatorCount = predatorCount;
    }

    @Override
    public void execute(WorldMap map, Random random) {
        placeMany(map, flowerCount, Flower::new);
        placeMany(map, treeCount, Tree::new);
        placeMany(map, grassCount, Grass::new);
        placeMany(map, herbivoreCount, coordinates -> new Herbivore(coordinates, 1, 100));
        placeMany(map, predatorCount, coordinates -> new Predator(coordinates, 2, 120, 25));
    }

    private void placeMany(WorldMap map, int count, Function<Coordinates, Entity> factory) {
        for (int i = 0; i < count; i++) {
            Optional<Coordinates> coordinates = map.findRandomEmptyCell();
            coordinates.ifPresent(c -> map.addEntity(factory.apply(c)));
        }
    }
}
