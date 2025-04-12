package prisonersDilemma;

import mvc.AppPanel;
import simstation.Agent;
import simstation.World;
import simstation.WorldPanel;

public class Prison extends World {
    private static int AGENTS_PER_STRATEGY = 10;
    private int cheatFitness = 0;
    private int cooperateFitness = 0;
    private int randomlyCooperateFitness = 0;
    private int tit4TatFitness = 0;
    private int cheatCount = 0;
    private int cooperateCount = 0;
    private int randomlyCooperateCount = 0;
    private int tit4TatCount = 0;

    @Override
    public void populate() {
        for (int i = 0; i < AGENTS_PER_STRATEGY; i++) {
            addAgent(new Prisoner(new Cheat()));
        }
        for (int i = 0; i < AGENTS_PER_STRATEGY; i++) {
            addAgent(new Prisoner(new Cooperate()));
        }
        for (int i = 0; i < AGENTS_PER_STRATEGY; i++) {
            addAgent(new Prisoner(new RandomlyCooperate()));
        }
        for (int i = 0; i < AGENTS_PER_STRATEGY; i++) {
            addAgent(new Prisoner(new Tit4Tat()));
        }
    }

    @Override
    public String getStatus() {
        cheatFitness = 0;
        cheatCount = 0;
        cooperateFitness = 0;
        cooperateCount = 0;
        randomlyCooperateFitness = 0;
        randomlyCooperateCount = 0;
        tit4TatFitness = 0;
        tit4TatCount = 0;
        for (Agent a : agents) {
            if (a instanceof Prisoner prisoner) {
                if (prisoner.getStrategy() instanceof Cheat) {
                    cheatCount++;
                    cheatFitness += prisoner.getFitness();
                } else if (prisoner.getStrategy() instanceof Cooperate) {
                    cooperateCount++;
                    cooperateFitness += prisoner.getFitness();
                } else if (prisoner.getStrategy() instanceof RandomlyCooperate) {
                    randomlyCooperateCount++;
                    randomlyCooperateFitness += prisoner.getFitness();
                } else if (prisoner.getStrategy() instanceof Tit4Tat) {
                    tit4TatCount++;
                    tit4TatFitness += prisoner.getFitness();
                }
            }
        }
        return "Average Strategy Fitness" + "\n" +
                "Cheat: " + cheatFitness/cheatCount + "\n" +
                "Cooperate: " + cooperateFitness/cooperateCount + "\n" +
                "Randomly Cooperate: " + randomlyCooperateFitness/randomlyCooperateCount + "\n" +
                "Tit 4 Tat: " + tit4TatFitness/tit4TatCount;
    }

    public static void main(String[] args) {
        AppPanel panel = new WorldPanel(new PrisonFactory());
        panel.display();
    }
}
