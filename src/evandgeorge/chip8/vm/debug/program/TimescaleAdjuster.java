package evandgeorge.chip8.vm.debug.program;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class TimescaleAdjuster extends JPanel {

	private static final double[] TIME_SCALES = new double[] {.5, 1, 2, 5, 10};
	private static final Dictionary<Integer, JLabel> TIME_SCALE_LABEL_TABLE = new Hashtable<>();

	static {
		for(int i = 0; i < TIME_SCALES.length; i++) {
			String labelText = Double.toString(TIME_SCALES[i]).replace(".0", "");
			TIME_SCALE_LABEL_TABLE.put(i, new JLabel(labelText));
		}
	}

	private final JLabel timescaleAdjusterLabel = new JLabel("Timescale:");
	private final JSlider timescaleSlider = new JSlider(JSlider.HORIZONTAL, TIME_SCALES.length - 1, 1);

	public TimescaleAdjuster() {
		this.setLayout(new GridBagLayout());
		timescaleSlider.setSnapToTicks(true);
		timescaleSlider.setLabelTable(TIME_SCALE_LABEL_TABLE);
		timescaleSlider.setMinorTickSpacing(1);
		timescaleSlider.setPaintTicks(true);
		timescaleSlider.setPaintLabels(true);

		this.add(timescaleAdjusterLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),  0, 0));
		this.add(timescaleSlider, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),  0, 0));
	}

	public double getTimeFactor() {
		return TIME_SCALES[timescaleSlider.getValue()];
	}
}
