package simstation;

import mvc.Command;
import mvc.Model;

public class SuspendCommand extends Command {
    public SuspendCommand(Model model) {
        super(model);
    }

    @Override
    public void execute() {
        World world = (World) model;
        world.pauseAgents();
    }
}
