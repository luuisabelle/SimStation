package plague;

import simstation.*;
import mvc.Utilities;

public class PlagueSimulation extends World {
    // Constants for the simulation
    public static int VIRULENCE = 50;    // % chance of infection
    public static int RESISTANCE = 2;    // % chance of resisting infection
    private int initialInfectedPercent = 10; // Default initial infected percentage
    private int populationSize = 50;     // Default population size
    private int recoveryTime = 200;      // Default recovery time
    private boolean isFatal = false;     // Whether infection is fatal
    private int clock;

    // Keep track of the last instance created for static access
    private static PlagueSimulation lastInstance;

    // Statistics variables
    private int infectedCount = 0;

    // Constructor
    public PlagueSimulation() {
        super();
        lastInstance = this;
    }


    // Static method to get the last instance
    public static PlagueSimulation getLastInstance() {
        return lastInstance;
    }

    // Static method to handle infection attempts

    // Getters and setters for parameters
    public void setInitialInfectedPercent(int percent) {
        this.initialInfectedPercent = percent;
    }

    public int getInitialInfectedPercent() {
        return initialInfectedPercent;
    }

    public void setPopulationSize(int size) {
        this.populationSize = size;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setRecoveryTime(int time) {
        this.recoveryTime = time;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public boolean toggleFatality() {
        isFatal = !isFatal;
        return isFatal;
    }

    public int getClock() {
        return clock;
    }


    @Override
    public void populate() {
        // Clear existing agents if any
        getAgents().clear();
        infectedCount = 0;
        // Create a population of hosts
        for (int i = 0; i < populationSize; i++) {
            Host host = new Host();

            // Set recovery parameters
            host.setRecoveryTime(recoveryTime);
            host.setFatal(isFatal);

            // Infect some percentage of hosts initially
            if (i < populationSize * (initialInfectedPercent / 100.0)) {
                host.setInfected(true);
                infectedCount++;
            }

            addAgent(host);
        }
    }

    @Override
    public String getStatus() {
        updateInfectedCount();

        // Calculate infection percentage
        double infectedPercent = 0;
        int agentCount = 0;

        for (Agent a : getAgents()) {
            if (!(a instanceof ObserverAgent)) {
                agentCount++;
                if (a instanceof Host && ((Host) a).isInfected()) {
                    infectedCount++;
                }
            }
        }

        if (agentCount > 0) {
            infectedPercent = (double) infectedCount / agentCount * 100;
        }

        return "#agents = " + agentCount + "\n" +
                "clock = " + getClock() + "\n" +
                "% infected = " + String.format("%.1f", infectedPercent);
    }

    // Update the count of infected agents
    public void updateInfectedCount() {
        infectedCount = 0;
        for (Agent a : getAgents()) {
            if (a instanceof Host && ((Host) a).isInfected()) {
                infectedCount++;
            }
        }
    }

    @Override
    public void updateStatistics() {
        super.updateStatistics();
        updateInfectedCount();
    }
}