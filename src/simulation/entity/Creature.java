package simulation.entity;

import simulation.Coordinates;
import simulation.world.WorldMap;

//Существо: имеет скорость (сколько клеток может пройти за один ход), запас HP и счётчик ходов которые оно может пройти подряд без еды/охоты.
// Общая логика для Herbivore и Predator (наследование)


//
public abstract class Creature extends Entity {
    protected final int speed;
    protected final int maxHp;
    protected int hp;
    protected int hungerTurns;

    protected Creature(Coordinates coordinates, int speed, int maxHp) {
        super(coordinates);
        this.speed = speed;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.hungerTurns = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    protected void resetHunger() {
        hungerTurns = 0;
    }

    // Механика голода: если существо слишком долго не ело илии не охотилось,
     //оно начинает терять HP. Вызывается каждый ход перед принятием решения


    protected void increaseHunger(int starvationThreshold, int starvationDamage) {
        hungerTurns++;
        if (hungerTurns > starvationThreshold) {
            takeDamage(starvationDamage);
        }
    }

    // Полиморфизм: у каждого вида существ свой алгоритм поведения на ходу
    public abstract void makeMove(WorldMap map);
}
