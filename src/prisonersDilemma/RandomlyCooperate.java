package prisonersDilemma;

import mvc.Utilities;

public class RandomlyCooperate extends Strategy {
    @Override
    public boolean cooperate() {
        return Utilities.rng.nextBoolean();
    }
}
