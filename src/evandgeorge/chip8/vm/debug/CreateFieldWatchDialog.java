package evandgeorge.chip8.vm.debug;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CreateFieldWatchDialog extends JDialog {

	private static JFormattedTextField.AbstractFormatter memoryAddressFormatter;

	private State ACTIVE = State.ACTIVE;
	private State SUBMITTED = State.SUBMITTED;
	private State CANCELED = State.CANCELED;

	private enum State {
		ACTIVE,
		SUBMITTED,
		CANCELED
	}

	private FieldWatch fieldWatch;
	private State formState;

	static {
		memoryAddressFormatter = new JFormattedTextField.AbstractFormatter() {
			@Override
			public Object stringToValue(String text) throws ParseException {
				try {
					if (text.length() > 2 && text.substring(0, 2).equalsIgnoreCase("0x")) {
						//parse hex
						return Integer.parseInt(text.substring(2), 16);
					} else {
						//parse decimal
						return Integer.parseInt(text, 10);
					}
				} catch(NumberFormatException e) {
					throw new ParseException("Invalid number (" + e.getMessage() + ")", -1);
				}
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				if(value instanceof Integer) {
					String hexStringManyLeadingZeros = "00" + Integer.toHexString((int) value).toUpperCase();
					return "0x" + hexStringManyLeadingZeros.substring(hexStringManyLeadingZeros.length() - 3);
				} else {
					return "0x000";
				}
			}
		};
	}

	JTextField nameField = new JTextField();
	JPanel container = new JPanel();

	JPanel memoryFieldWatchPanel = new JPanel();
	JFormattedTextField memoryBeginAddressField = new JFormattedTextField(memoryAddressFormatter);
	JComboBox<String> bytesField = new JComboBox<>(new String[] {"1 byte", "2 bytes"});

	ReentrantLock stateLock = new ReentrantLock();
	Condition closed = stateLock.newCondition();

	JPanel processorFieldWatchPanel = new JPanel();
	JComboBox<String> registerField = new JComboBox<>(new String[] {
			"V0", "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "VA", "VB", "VC", "VD", "VE", "VF"
	});

	private static final String MEMORY_FIELD_WATCH_OPTION = "Memory Field";
	private static final String PROCESSOR_FIELD_WATCH_OPTION = "Processor Field";

	JComboBox<String> fieldTypeComboBox = new JComboBox<>(new String[] {MEMORY_FIELD_WATCH_OPTION, PROCESSOR_FIELD_WATCH_OPTION});

	public CreateFieldWatchDialog() {
		memoryFieldWatchPanel.setLayout(new GridBagLayout());
		memoryFieldWatchPanel.add(new JLabel("Address: ", JLabel.RIGHT), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		memoryFieldWatchPanel.add(memoryBeginAddressField, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		memoryFieldWatchPanel.add(new JLabel("Size: ", JLabel.RIGHT), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		memoryFieldWatchPanel.add(bytesField, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		registerField.setBackground(container.getBackground());
		processorFieldWatchPanel.setLayout(new GridBagLayout());
		JLabel registerFieldLabel = new JLabel("Register: ", JLabel.RIGHT);
		processorFieldWatchPanel.add(registerFieldLabel);
		processorFieldWatchPanel.add(registerField);

		container.setLayout(new CardLayout());
		container.add(memoryFieldWatchPanel, MEMORY_FIELD_WATCH_OPTION);
		container.add(processorFieldWatchPanel, PROCESSOR_FIELD_WATCH_OPTION);

		JPanel nameFieldPanel = new JPanel();
		nameFieldPanel.setBackground(Color.WHITE);
		nameFieldPanel.setLayout(new BoxLayout(nameFieldPanel, BoxLayout.X_AXIS));
		JLabel nameFieldLabel = new JLabel(" Name: ", JLabel.RIGHT);
		nameFieldPanel.add(nameFieldLabel);
		nameFieldPanel.add(nameField);

		fieldTypeComboBox.addItemListener(e -> {
			CardLayout cl = (CardLayout) (container.getLayout());
			cl.show(container, (String) e.getItem());
		});

		this.setLayout(new BorderLayout());
		this.add(nameFieldPanel, BorderLayout.PAGE_END);
		this.add(fieldTypeComboBox, BorderLayout.PAGE_START);
		this.add(container, BorderLayout.CENTER);

		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	public void showAndWaitUntilClose() throws InterruptedException {
		stateLock.lock();
		SwingUtilities.invokeLater(() -> this.setVisible(true));

		while(formState == State.ACTIVE)
			closed.await();

		stateLock.unlock();
	}

	public State formResult() {
		return formState;
	}

	public FieldWatch getFieldWatch() {
		return fieldWatch;
	}
}
