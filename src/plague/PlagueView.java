package plague;

import simstation.Agent;
import simstation.WorldView;

import java.awt.*;

class PlagueView extends WorldView {

    public PlagueView(PlagueSimulation model) {
        super(model);
    }

    @Override
    public void drawAgent(Agent a, Graphics gc) {
        if (a instanceof Host) {
            if (((Host) a).isInfected()) {
                gc.setColor(Color.RED);     // Infected hosts are red
            } else {
                gc.setColor(Color.GREEN);   // Healthy hosts are green
            }
            gc.fillOval(a.getXc(), a.getYc(), 10, 10);
        }
    }
}
