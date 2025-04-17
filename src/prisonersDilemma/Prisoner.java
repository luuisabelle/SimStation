package prisonersDilemma;

import simstation.MobileAgent;

public class Prisoner extends MobileAgent {
    private int fitness = 0;
    private boolean cheated = false;
    private boolean partnerCheated = false;
    private Strategy strategy;
    private static final int PARTNER_RADIUS = 20;

    public Prisoner(Strategy strategy) {
        super();
        this.strategy = strategy;
        strategy.prisoner = this;
    }

    @Override
    public void update() {
        move(2);
        turn(Heading.random());
        Prisoner partner = (Prisoner) world.getNeighbor(this, PARTNER_RADIUS);
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
