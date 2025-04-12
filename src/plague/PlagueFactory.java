package plague;

import mvc.Model;
import simstation.World;
import simstation.WorldFactory;
import simstation.WorldView;

class PlagueFactory extends WorldFactory {
    // Keep track of the current model
    private static PlagueSimulation currentModel = null;

    @Override
    public String getTitle() {
        return "Plague Simulation";
    }

    @Override
    public World makeModel() {
        // Create a new model
        PlagueSimulation model = new PlagueSimulation();

        // Apply all settings from the central settings manager
        Setting.getInstance().applyToModel(model);
        currentModel = model;
        return model;
    }

    @Override
    public WorldView makeView(Model m) {
        return new PlagueView((PlagueSimulation) m);
    }

    @Override
    public String about() {
        return "Plague Simulation - A virus spread model";
    }

    // Provide access to the current model
    public static PlagueSimulation getCurrentModel() {
        return currentModel;
    }
}