package randomWalks;
import mvc.*;
import simstation.*;

public class RandomWalkSimulation extends World {

    public void populate() {
        for(int i = 0; i < 50; i++)
            addAgent(new Drunk());
    }

    public static void main(String[] args) {
        AppPanel panel = new WorldPanel(new RandomWalkFactory());
        panel.display();
    }

}
