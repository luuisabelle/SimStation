package prisonersDilemma;

public abstract class Strategy {
    protected Prisoner prisoner;

    public abstract boolean cooperate();
}
