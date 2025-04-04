package mvc;

public interface AppFactory {
    String getTitle();

    String[] getEditCommands();

    String getHelp();

    String about();

    Model makeModel();

    View makeView(Model m);

    Command makeEditCommand(Model model, String type, Object source);
}
