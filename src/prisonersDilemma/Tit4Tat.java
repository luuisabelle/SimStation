package prisonersDilemma;

public class Tit4Tat extends Strategy {
    @Override
    public boolean cooperate() {
        return !prisoner.isPartnerCheated();
    }
}
