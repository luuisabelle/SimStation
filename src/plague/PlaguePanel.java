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

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if this is the Start button
        if (e.getActionCommand().equals("Start")) {
            // Apply current settings from sliders to the model before starting
            applyCurrentSettings();
        }

        // Call the parent implementation to handle the actual command
        super.actionPerformed(e);
    }

    // Update methods for sliders - use the static PlagueFactory methods
    private void updateInitialInfected() {
        int value = initialInfectedSlider.getValue();
        initialInfectedValue.setText(String.valueOf(value));

        // Update using factory method
        PlagueFactory.setInitialInfectedPercent(value);
    }

    private void updateInfectionProbability() {
        int value = infectionProbabilitySlider.getValue();
        infectionProbabilityValue.setText(String.valueOf(value));

        // Update using factory method
        PlagueFactory.setVirulence(value);
    }

    private void updatePopulationSize() {
        int value = populationSizeSlider.getValue();
        populationSizeValue.setText(String.valueOf(value));

        // Update using factory method
        PlagueFactory.setPopulationSize(value);
    }

    private void updateRecoveryTime() {
        int value = recoveryTimeSlider.getValue();
        recoveryTimeValue.setText(String.valueOf(value));

        // Update using factory method
        PlagueFactory.setRecoveryTime(value);
    }

    private void toggleFatality() {
        // Toggle the factory's fatality setting
        PlagueFactory.setFatal(!PlagueFactory.isFatal());

        // Update the button text
        fatalityButton.setText(PlagueFactory.isFatal() ? "Fatal" : "Not Fatal");
    }

    // Method to apply current settings before starting the simulation
    private void applyCurrentSettings() {
        System.out.println("Applying settings from UI: " +
                "Population=" + populationSizeSlider.getValue() +
                ", InitialInfected=" + initialInfectedSlider.getValue() +
                ", Virulence=" + infectionProbabilitySlider.getValue() +
                ", RecoveryTime=" + recoveryTimeSlider.getValue());

        // Update all factory settings
        PlagueFactory.setPopulationSize(populationSizeSlider.getValue());
        PlagueFactory.setInitialInfectedPercent(initialInfectedSlider.getValue());
        PlagueFactory.setVirulence(infectionProbabilitySlider.getValue());
        PlagueFactory.setRecoveryTime(recoveryTimeSlider.getValue());
    }

    // Update the setModel method to initialize sliders from factory settings
    @Override
    public void setModel(Model m) {
        // Call super implementation first
        super.setModel(m);

        // Update the plagueSimulation reference
        if (m instanceof PlagueSimulation) {
            this.plagueSimulation = (PlagueSimulation)m;

            // Initialize sliders with current factory values
            if (initialInfectedSlider != null) {
                initialInfectedSlider.setValue(PlagueFactory.getInitialInfectedPercent());
                initialInfectedValue.setText(String.valueOf(PlagueFactory.getInitialInfectedPercent()));

                infectionProbabilitySlider.setValue(PlagueFactory.getVirulence());
                infectionProbabilityValue.setText(String.valueOf(PlagueFactory.getVirulence()));

                populationSizeSlider.setValue(PlagueFactory.getPopulationSize());
                populationSizeValue.setText(String.valueOf(PlagueFactory.getPopulationSize()));

                recoveryTimeSlider.setValue(PlagueFactory.getRecoveryTime());
                recoveryTimeValue.setText(String.valueOf(PlagueFactory.getRecoveryTime()));

                // Update button text
                fatalityButton.setText(PlagueFactory.isFatal() ? "Fatal" : "Not Fatal");
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