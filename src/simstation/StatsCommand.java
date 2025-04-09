package simstation;

import mvc.Command;
import mvc.Model;
import mvc.Utilities;

import javax.swing.*;

public class StatsCommand extends Command {
    public StatsCommand(Model model) {
        super(model);
    }

    @Override
    public void execute() {
        World world = (World) model;
        String stats = world.getStatus();
        Utilities.inform(stats);
    }
}
