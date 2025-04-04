package simstation;

import mvc.AppFactory;
import mvc.Command;
import mvc.Model;
import mvc.View;

public class WorldFactory implements AppFactory {
    @Override
    public String getTitle() {
        return "Sim Station";
    }

    @Override
    public String[] getEditCommands() {
        return new String[] {"Start", "Pause", "Resume", "Stop", "Stats"};
    }

    @Override
    public String getHelp() {
        return "Start to start simulation\n" +
                "Pause to pause simulation\n" +
                "Resume to resume simulation\n" +
                "Stop to stop simulation\n" +
                "Stats to view statistics of simulation";
    }

    @Override
    public String about() {
        return "Sim Station Group 11: Quy Lu, Isabelle Luu";
    }

    @Override
    public Model makeModel() {
        return new World();
    }

    @Override
    public View makeView(Model m) {
        return new WorldView((World) m);
    }

    @Override
    public Command makeEditCommand(Model model, String type, Object source) {
        if (type == "Start") {
            return new StartCommand(model);
        }
        else if (type == "Pause") {
            return new SuspendCommand(model);
        }
        else if (type == "Resume") {
            return new ResumeCommand(model);
        }
        else if (type == "Stop") {
            return new StopCommand(model);
        }
        else if (type == "Stats") {
            return new StatsCommand(model);
        }
        return null;
    }
}
