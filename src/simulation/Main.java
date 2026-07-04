package simulation;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Явно задаём UTF-8 для вывода, чтобы кириллица в консоли
        // не превращалась в "???" независимо от локали системы.
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Simulation simulation = new Simulation(20, 12);
        simulation.startSimulation(700);
    }
}
