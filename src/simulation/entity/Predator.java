package simulation.entity;

import simulation.Coordinates;
import simulation.pathfinding.Pathfinder;
import simulation.world.WorldMap;

import java.util.List;
import java.util.Random;

//Хищник. За ход либо двигается в сторону ближайшего травоядного,либо, если жертва уже рядом, атакует её.

public class Predator extends Creature {
    private static final int STARVATION_THRESHOLD = 20;
    private static final int STARVATION_DAMAGE = 8;
    private static final double REPRODUCE_CHANCE = 0.1;

    private static final Random RANDOM = new Random();

    private final int attackPower;

    public Predator(Coordinates coordinates, int speed, int maxHp, int attackPower) {
        super(coordinates, speed, maxHp);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public void makeMove(WorldMap map) {
        increaseHunger(STARVATION_THRESHOLD, STARVATION_DAMAGE);
        if (!isAlive()) {
            return;
        }

        List<Coordinates> path = Pathfinder.findPathToNearest(map, coordinates, entity -> entity instanceof Herbivore);

        if (path.isEmpty()) {
            map.moveToRandomFreeNeighbour(this);
            return;
        }

        if (path.size() == 1) {
            attack(map, path.get(0));
        } else {
            int steps = Math.min(speed, path.size() - 1);
            if (steps > 0) {
                map.moveEntity(coordinates, path.get(steps - 1));
            }
        }
    }

    private void attack(WorldMap map, Coordinates preyCoordinates) {
        Entity entity = map.getEntityAt(preyCoordinates);
        if (!(entity instanceof Herbivore prey)) {
            return;
        }

        prey.takeDamage(attackPower);
        resetHunger();

        if (!prey.isAlive()) {
            map.removeEntity(preyCoordinates);

            // Механика размножения- успешная охота иногда даёт потомство
            if (RANDOM.nextDouble() < REPRODUCE_CHANCE) {
                map.trySpawnNear(new Predator(coordinates, speed, maxHp, attackPower), coordinates);
            }
        }
    }
}
