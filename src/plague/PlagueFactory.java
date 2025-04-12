package plague;

import mvc.Model;
import simstation.World;
import simstation.WorldFactory;
import simstation.WorldView;

class PlagueFactory extends WorldFactory {
    // Add settings storage to the factory
    private static int populationSize = 50;
    private static int initialInfectedPercent = 10;
    private static int recoveryTime = 200;
    private static boolean isFatal = false;

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

        // Apply current settings to the new model
        model.setPopulationSize(populationSize);
        model.setInitialInfectedPercent(initialInfectedPercent);
        model.setRecoveryTime(recoveryTime);

        // Set virulence directly on the static field
        PlagueSimulation.VIRULENCE = getVirulence();

        // Handle fatality setting
        if (isFatal && !model.isFatal()) {
            model.toggleFatality();
        } else if (!isFatal && model.isFatal()) {
            model.toggleFatality();
        }

        // Store the current model reference
        currentModel = model;

        System.out.println("PlagueFactory creating model with settings: " +
                "Population=" + populationSize +
                ", InitialInfected=" + initialInfectedPercent +
                ", Virulence=" + PlagueSimulation.VIRULENCE +
                ", RecoveryTime=" + recoveryTime +
                ", Fatal=" + isFatal);

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

    // Add getter and setter methods for the settings
    public static PlagueSimulation getCurrentModel() {
        return currentModel;
    }

    public static int getPopulationSize() {
        return populationSize;
    }

    public static void setPopulationSize(int size) {
        populationSize = size;
        if (currentModel != null) {
            currentModel.setPopulationSize(size);
        }
    }

    public static int getInitialInfectedPercent() {
        return initialInfectedPercent;
    }

    public static void setInitialInfectedPercent(int percent) {
        initialInfectedPercent = percent;
        if (currentModel != null) {
            currentModel.setInitialInfectedPercent(percent);
        }
    }

    public static int getVirulence() {
        return PlagueSimulation.VIRULENCE;
    }

    public static void setVirulence(int virulence) {
        PlagueSimulation.VIRULENCE = virulence;
    }

    public static int getRecoveryTime() {
        return recoveryTime;
    }

    public static void setRecoveryTime(int time) {
        recoveryTime = time;
        if (currentModel != null) {
            currentModel.setRecoveryTime(time);
        }
    }

    public static boolean isFatal() {
        return isFatal;
    }

    public static void setFatal(boolean fatal) {
        isFatal = fatal;
        if (currentModel != null) {
            boolean currentFatal = currentModel.isFatal();
            if (fatal != currentFatal) {
                currentModel.toggleFatality();
            }
        }
    }
}