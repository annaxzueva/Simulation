package simulation;



import simulation.action.Action;
import simulation.action.CreatureMoveAction;
import simulation.action.GrowGrassAction;
import simulation.action.PopulateInitAction;
import simulation.action.RemoveDeadCreaturesAction;
import simulation.action.ReplenishPopulationAction;
import simulation.world.WorldMap;

import java.util.List;
import java.util.Random;

public class Simulation {
    private final WorldMap map;
    private final ConsoleRenderer renderer = new ConsoleRenderer();
    private final Random random = new Random();

    private final List<Action> initActions;
    private final List<Action> turnActions;

    private int turnCounter = 0;
    private volatile boolean running = false;

    public Simulation(int width, int height) {
        this.map = new WorldMap(width, height);

        this.initActions = List.of(
                new PopulateInitAction(10, 6, 25, 8, 3)
        );

        this.turnActions = List.of(
                new CreatureMoveAction(),
                new RemoveDeadCreaturesAction(),
                new GrowGrassAction(0.6),
                new ReplenishPopulationAction(4)
        );

        for (Action action : initActions) {
            action.execute(map, random);
        }
    }

    public void nextTurn() {
        turnCounter++;
        for (Action action : turnActions) {
            action.execute(map, random);
        }
        renderer.render(map, turnCounter);
    }

    public void startSimulation(int delayMillis) throws InterruptedException {
        running = true;
        renderer.render(map, turnCounter);
        while (running) {
            Thread.sleep(delayMillis);
            nextTurn();
        }
    }

    public void pauseSimulation() {
        running = false;
    }
}
