package plague;

import simstation.*;
import mvc.Model;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class PlaguePanel extends WorldPanel {
    // Sliders for various simulation parameters
    private JSlider initialInfectedSlider;
    private JSlider infectionProbabilitySlider;
    private JSlider populationSizeSlider;
    private JSlider recoveryTimeSlider;
    private JButton fatalityButton;

    // Value labels displayed above sliders
    private JLabel initialInfectedValue;
    private JLabel infectionProbabilityValue;
    private JLabel populationSizeValue;
    private JLabel recoveryTimeValue;

    // Reference to the simulation
    private PlagueSimulation plagueSimulation;

    public PlaguePanel(PlagueFactory factory) {
        super(factory);

        // Store reference to the model
        this.plagueSimulation = PlagueFactory.getCurrentModel();

        // Add the slider panels below the existing controls
        addSliderPanels();
    }

    private void addSliderPanels() {
        // Create a main panel to hold all the sliders
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBackground(Color.PINK);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create slider panels
        JPanel infectedPanel = createLabeledSlider("Initial % Infected:", 0, 100, 10, 10);
        JPanel probabilityPanel = createLabeledSlider("Infection Probability:", 0, 100, 50, 10);
        JPanel populationPanel = createLabeledSlider("Initial Population Size:", 0, 200, 50, 20);
        JPanel recoveryPanel = createLabeledSlider("Fatality/Recovery Time:", 0, 500, 200, 50);

        // Fatality button panel
        JPanel fatalityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fatalityPanel.setOpaque(false);
        fatalityButton = new JButton("Not Fatal");
        fatalityButton.setPreferredSize(new Dimension(100, 30));
        fatalityButton.addActionListener(e -> toggleFatality());
        fatalityPanel.add(fatalityButton);

        // Add slider panels to the main panel
        sliderPanel.add(infectedPanel);
        sliderPanel.add(Box.createVerticalStrut(10));
        sliderPanel.add(probabilityPanel);
        sliderPanel.add(Box.createVerticalStrut(10));
        sliderPanel.add(populationPanel);
        sliderPanel.add(Box.createVerticalStrut(10));
        sliderPanel.add(recoveryPanel);
        sliderPanel.add(Box.createVerticalStrut(10));
        sliderPanel.add(fatalityPanel);

        // Add the slider panel to the south position of the control panel
        controlPanel.add(sliderPanel, BorderLayout.SOUTH);

        // Update the control panel
        controlPanel.revalidate();
        controlPanel.repaint();
    }

    private JPanel createLabeledSlider(String labelText, int min, int max, int initial, int majorTick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Title label - centered
        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value label - centered below title
        JLabel valueLabel = new JLabel(String.valueOf(initial));
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Add title and value to panel
        panel.add(titleLabel);
        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(5));

        // Create slider with custom labels
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);

        // Only paint a few primary labels to avoid overlap
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        // For population size and recovery time, use fewer labels
        if (max > 100) {
            int step = (max == 200) ? 20 : 50;
            for (int i = min; i <= max; i += step) {
                labelTable.put(i, new JLabel(String.valueOf(i)));
            }
        } else {
            // For percentage sliders
            for (int i = min; i <= max; i += 10) {
                labelTable.put(i, new JLabel(String.valueOf(i)));
            }
        }

        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(majorTick);
        slider.setMinorTickSpacing(majorTick/2);

        // Add extra height for the slider to accommodate labels
        slider.setPreferredSize(new Dimension(slider.getPreferredSize().width, 50));

        // Store slider and value label references for later use
        if (labelText.contains("Initial % Infected")) {
            initialInfectedSlider = slider;
            initialInfectedValue = valueLabel;
            slider.addChangeListener(e -> updateInitialInfected());
        } else if (labelText.contains("Infection Probability")) {
            infectionProbabilitySlider = slider;
            infectionProbabilityValue = valueLabel;
            slider.addChangeListener(e -> updateInfectionProbability());
        } else if (labelText.contains("Population Size")) {
            populationSizeSlider = slider;
            populationSizeValue = valueLabel;
            slider.addChangeListener(e -> updatePopulationSize());
        } else if (labelText.contains("Fatality/Recovery")) {
            recoveryTimeSlider = slider;
            recoveryTimeValue = valueLabel;
            slider.addChangeListener(e -> updateRecoveryTime());
        }

        // Add slider to panel
        panel.add(slider);

        return panel;
    }

    private void updateInitialInfected() {
        int value = initialInfectedSlider.getValue();
        initialInfectedValue.setText(String.valueOf(value));

        // Update using settings class
        Setting.getInstance().setInitialInfectedPercent(value);
    }

    private void updateInfectionProbability() {
        int value = infectionProbabilitySlider.getValue();
        infectionProbabilityValue.setText(String.valueOf(value));

        // Update using settings class
        Setting.getInstance().setVirulence(value);
        // Also update the static field for immediate effect
        PlagueSimulation.VIRULENCE = value;
    }

    private void updatePopulationSize() {
        int value = populationSizeSlider.getValue();
        populationSizeValue.setText(String.valueOf(value));

        // Update using settings class
        Setting.getInstance().setPopulationSize(value);
    }

    private void updateRecoveryTime() {
        int value = recoveryTimeSlider.getValue();
        recoveryTimeValue.setText(String.valueOf(value));

        // Update using settings class
        Setting.getInstance().setRecoveryTime(value);
    }

    private void toggleFatality() {
        // Toggle using settings class
        boolean current = Setting.getInstance().isFatal();
        Setting.getInstance().setFatal(!current);

        // Update button text
        fatalityButton.setText(!current ? "Fatal" : "Not Fatal");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if this is the Start button
        if (e.getActionCommand().equals("Start")) {
            applyCurrentSettings();
            PlagueSimulation currentModel = PlagueFactory.getCurrentModel();
            if (currentModel != null) {
                Setting settings = Setting.getInstance();
                currentModel.setPopulationSize(settings.getPopulationSize());
                currentModel.setInitialInfectedPercent(settings.getInitialInfectedPercent());
                currentModel.setRecoveryTime(settings.getRecoveryTime());
                PlagueSimulation.VIRULENCE = settings.getVirulence();

                // Also check the fatality setting
                if (settings.isFatal() != currentModel.isFatal()) {
                    currentModel.toggleFatality();
                }
            }
        }

        // Call the parent implementation to handle the actual command
        super.actionPerformed(e);
    }

    private void applyCurrentSettings() {
        int infectedPercent = initialInfectedSlider.getValue();
        int virulence = infectionProbabilitySlider.getValue();
        int population = populationSizeSlider.getValue();
        int recovery = recoveryTimeSlider.getValue();
        Setting.getInstance().setPopulationSize(population);
        Setting.getInstance().setInitialInfectedPercent(infectedPercent);
        Setting.getInstance().setVirulence(virulence);
        Setting.getInstance().setRecoveryTime(recovery);
        PlagueSimulation.VIRULENCE = virulence;
    }
    @Override
    public void setModel(Model m) {
        // Call super implementation first
        super.setModel(m);

        // Update the plagueSimulation reference
        if (m instanceof PlagueSimulation) {
            this.plagueSimulation = (PlagueSimulation)m;

            // Initialize sliders with current settings values
            if (initialInfectedSlider != null) {
                Setting settings = Setting.getInstance();

                initialInfectedSlider.setValue(settings.getInitialInfectedPercent());
                initialInfectedValue.setText(String.valueOf(settings.getInitialInfectedPercent()));

                infectionProbabilitySlider.setValue(settings.getVirulence());
                infectionProbabilityValue.setText(String.valueOf(settings.getVirulence()));

                populationSizeSlider.setValue(settings.getPopulationSize());
                populationSizeValue.setText(String.valueOf(settings.getPopulationSize()));

                recoveryTimeSlider.setValue(settings.getRecoveryTime());
                recoveryTimeValue.setText(String.valueOf(settings.getRecoveryTime()));

                // Update button text
                fatalityButton.setText(settings.isFatal() ? "Fatal" : "Not Fatal");
            }

            System.out.println("PlaguePanel.setModel: Model set to " + m);
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        PlagueFactory factory = new PlagueFactory();
        PlaguePanel panel = new PlaguePanel(factory);
        panel.display();
    }
}