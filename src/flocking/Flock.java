package flocking;

import mvc.*;
import simstation.*;

public class Flock extends World {

    public void populate() {
        // Create multiple birds (e.g., 20-30 birds)
        for(int i = 0; i < 30; i++) {
            addAgent(new Bird());
        }
    }

    public static void main(String[] args) {
        AppPanel panel = new WorldPanel(new FlockingFactory());
        panel.display();
    }
}