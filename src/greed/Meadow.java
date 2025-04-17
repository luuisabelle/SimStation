package greed;

import simstation.Agent;
import simstation.World;

public class Meadow extends World {

    int waitPenalty = 5;
    int moveEnergy = 10;
    int numCows = 50;

    //int greediness = 25;
    int patchSize = 20;
    int dim = SIZE / patchSize; // dim is how many patches per row

    public Patch getPatchAt(int cowX, int cowY) {
        // Calculate the grid position
        int gridX = cowX / patchSize;
        int gridY = cowY / patchSize;

        // Ensure coordinates are within bounds
        if (gridX < 0) gridX = 0;
        if (gridY < 0) gridY = 0;
        if (gridX >= dim) gridX = dim - 1;
        if (gridY >= dim) gridY = dim - 1;

        // Get the exact coordinates for this grid position
        int exactX = gridX * patchSize;
        int exactY = gridY * patchSize;

        // Look for a patch at the exact position
        for (Agent agent : getAgents()) {
            if (agent instanceof Patch && agent.getXc() == exactX && agent.getYc() == exactY) {
                return (Patch) agent;
            }
        }

        // If we get here, we didn't find the patch - create one on demand
        System.out.println("Creating missing patch at grid position (" + gridX + "," + gridY +
                ") with coordinates (" + exactX + "," + exactY + ")");

        Patch newPatch = new Patch();
        newPatch.setXc(exactX);
        newPatch.setYc(exactY);

        // Use the parent class's addAgent
        super.addAgent(newPatch);

        // Make sure the patch stayed at the correct position
        if (newPatch.getXc() != exactX || newPatch.getYc() != exactY) {
            newPatch.setXc(exactX);
            newPatch.setYc(exactY);
        }

        return newPatch;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPatchSize() {
        return patchSize;
    }

    @Override
    public void populate() {
        // Pre-create a patch at (1,1) so World's addAgent doesn't randomize (0,0)
        Patch firstPatch = new Patch();
        firstPatch.setXc(1);
        firstPatch.setYc(1);
        super.addAgent(firstPatch);

        // Create patches for all positions EXCEPT (0,0)
        for(int i = 0; i < dim; i++){
            for (int j = 0; j < dim; j++){
                // Skip (0,0) for now
                if (i == 0 && j == 0) {
                    continue;
                }

                Patch patch = new Patch();
                patch.setXc(i * patchSize);
                patch.setYc(j * patchSize);
                super.addAgent(patch);
            }
        }

        // Now manually create the (0,0) patch last
        Patch zeroZeroPatch = new Patch();
        zeroZeroPatch.setXc(0);
        zeroZeroPatch.setYc(0);
        super.addAgent(zeroZeroPatch);

        if (zeroZeroPatch.getXc() != 0 || zeroZeroPatch.getYc() != 0) {
            zeroZeroPatch.setXc(0);
            zeroZeroPatch.setYc(0);
        }

        // Add cows - ensure they don't start at (0,0)
        for(int i = 0; i < numCows; i++){
            addAgent(new Cow());
        }
    }

    public int getDim() {
        return dim;
    }

    public void setGreediness(int greediness) {
        Cow.greediness = greediness;
    }

    public void setGrowBackRate(int growBackRate) { Patch.growBackRate = growBackRate; }

    public void setMoveEnergy(int moveEnergy) { this.moveEnergy = moveEnergy; }

    public int getGreediness() {
        return Cow.greediness;
    }
}