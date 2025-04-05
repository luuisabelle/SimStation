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

//        world.getStatus();
        // Find the parent frame to center the dialog
//        JFrame frame = null;
//        for (java.awt.Window window : java.awt.Window.getWindows()) {
//            if (window instanceof JFrame) {
//                frame = (JFrame) window;
//                break;
//            }
//        }

        // Display the statistics in a dialog box
        //JOptionPane.showMessageDialog(frame, stats, "Message", JOptionPane.INFORMATION_MESSAGE);
        Utilities.inform(stats);
    }
}
