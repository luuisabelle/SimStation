package simstation;

import mvc.Model;
import mvc.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World extends Model {
    protected static int SIZE = 500;
    private int clock = 0;
    private int alive = 0;
    private List<Agent> agents;
    private boolean statsUpdaterAdded = false;


    public World() {
        agents = new ArrayList<>();
    }

    public void addAgent(Agent a) {
        // Set random initial position if not already set
        if (a.getXc() == 0 && a.getYc() == 0) {
            a.setXc(Utilities.rng.nextInt(SIZE));
            a.setYc(Utilities.rng.nextInt(SIZE));
        }

        // Set world reference if the agent is a MobileAgent
        if (a instanceof MobileAgent) {
            ((MobileAgent) a).setWorld(this);
        }

        agents.add(a);
    }

    public void startAgents() {
        // Reset clock and stats when starting
        clock = 0;
        alive = agents.size();
        // Add a stats updater agent if not already added
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

    public void stopAgents() {
        for (Agent a : agents) {
            a.stop();
        }
        changed();
    }

    public void pauseAgents() {
        for (Agent a : agents) {
            a.pause();
        }
        changed();
    }

    public void resumeAgents() {
        for (Agent a : agents) {
            a.resume();
        }
        changed();
    }

    public void populate() {
        //empty method to be overridden in subclasses
    }

    public String getStatus() {
        return "#agents: " + agents.size() + "\n"+
                "#living: " + alive + "\n" +
                "#clock: " + clock;
    }

    public void updateStatistics() {
        clock++;
        alive++;
    }

    public Agent getNeighbor(Agent caller, int radius) {
        List<Agent> nearby = new ArrayList<>();
        for (Agent a : agents) { // will probably change implementation to match the professor's recommendation
            int dx = a.getXc() - caller.getXc();
            int dy = a.getYc() - caller.getYc();
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance <= radius) {
                nearby.add(a);
            }
        }
        return nearby.get(new Random().nextInt(nearby.size()));
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Iterator<Agent> iterator() {
        return agents.iterator();
    }
}
