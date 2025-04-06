package flocking;

import simstation.*;
import mvc.*;

public class Bird extends MobileAgent {
    private int speed;
    private static final int DEFAULT_RADIUS = 20;

    public Bird() {
        super();
        speed = Utilities.rng.nextInt(5) + 1; // Random speed between 1-5
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void update() {
        // Ask world for a random nearby neighbor
        Agent neighborAgent = world.getNeighbor(this, DEFAULT_RADIUS);

        if (neighborAgent != null && neighborAgent != this && neighborAgent instanceof Bird) {
            Bird neighbor = (Bird)neighborAgent;

            // Copy the speed and heading of the neighbor
            this.setSpeed(neighbor.getSpeed());
            this.setHeading(neighbor.getHeading());
        }

        // Move with steps = speed
        move(speed);
    }
}