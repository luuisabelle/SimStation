package simstation;

public class ObserverAgent extends Agent {
    private World world;

    public ObserverAgent(World world) {
        this.world = world;
    }
    @Override
    public void update()
    {
        if(world!=null) {
            world.updateStatistics();
        }
    }
}
