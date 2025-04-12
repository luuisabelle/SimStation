package prisonersDilemma;

import simstation.Agent;

public class Prisoner extends Agent {
    private int fitness = 0;
    private boolean cheated = false;
    private boolean partnerCheated = false;
    private Strategy strategy;

    public Prisoner(Strategy strategy) {
        super();
        this.strategy = strategy;
        strategy.prisoner = this;
    }

    @Override
    public void update() {
        Prisoner partner = (Prisoner) world.getNeighbor(this, 25);
        if (partner != null) {
            boolean thisCooperate = this.cooperate();
            boolean partnerCooperate = partner.cooperate();
            if (partnerCooperate && thisCooperate) {
                partner.updateFitness(3);
                this.updateFitness(3);
            }
            if (!partnerCooperate && thisCooperate) {
                partner.updateFitness(5);
            }
            if (partnerCooperate && !thisCooperate) {
                this.updateFitness(5);
            }
            if (!partnerCooperate && !thisCooperate) {
                partner.updateFitness(1);
                this.updateFitness(1);
            }
            cheated = !this.cooperate();
            partnerCheated = !partner.cooperate();
        }
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean isPartnerCheated() {
        return partnerCheated;
    }
    public boolean cooperate() {
        return strategy.cooperate();
    }
    public void updateFitness(int amount) {
        fitness+=amount;
    }

    public int getFitness() {
        return fitness;
    }
}
