package simulation;

import simulation.entity.Entity;
import simulation.world.WorldMap;

public class ConsoleRenderer {
    public void render(WorldMap map, int turn) {
        StringBuilder builder = new StringBuilder();
        builder.append("Ход: ").append(turn).append('\n');

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Entity entity = map.getEntityAt(new Coordinates(x, y));
                builder.append(entity == null ? '.' : entity.getSymbol());
                builder.append(' ');
            }
            builder.append('\n');
        }

        builder.append("Травоядных: ").append(map.getHerbivores().size());
        builder.append(", хищников: ").append(map.getPredators().size());
        builder.append('\n');

        System.out.println(builder);
    }
}
