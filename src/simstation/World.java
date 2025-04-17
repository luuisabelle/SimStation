package simstation;

import mvc.Model;
import mvc.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World extends Model {
    protected static int SIZE = 500;
    private int clock = 0;
    private int alive = 0;
    protected List<Agent> agents;
    private boolean statsUpdaterAdded = false;


    public World() {
        agents = new ArrayList<>();
    }

    public synchronized void addAgent(Agent a) {
        // Set random initial position if not already set
        if (a.getXc() == 0 && a.getYc() == 0) {
            a.setXc(Utilities.rng.nextInt(SIZE));
            a.setYc(Utilities.rng.nextInt(SIZE));
        }
        a.world = this;
        agents.add(a);
    }

    public synchronized void startAgents() {
        // Reset clock and stats when starting
//        clock = 0;
//        alive = agents.size();
        // Add a stats updater agent if not already added
        agents.clear();
        if (!statsUpdaterAdded) {
            addAgent(new ObserverAgent(this));
            statsUpdaterAdded = true;
        }
        populate();
        for (Agent a : agents) {
            a.start();
        }
        changed();
    }

    public synchronized void stopAgents() {
        for (Agent a : agents) {
            a.stop();
        }
        changed();
    }

    public synchronized void pauseAgents() {
        for (Agent a : agents) {
            a.pause();
        }
        changed();
    }

    public synchronized void resumeAgents() {
        for (Agent a : agents) {
            a.resume();
        }
        changed();
    }

    public void populate() {
        //empty method to be overridden in subclasses
    }

    public String getStatus() {
        int agentCount = 0;
        for (Agent a : agents) {
            if (!(a instanceof ObserverAgent)) {
                agentCount++;
            }
        }
        return "#agents: " + agentCount + "\n"+
                "#living: " + alive + "\n" +
                "#clock: " + clock;
    }

    public synchronized void updateStatistics() {
        clock++;
        alive = 0;
        for (Agent a : agents) {
            if (!(a instanceof ObserverAgent)) {
                alive++;
            }
        }
        changed();
    }

    public Agent getNeighbor(Agent caller, int radius) {
        List<Agent> nearby = new ArrayList<>();
        for (Agent a : agents) {
            if (a != caller && (a instanceof MobileAgent)) { // will probably change implementation to match the professor's recommendation
                int dx = a.getXc() - caller.getXc();
                int dy = a.getYc() - caller.getYc();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= radius) {
                    nearby.add(a);
                }
            }
        }
        if (nearby.isEmpty()) {
            return null;
        }
        return nearby.get(Utilities.rng.nextInt(nearby.size()));
    }

    public synchronized List<Agent> getAgents() {
        return agents;
    }

    public Iterator<Agent> iterator() {
        return agents.iterator();
    }
}
