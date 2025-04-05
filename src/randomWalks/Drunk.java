package randomWalks;

import mvc.*;
import simstation.*;

class Drunk extends MobileAgent {

    public Drunk() {
        super();
    }

    public void update() {
        Heading heading = Heading.random();
        int steps = Utilities.rng.nextInt(20) + 1;
        move(steps);
    }

}
