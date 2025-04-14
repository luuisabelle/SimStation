package prisonersDilemma;

import mvc.Model;
import simstation.WorldFactory;

public class PrisonFactory extends WorldFactory {
    @Override
    public String getTitle() {
        return "Prisoner's Dilemma";
    }
    @Override
    public Model makeModel() {
        return new Prison();
    }
}
