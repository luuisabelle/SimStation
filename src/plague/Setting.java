package plague;

public class Setting {
    private static Setting instance = new Setting();
    private int populationSize = 50;
    private int initialInfectedPercent = 10;
    private int virulence = 50;
    private int recoveryTime = 200;
    private boolean isFatal = false;

    // Private constructor for singleton pattern
    private Setting() {}

    // Get the singleton instance
    public static Setting getInstance() {
        return instance;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int size) {
        this.populationSize = size;
    }

    public int getInitialInfectedPercent() {
        return initialInfectedPercent;
    }

    public void setInitialInfectedPercent(int percent) {
        this.initialInfectedPercent = percent;
    }

    public int getVirulence() {
        return virulence;
    }

    public void setVirulence(int virulence) {
        this.virulence = virulence;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(int time) {
        this.recoveryTime = time;
    }

    public boolean isFatal() {
        return isFatal;
    }

    public void setFatal(boolean fatal) {
        this.isFatal = fatal;
    }

    public void applyToModel(PlagueSimulation model) {
        if (model != null) {
            model.setPopulationSize(populationSize);
            model.setInitialInfectedPercent(initialInfectedPercent);
            model.setRecoveryTime(recoveryTime);
            PlagueSimulation.VIRULENCE = virulence;

            // Handle fatality setting
            if (isFatal && !model.isFatal()) {
                model.toggleFatality();
            } else if (!isFatal && model.isFatal()) {
                model.toggleFatality();
            }
        }
    }
}