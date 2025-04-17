package plague;

import simstation.*;
import mvc.Utilities;
import java.util.ArrayList;
import java.util.List;

public class PlagueSimulation extends World {
    // Constants for the simulation
    public static int VIRULENCE = 50;    // % chance of infection
    public static int RESISTANCE = 2;    // % chance of resisting infection
    private int initialInfectedPercent;
    private int populationSize;
    private int recoveryTime;
    private boolean isFatal = false;

    // Statistics variables
    private int infectedCount = 0;

    // Constructor
    public PlagueSimulation() {
        super();
        Setting settings = Setting.getInstance();
        this.populationSize = settings.getPopulationSize();
        this.initialInfectedPercent = settings.getInitialInfectedPercent();
        this.recoveryTime = settings.getRecoveryTime();
        this.isFatal = settings.isFatal();
        VIRULENCE = settings.getVirulence();
    }

    public boolean isFatal() {
        return isFatal;
    }

    @Override
    public synchronized void startAgents() {
        // First call the parent implementation
        super.startAgents();
        boolean hasObserver = false;
        for (Agent a : getAgents()) {
            if (a instanceof ObserverAgent) {
                hasObserver = true;
                break;
            }
        }
        if (!hasObserver) {
            addAgent(new ObserverAgent(this));
        }
    }

    public void applyImmediateVirulenceEffect(int virulence) {
        // Get all healthy hosts
        List<Host> healthyHosts = new ArrayList<>();
        for (Agent agent : getAgents()) {
            if (agent instanceof Host && !((Host) agent).isInfected()) {
                healthyHosts.add((Host) agent);
            }
        }

        // Skip if no healthy hosts
        if (healthyHosts.isEmpty()) {
            return;
        }

        double infectionRate = virulence / 500.0;
        int hostsToInfect = (int)Math.ceil(healthyHosts.size() * infectionRate);

        // Don't exceed the number of healthy hosts
        hostsToInfect = Math.min(hostsToInfect, healthyHosts.size());

        // Infect random healthy hosts
        for (int i = 0; i < hostsToInfect; i++) {
            // Exit if no more healthy hosts
            if (healthyHosts.isEmpty()) {
                break;
            }

            // Select a random healthy host
            int randomIndex = Utilities.rng.nextInt(healthyHosts.size());
            Host hostToInfect = healthyHosts.get(randomIndex);

            // Infect the host
            hostToInfect.setInfected(true);

            // Remove from the list to avoid infecting the same host twice
            healthyHosts.remove(randomIndex);
        }
        changed();
    }

    public void updateHostsRecoveryTime(int time) {
        // Store the new value
        this.recoveryTime = time;

        // Update all hosts
        for (Agent agent : getAgents()) {
            if (agent instanceof Host) {
                ((Host) agent).setRecoveryTime(time);
            }
        }

        // Notify observers that the model has changed
        changed();
    }

    public void updateHostsFatality(boolean fatal) {
        // Store the new value
        this.isFatal = fatal;

        // Update all hosts
        for (Agent agent : getAgents()) {
            if (agent instanceof Host) {
                ((Host) agent).setFatal(fatal);
            }
        }
        changed();
    }

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

    @Override
    public void populate() {
        Agent observerAgent = null;
        for (Agent a : getAgents()) {
            if (a instanceof ObserverAgent) {
                observerAgent = a;
                break;
            }
        }

        // Clear existing agents
        getAgents().clear();
        infectedCount = 0;

        // Re-add the observer agent if it existed
        if (observerAgent != null) {
            addAgent(observerAgent);
        }

        // Calculate exactly how many hosts should be infected based on percentage
        int numInfected = (int)Math.ceil((populationSize * initialInfectedPercent) / 100.0);
        // Create a population of hosts
        for (int i = 0; i < populationSize; i++) {
            Host host = new Host();
            host.setRecoveryTime(recoveryTime);
            host.setFatal(isFatal);
            if (i < numInfected) {
                host.setInfected(true);
            }

            addAgent(host);
        }
    }

    @Override
    public String getStatus() {
        updateInfectedCount();
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