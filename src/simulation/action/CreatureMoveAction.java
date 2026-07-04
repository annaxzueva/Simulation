
package simulation.action;

import simulation.entity.Herbivore;
import simulation.entity.Predator;
import simulation.world.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//Каждый ход в игре животные ходят (Сначала  травоядные, потом хищники)
public class CreatureMoveAction implements Action {
    @Override
    public void execute(WorldMap map, Random random) {
        // Снимаем снимок коллекций: makeMove может менять карту
        // (убирать съеденную траву, добавлять потомство, убивать жертву), поэтому итерироваться напрямую по "живым" коллекциям карты опасно
        List<Herbivore> herbivoresSnapshot = new ArrayList<>(map.getHerbivores());
        for (Herbivore herbivore : herbivoresSnapshot) {
            if (herbivore.isAlive()) {
                herbivore.makeMove(map);
            }
        }

        List<Predator> predatorsSnapshot = new ArrayList<>(map.getPredators());
        for (Predator predator : predatorsSnapshot) {
            if (predator.isAlive()) {
                predator.makeMove(map);
            }
        }
    }
}
