package evandgeorge.chip8.vm.debug.program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.function.Consumer;

public class FilterPanel extends JPanel {

	JCheckBox filterCheckBox = new JCheckBox();
	JLabel filterLabel = new JLabel("Filter: ");
	JTextField filterField = new JTextField();

	private Consumer<Boolean> filterCheckboxChangeRoutine;
	private Consumer<String> filterFieldChangeRoutine;

	public FilterPanel() {
		GridBagConstraints filterCheckBoxConstraints = new GridBagConstraints(
				0, 0, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints filterLabelConstraints = new GridBagConstraints(
				1, 0, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints filterFieldConstraints = new GridBagConstraints(
				2, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);


		this.setLayout(new GridBagLayout());
		this.add(filterCheckBox, filterCheckBoxConstraints);
		this.add(filterLabel, filterLabelConstraints);
		this.add(filterField, filterFieldConstraints);

		filterCheckBox.addActionListener(e -> {
			if(filterCheckboxChangeRoutine != null)
				filterCheckboxChangeRoutine.accept(filterCheckBox.isSelected());
		});

		filterField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(filterFieldChangeRoutine != null)
					filterFieldChangeRoutine.accept(filterField.getText());
			}
		});
	}

	public boolean isFilterEnabled() {
		return filterCheckBox.isSelected();
	}

	public String getFilter() {
		return filterField.getText();
	}

	public void setFilterCheckboxChangeRoutine(Consumer<Boolean> filterCheckboxChangeRoutine) {
		this.filterCheckboxChangeRoutine = filterCheckboxChangeRoutine;
	}

	public void setFilterFieldChangeRoutine(Consumer<String> filterFieldChangeRoutine) {
		this.filterFieldChangeRoutine = filterFieldChangeRoutine;
	}
}
