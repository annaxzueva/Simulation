package simulation.world;

import simulation.Coordinates;
import simulation.entity.Creature;
import simulation.entity.Entity;
import simulation.entity.Herbivore;
import simulation.entity.Predator;

import java.util.*;

/**
 * Карта мира. Снаружи доступны только именованные операции
 * (addEntity, removeEntity, moveEntity, ...) - внутреннее устройство
 * (какая именно коллекция используется для хранения) наружу не "протекает".
 */
public class WorldMap {
    private final int width;
    private final int height;

    private final Map<Coordinates, Entity> entitiesByCoordinates = new HashMap<>();

    // Параллельные типизированные коллекции - позволяют быстро проитерироваться
    // по травоядным или хищникам отдельно, не проверяя instanceof для всех сущностей карты.
    private final Set<Herbivore> herbivores = new LinkedHashSet<>();
    private final Set<Predator> predators = new LinkedHashSet<>();

    private final Random random = new Random();

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInBounds(Coordinates coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    public boolean isCellEmpty(Coordinates coordinates) {
        return isInBounds(coordinates) && !entitiesByCoordinates.containsKey(coordinates);
    }

    public Entity getEntityAt(Coordinates coordinates) {
        return entitiesByCoordinates.get(coordinates);
    }

    public boolean addEntity(Entity entity) {
        Coordinates coordinates = entity.getCoordinates();
        if (!isInBounds(coordinates) || !isCellEmpty(coordinates)) {
            return false;
        }
        entitiesByCoordinates.put(coordinates, entity);
        registerInTypedCollection(entity);
        return true;
    }

    public void removeEntity(Coordinates coordinates) {
        Entity removed = entitiesByCoordinates.remove(coordinates);
        if (removed != null) {
            unregisterFromTypedCollection(removed);
        }
    }

    public void moveEntity(Coordinates from, Coordinates to) {
        if (!isInBounds(to) || !isCellEmpty(to)) {
            return;
        }
        Entity entity = entitiesByCoordinates.remove(from);
        if (entity == null) {
            return;
        }
        entitiesByCoordinates.put(to, entity);
        entity.setCoordinates(to);
    }

    public void moveToRandomFreeNeighbour(Creature creature) {
        Coordinates current = creature.getCoordinates();
        List<Coordinates> neighbours = new ArrayList<>(List.of(
                current.shift(1, 0), current.shift(-1, 0),
                current.shift(0, 1), current.shift(0, -1)
        ));
        Collections.shuffle(neighbours, random);

        for (Coordinates neighbour : neighbours) {
            if (isCellEmpty(neighbour)) {
                moveEntity(current, neighbour);
                return;
            }
        }
    }

    public void trySpawnNear(Entity entity, Coordinates near) {
        List<Coordinates> neighbours = new ArrayList<>(List.of(
                near.shift(1, 0), near.shift(-1, 0),
                near.shift(0, 1), near.shift(0, -1)
        ));
        Collections.shuffle(neighbours, random);

        for (Coordinates neighbour : neighbours) {
            if (isCellEmpty(neighbour)) {
                entity.setCoordinates(neighbour);
                addEntity(entity);
                return;
            }
        }
    }

    public Optional<Coordinates> findRandomEmptyCell() {
        int attempts = width * height * 2;
        for (int i = 0; i < attempts; i++) {
            Coordinates coordinates = new Coordinates(random.nextInt(width), random.nextInt(height));
            if (isCellEmpty(coordinates)) {
                return Optional.of(coordinates);
            }
        }
        return Optional.empty();
    }

    public Set<Herbivore> getHerbivores() {
        return Collections.unmodifiableSet(herbivores);
    }

    public Set<Predator> getPredators() {
        return Collections.unmodifiableSet(predators);
    }

    private void registerInTypedCollection(Entity entity) {
        if (entity instanceof Herbivore herbivore) {
            herbivores.add(herbivore);
        } else if (entity instanceof Predator predator) {
            predators.add(predator);
        }
    }

    private void unregisterFromTypedCollection(Entity entity) {
        if (entity instanceof Herbivore herbivore) {
            herbivores.remove(herbivore);
        } else if (entity instanceof Predator predator) {
            predators.remove(predator);
        }
    }
}
