package plague;

import mvc.Model;
import simstation.World;
import simstation.WorldFactory;
import simstation.WorldView;

class PlagueFactory extends WorldFactory {

    @Override
    public String getTitle() {
        return "Plague Simulation";
    }

    @Override
    public World makeModel() {
        return new PlagueSimulation();
    }

    @Override
    public WorldView makeView(Model m) {
        return new PlagueView((PlagueSimulation) m);
    }

    @Override
    public String about() {
        return "Plague Simulation - A virus spread model";
    }
}