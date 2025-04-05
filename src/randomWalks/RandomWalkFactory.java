package randomWalks;

import mvc.Model;
import simstation.WorldFactory;

class RandomWalkFactory extends WorldFactory {
    public Model makeModel() { return new RandomWalkSimulation(); }
    public String getTitle() { return "Random Walks";}
}
