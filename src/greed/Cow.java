package greed;

import simstation.MobileAgent;

public class Cow extends MobileAgent {
    int energy = 100;


    public boolean died = false;
    @Override
    public void update() {
        if (!died) {
            eat();
            if (energy <= 0) {
                die();
            }
        }
    }

    public void die() {
        died = true;
        this.stop();
    }


    public void eat() {
        Patch patch = (Patch) ((Meadow)world).getPatchAt(getXc(), getYc());
        int amountToEat = ((Meadow)world).getGreediness();

//        System.out.println(patch.getXc() + ", " + patch.getYc());

        if (patch != null) {
            synchronized (patch) {
                if (patch.energy >= amountToEat) {
                    patch.eatMe(this, amountToEat);
                } else {
                    moveEnergy(); // Try moving to find better grass
                    // If can't move, moveEnergy() will call waitForGrass
                }
            }
        }
    }


    public void setEnergy(int newEnergy) {
        if (newEnergy > 100) {
            this.energy = 100;
        }
        else if (newEnergy < 0) {
            this.energy = 0;
        }
        else this.energy = newEnergy;
    }

    public void moveToAnotherPatch(int patchSize){
        setHeading(Heading.random());
        int steps = patchSize;
        move(steps);
    }

    public void moveEnergy() {
        int moveCost = ((Meadow) world).getMoveEnergy();
        if (energy >= moveCost) {
            moveToAnotherPatch(((Meadow) world).getPatchSize());
            setEnergy(energy - moveCost);
        } else {
            waitForGrass(); // cow doesn't have energy to move
        }
    }

    public void waitForGrass() {
        int waitPenalty = ((Meadow) world).waitPenalty;
        setEnergy(energy - waitPenalty);
    }
}
