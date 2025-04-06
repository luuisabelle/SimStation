package flocking;

import mvc.Model;
import simstation.WorldFactory;

public class FlockingFactory extends WorldFactory {
    public Model makeModel() { return new Flock(); }
    public String getTitle() { return "Flocking Simulation"; }

    @Override
    public String about() {
        return "Flocking Simulation - A simulation of birds flocking behavior";
    }
}