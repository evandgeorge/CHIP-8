package evandgeorge.chip8.vm.debug;

import evandgeorge.chip8.vm.input.Keyboard;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class KeyboardDisplay extends JPanel {

	private JPanel[] keyPanels = new JPanel[16];

	private static Border pressedBorder = BorderFactory.createLoweredBevelBorder();
	private static Border notPressedBorder = BorderFactory.createRaisedBevelBorder();

	private final Color background = getBackground();
	private final Color waitingForKeyBackground = Color.PINK;

	public KeyboardDisplay(Keyboard keyboard) {
		this.setLayout(new GridLayout(4, 4));
		this.setBorder(BorderFactory.createTitledBorder("Virtual Keyboard"));

		for(int key = 0; key < keyPanels.length; key++) {
			keyPanels[key] = new JPanel();
			keyPanels[key].setLayout(new GridLayout(1, 1));
			JLabel keyLabel = new JLabel(Integer.toHexString(key).toUpperCase(), JLabel.CENTER);
			keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD));
			keyPanels[key].add(keyLabel);
			keyPanels[key].setBorder(notPressedBorder);
			this.add(keyPanels[key]);
		}

		keyboard.setKeyListener(new Keyboard.Listener() {
			@Override
			public void onKeyEvent(int virtualKey, boolean pressed) {
				SwingUtilities.invokeLater(() -> {
					updateKeyPanel(virtualKey, pressed);
					setBackground(background);
				});
			}

			@Override
			public void onAwaitKey() {
				SwingUtilities.invokeLater(() -> setBackground(waitingForKeyBackground));
			}
		});
	}

	private void updateKeyPanel(int key, boolean pressed) {
		if(pressed)
			keyPanels[key].setBorder(pressedBorder);
		else
			keyPanels[key].setBorder(notPressedBorder);
	}
}
