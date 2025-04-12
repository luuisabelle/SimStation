package plague;

import mvc.Utilities;
import simstation.Agent;
import simstation.MobileAgent;
import simstation.World;

class Host extends MobileAgent {
    private boolean infected = false;
    private static final int INFECTION_RADIUS = 10;
    private int recoveryTime = 200;
    private int infectionTime = 0;
    private boolean fatal = false;

    public Host() {
        super();
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
        if (infected) {
            infectionTime = 0; // Reset infection time counter
        }
    }

    public void setRecoveryTime(int time) {
        this.recoveryTime = time;
    }

    public void setFatal(boolean fatal) {
        this.fatal = fatal;
    }


    @Override
    public void update() {
        // If the agent is dead (fatal infection and recovery time passed), don't move
        if (fatal && infected && infectionTime >= recoveryTime) {
            return;
        }

        // Move in the current heading
        move(1);

        // Handle infection
        if (infected) {
            // Increment infection time
            infectionTime++;

            // Check if recovery time has been reached
            if (infectionTime >= recoveryTime) {
                if (!fatal) {
                    // Recover from infection
                    infected = false;
                    infectionTime = 0;
                }
            } else {
                // Try to infect nearby hosts
                Agent neighbor = world.getNeighbor(this, INFECTION_RADIUS);
                if (neighbor != null && neighbor instanceof Host && !((Host) neighbor).isInfected()) {
                    // Check if infection happens based on virulence
                    int roll = Utilities.rng.nextInt(100);
                    boolean infectionOccurs = roll < PlagueSimulation.VIRULENCE;

                    if (infectionOccurs) {
                        // Check if host resists based on resistance
                        roll = Utilities.rng.nextInt(100);
                        boolean resistanceSucceeds = roll < PlagueSimulation.RESISTANCE;

                        if (!resistanceSucceeds) {
                            ((Host) neighbor).setInfected(true);
                        }
                    }
                }
            }
        }
    }
}