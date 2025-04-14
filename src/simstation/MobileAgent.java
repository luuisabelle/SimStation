package simstation;

import mvc.Utilities;

public abstract class MobileAgent extends Agent {
    private Heading heading;

    public MobileAgent() {
        super();
        // Initialize with a random heading if not specified
        this.heading = Heading.random();
    }

// Add getter and setter for heading
    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }
    public void move(int steps) {
        switch (heading) {
            case NORTH -> setYc(getYc() - steps);
            case EAST -> setXc(getXc() + steps);
            case WEST -> setXc(getXc() - steps);
            case SOUTH -> setYc(getYc() + steps);
        }
        if (getXc() < 0) {
            setXc(getXc() + World.SIZE);
            setXc(getXc() + world.SIZE);
        }
        if (getXc() >= world.SIZE) {
            setXc(getXc() % world.SIZE);
        }
        if (getYc() < 0) {
            setYc(getYc() + World.SIZE);
            setYc(getYc() + world.SIZE);
        }
        if (getYc() >= world.SIZE) {
            setYc(getYc() % world.SIZE);
        }
        if (world != null) {
        world.changed(); }
    }

    protected void turn(Heading dir) {
        this.heading = dir;
    }

    public enum Heading {
        NORTH, EAST, SOUTH, WEST;

        public static Heading parse(String heading) {
            if (heading.equalsIgnoreCase("north")) return NORTH;
            if (heading.equalsIgnoreCase("east")) return EAST;
            if (heading.equalsIgnoreCase("south")) return SOUTH;
            if (heading.equalsIgnoreCase("west")) return WEST;
            Utilities.error("Invalid heading: " + heading);
            return null;
        }

        public static Heading random() {
            int luck = Utilities.rng.nextInt(4);
            if (luck == 0) return NORTH;
            if (luck == 1) return SOUTH;
            if (luck == 2) return EAST;
            return WEST;
        }

    }
}
