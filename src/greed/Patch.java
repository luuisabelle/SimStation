package greed;

import simstation.Agent;

public class Patch extends Agent {
    public int energy = 100;
    public static int growBackRate = 1;
    public boolean beingGrazed = false;

    public Patch() {
        super();
    }

    public void setEnergy(int newEnergy) {
        // Enforce energy bounds (0-100)
        if (newEnergy > 100) {
            this.energy = 100;
        }
        else if (newEnergy < 0) {
            this.energy = 0;
        }
        else this.energy = newEnergy;
    }

    public synchronized void eatMe(Cow cow, int amt) {
        // Only one cow at a time can execute this code per patch
        if (amt <= energy) {
            this.setEnergy(energy - amt);
            cow.setEnergy(cow.energy + amt);
        } else {
            // Even when energy is low, allow the cow to get whatever is left
            int remainingEnergy = this.energy;
            this.setEnergy(0);
            cow.setEnergy(cow.energy + remainingEnergy);
            cow.moveEnergy(); // Move after eating what's available
        }
    }

    @Override
    public void update() {
        // Regrow grass every tick if not at maximum
        if (energy < 100) {
            setEnergy(energy + growBackRate);
        }
    }
}