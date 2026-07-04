package simulation.pathfinding;

import simulation.Coordinates;
import simulation.entity.Entity;
import simulation.world.WorldMap;

import java.util.*;
        import java.util.function.Predicate;

/**
 * Поиск пути в ширину (BFS), написан "с нуля".
 * Общий класс для всех существ: и травоядное (ищет траву), и хищник
 * (ищет травоядное) пользуются одним и тем же алгоритмом, отличаясь
 * только предикатом "что считать целью" - это убирает дублирование
 * кода поиска пути между классами существ.
 *
 * Проходимой считается пустая клетка. Клетка-цель считается достижимой,
 * даже если формально занята (например, травой), - именно её мы ищем.
 */
public final class Pathfinder {

    private Pathfinder() {
    }

    /**
     * @return путь от start (не включая) до ближайшей клетки-цели (включая её).
     *         Пустой список, если цель не найдена.
     */
    public static List<Coordinates> findPathToNearest(WorldMap map, Coordinates start, Predicate<Entity> isTarget) {
        Queue<Coordinates> queue = new ArrayDeque<>();
        Map<Coordinates, Coordinates> cameFrom = new HashMap<>();
        Set<Coordinates> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            for (Coordinates neighbour : neighboursOf(current)) {
                if (!map.isInBounds(neighbour) || visited.contains(neighbour)) {
                    continue;
                }

                Entity entityAtNeighbour = map.getEntityAt(neighbour);
                boolean isTargetCell = entityAtNeighbour != null && isTarget.test(entityAtNeighbour);

                if (isTargetCell) {
                    cameFrom.put(neighbour, current);
                    return buildPath(cameFrom, start, neighbour);
                }

                boolean isPassableCell = entityAtNeighbour == null;
                if (isPassableCell) {
                    visited.add(neighbour);
                    cameFrom.put(neighbour, current);
                    queue.add(neighbour);
                }
            }
        }

        return Collections.emptyList();
    }

    private static List<Coordinates> neighboursOf(Coordinates coordinates) {
        return List.of(
                coordinates.shift(1, 0),
                coordinates.shift(-1, 0),
                coordinates.shift(0, 1),
                coordinates.shift(0, -1)
        );
    }

    private static List<Coordinates> buildPath(Map<Coordinates, Coordinates> cameFrom, Coordinates start, Coordinates target) {
        LinkedList<Coordinates> path = new LinkedList<>();
        path.addFirst(target);

        Coordinates step = cameFrom.get(target);
        while (step != null && !step.equals(start)) {
            path.addFirst(step);
            step = cameFrom.get(step);
        }

        return path;
    }
}
