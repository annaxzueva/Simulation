package simulation.action;

import simulation.world.WorldMap;

import java.util.Random;

//simulation работает со списком action через полиморфизм
public interface Action {
    void execute(WorldMap map, Random random);
}
