package simstation;

public class ObserverAgent extends Agent {
    private World world;
    @Override
    public void update() {
        world.updateStatistics();
    }
}
