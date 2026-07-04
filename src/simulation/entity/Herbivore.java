package simulation.entity;

import simulation.Coordinates;
import simulation.pathfinding.Pathfinder;
import simulation.world.WorldMap;

import java.util.List;
import java.util.Random;

//Травоядное. За ход либо двигается в сторону ближайшей травы,либо, если трава уже рядом, съедает её

public class Herbivore extends Creature {
    private static final int STARVATION_THRESHOLD = 15; // сколько ходов подряд без еды терпит существо
    private static final int STARVATION_DAMAGE = 5;
    private static final int EAT_HEAL_AMOUNT = 40;
    private static final double REPRODUCE_CHANCE = 0.15;

    private static final Random RANDOM = new Random();

    public Herbivore(Coordinates coordinates, int speed, int maxHp) {
        super(coordinates, speed, maxHp);
    }

    @Override
    public String getSymbol() {
        return "H";
    }

    @Override
    public void makeMove(WorldMap map) {
        increaseHunger(STARVATION_THRESHOLD, STARVATION_DAMAGE);
        if (!isAlive()) {
            return;
        }

        List<Coordinates> path = Pathfinder.findPathToNearest(map, coordinates, entity -> entity instanceof Grass);

        if (path.isEmpty()) {
            map.moveToRandomFreeNeighbour(this);
            return;
        }

        if (path.size() == 1) {
            eat(map, path.get(0));
        } else {
            moveAlongPath(map, path);
        }
    }

    private void eat(WorldMap map, Coordinates grassCoordinates) {
        map.removeEntity(grassCoordinates);
        heal(EAT_HEAL_AMOUNT);
        resetHunger();

        // Механика размножения для травоядных: если HP полное иногда даёт потомство рядом с собой
        if (hp == maxHp && RANDOM.nextDouble() < REPRODUCE_CHANCE) {
            map.trySpawnNear(new Herbivore(coordinates, speed, maxHp), coordinates);
        }
    }

    private void moveAlongPath(WorldMap map, List<Coordinates> path) {
        int steps = Math.min(speed, path.size() - 1);
        if (steps <= 0) {
            return;
        }
        Coordinates destination = path.get(steps - 1);
        map.moveEntity(coordinates, destination);
    }
}
