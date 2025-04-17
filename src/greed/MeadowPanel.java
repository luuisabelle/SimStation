package greed;

import simstation.*;
import mvc.Model;
import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class MeadowPanel extends WorldPanel {
    // Sliders for various simulation parameters
    private JSlider greedinessSlider;
    private JSlider growBackSlider;
    private JSlider moveEnergySlider;

    // Value labels displayed above sliders
    private JLabel greedinessLabel;
    private JLabel growBackLabel;
    private JLabel moveEnergyLabel;

    // Reference to the simulation
    private Meadow meadow;

    public MeadowPanel(GreedFactory factory) {
        super(factory);
        meadow = (Meadow) model;
        addSliderPanels();
    }

    private void addSliderPanels() {
        // Create a main panel to hold all the sliders
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBackground(Color.PINK);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create slider panels
        JPanel greedinessPanel = createLabeledSlider("Greediness:", 0, 100, 10, 10);
        JPanel growBackPanel = createLabeledSlider("Grow Back Rate:", 0, 10, 1, 2);
        JPanel moveEnergyPanel = createLabeledSlider("Move Energy:", 0, 50, 10, 10);

        // Add slider panels to the main panel
        sliderPanel.add(greedinessPanel);
        sliderPanel.add(growBackPanel);
        sliderPanel.add(moveEnergyPanel);
        sliderPanel.add(Box.createVerticalStrut(10));

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
        if (labelText.contains("Greediness")) {
            greedinessSlider = slider;
            greedinessLabel = valueLabel;
            slider.addChangeListener(e -> updateInitialInfected());
        }

        else if (labelText.contains("Grow Back Rate")) {
            growBackSlider = slider;
            growBackLabel = valueLabel;
            slider.addChangeListener(e -> updateGrowBackRate());
        }

        else if (labelText.contains("Move Energy")) {
            moveEnergySlider = slider;
            moveEnergyLabel = valueLabel;
            slider.addChangeListener(e -> updateMoveEnergy());
        }

        // Add slider to panel
        panel.add(slider);

        return panel;
    }

    @Override
    public void setModel(Model m) {
        super.setModel(m);
        if (m instanceof Meadow) {
            meadow = (Meadow)m;
        }
    }

    // adjust  upadte sliders
    private void updateInitialInfected() {
        int value = greedinessSlider.getValue();
        greedinessLabel.setText(String.valueOf(value));
        if (meadow != null) {
            meadow.setGreediness(value);
        }
    }

    private void updateGrowBackRate() {
        int value = growBackSlider.getValue();
        growBackLabel.setText(String.valueOf(value));
        if (meadow != null) {
            meadow.setGrowBackRate(value);
        }
    }

    private void updateMoveEnergy() {
        int value = moveEnergySlider.getValue();
        moveEnergyLabel.setText(String.valueOf(value));
        if (meadow != null) {
            meadow.setMoveEnergy(value);
        }
    }

    public static void main(String[] args) {
        FRAME_WIDTH = 1000;
        FRAME_HEIGHT = 570;
        MeadowPanel panel = new MeadowPanel(new GreedFactory());
        panel.display();
    }
}