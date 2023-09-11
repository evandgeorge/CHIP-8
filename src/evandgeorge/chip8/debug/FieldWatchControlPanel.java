package evandgeorge.chip8.debug;

import javax.swing.*;

public class FieldWatchControlPanel extends JPanel {

	private JButton addWatchButton, removeWatchButton;

	public FieldWatchControlPanel() {
		addWatchButton = new JButton("Add Watch");
		removeWatchButton = new JButton("Remove Watch");

		this.add(addWatchButton);
		this.add(removeWatchButton);
	}

}
