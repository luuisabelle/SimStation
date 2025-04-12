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

    public boolean isFatal() {
        return isFatal;
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
        // Clear existing agents
        getAgents().clear();
        infectedCount = 0;

        System.out.println("PlagueSimulation.populate: Creating " + populationSize +
                " agents with " + initialInfectedPercent + "% infected initially");
        System.out.println("Virulence: " + VIRULENCE + "%, Recovery Time: " + recoveryTime +
                ", Fatal: " + isFatal);

        // Calculate exactly how many hosts should be infected based on percentage
        int numInfected = (int)Math.ceil((populationSize * initialInfectedPercent) / 100.0);
        System.out.println("Will infect " + numInfected + " out of " + populationSize + " agents");

        // Create a population of hosts
        for (int i = 0; i < populationSize; i++) {
            Host host = new Host();

            // Set recovery parameters
            host.setRecoveryTime(recoveryTime);
            host.setFatal(isFatal);

            // Infect the calculated number of hosts
            if (i < numInfected) {
                host.setInfected(true);
                System.out.println("Agent " + i + " initially infected");
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
        int infectionCount = 0;

        for (Agent a : getAgents()) {
            if (!(a instanceof ObserverAgent)) {
                agentCount++;
                if (a instanceof Host && ((Host) a).isInfected()) {
                    infectionCount++;
                }
            }
        }

        if (agentCount > 0) {
            infectedPercent = (double) infectionCount / agentCount * 100;
        }

        String parentStatus = super.getStatus();
        String clockLine = "";
        for (String line : parentStatus.split("\n")) {
            if (line.contains("#clock:")) {
                clockLine = line;
                break;
            }
        }

        return "#agents = " + agentCount + "\n" +
                clockLine + "\n" +
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