package evandgeorge.chip8.vm.debug;

import evandgeorge.chip8.vm.audio.AudioOutput;
import evandgeorge.chip8.vm.audio.SawtoothWaveTone;
import evandgeorge.chip8.vm.audio.Sound;
import evandgeorge.chip8.vm.debug.program.ControlPanel;
import evandgeorge.chip8.vm.debug.program.FilterPanel;
import evandgeorge.chip8.vm.debug.program.ProgramDisplay;
import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.DrawSpriteInstruction;
import evandgeorge.chip8.vm.state.Memory;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Program;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Debugger {

	private final JPanel container = new JPanel();

	public final Keyboard keyboard = new Keyboard();
	private final KeyboardDisplay keyboardDisplay = new KeyboardDisplay(keyboard);
	private final ProgramDisplay programDisplay;
	private final RegisterTable registerTable = new RegisterTable();
	private final MemoryDisplay memoryDisplay = new MemoryDisplay();
	private final DisplayPanel displayPanel = new DisplayPanel();
	private final ControlPanel controlPanel = new ControlPanel();
	private final FilterPanel filterPanel = new FilterPanel();

	//private Set<FieldWatch> fieldBreakpoints = new HashSet<>();
	//private ArrayList<FieldWatch> fieldWatches = new ArrayList<>();
	//private ArrayList<Boolean> fieldRecentlyChanged = new ArrayList<>();
	//private final FieldWatchDisplay fieldWatchDisplay = new FieldWatchDisplay();
	//private final FieldWatchControlPanel fieldWatchControlPanel = new FieldWatchControlPanel();

	private VirtualMachine initialVMState;
	private VirtualMachine vm;

	private AudioOutput audioOutput;
	private Sound buzzerSound = new SawtoothWaveTone(100);

	private Thread thread = new Thread(this::threadRoutine, "VM debugger thread");
	private ReentrantLock lock = new ReentrantLock();
	private Condition unblocked = lock.newCondition();
	private boolean blocked = true;

	private long lastStepTime = -1;

	public Debugger(VirtualMachine vm, Program program) {
		programDisplay = new ProgramDisplay(program);
		memoryDisplay.update(vm.getMemory());
		registerTable.update(vm.getProcessor());
		displayPanel.update(vm.getDisplay());
		controlPanel.update(blocked);
		initialVMState = this.vm = vm;

		try {
			audioOutput = new AudioOutput();
			audioOutput.startAudio();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		setupComponents();

		controlPanel.setRunButtonRoutine(this::run);
		controlPanel.setPauseButtonRoutine(this::pause);
		controlPanel.setStepButtonRoutine(this::step);
		controlPanel.setResetProgramButtonRoutine(this::reset);
		//controlPanel.setClearAllBreakpointsButtonRoutine(programDisplay::clearAllBreaks);

		filterPanel.setFilterCheckboxChangeRoutine(programDisplay::setFilterEnabled);
		filterPanel.setFilterFieldChangeRoutine(programDisplay::setFilter);

		thread.start();
	}

	public Container getUI() {
		return container;
	}

	private void setupComponents() {
		container.setLayout(new GridBagLayout());

		GridBagConstraints memoryDisplayConstraints = new GridBagConstraints(
				0, 0, 1, 2, 0, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints programDisplayAndControlsConstraints = new GridBagConstraints(
				1, 0, 1, 2, 0, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints displayPanelConstraints = new GridBagConstraints(
				3, 0, 1, 1, 1, 10,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints keyboardDisplayConstraints = new GridBagConstraints(
				3, 1, GridBagConstraints.REMAINDER, 1, .5, .25,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		GridBagConstraints registerTableConstraints = new GridBagConstraints(
				2, 1, 1, 1, 0, .25,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(5, 5, 0, 5), 0, 0);

		GridBagConstraints fieldWatchDisplayAndControlPanelConstraints = new GridBagConstraints(
				2, 0, 1, 1, 0, 10,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		JScrollPane memoryDisplayScrollPanel = new JScrollPane(memoryDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		memoryDisplayScrollPanel.setMinimumSize(memoryDisplay.getMinimumSize());

		JScrollPane programDisplayScrollPanel = new JScrollPane(programDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		programDisplayScrollPanel.setMinimumSize(programDisplay.getMinimumSize());

		JPanel programDisplayAndControlPanel = new JPanel();
		programDisplayAndControlPanel.setLayout(new GridBagLayout());
		GridBagConstraints programDisplayConstraints = new GridBagConstraints();
		GridBagConstraints controlPanelConstraints = new GridBagConstraints();
		GridBagConstraints filterPanelConstraints = new GridBagConstraints();
		programDisplayConstraints.gridx = controlPanelConstraints.gridx = filterPanelConstraints.gridx = 0;
		programDisplayConstraints.gridy = 1;
		controlPanelConstraints.gridy = 2;
		controlPanelConstraints.weightx = 0;
		controlPanelConstraints.weighty = 0;
		programDisplayConstraints.weightx = 1;
		programDisplayConstraints.weighty = 1;
		filterPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

		programDisplayAndControlPanel.add(filterPanel, filterPanelConstraints);
		programDisplayAndControlPanel.add(programDisplayScrollPanel, programDisplayConstraints);
		programDisplayAndControlPanel.add(controlPanel, controlPanelConstraints);

		/*JPanel fieldWatchDisplayAndControlPanel = new JPanel();
		fieldWatchDisplayAndControlPanel.setLayout(new GridBagLayout());
		GridBagConstraints fieldWatchDisplayConstraints = new GridBagConstraints();
		GridBagConstraints fieldWatchControlPanelConstraints = new GridBagConstraints();
		fieldWatchDisplayConstraints.gridx = fieldWatchControlPanelConstraints.gridx = fieldWatchDisplayConstraints.gridy = 0;
		fieldWatchControlPanelConstraints.gridy = 1;
		fieldWatchControlPanelConstraints.weightx = 0;
		fieldWatchControlPanelConstraints.weighty = 0;
		fieldWatchDisplayConstraints.weightx = 1;
		fieldWatchDisplayConstraints.weighty = 1;
		fieldWatchDisplayConstraints.fill = GridBagConstraints.BOTH;

/*		fieldWatchDisplayAndControlPanel.add(new JScrollPane(fieldWatchDisplay), fieldWatchDisplayConstraints);
		fieldWatchDisplayAndControlPanel.add(fieldWatchControlPanel, fieldWatchControlPanelConstraints);*/

		memoryDisplay.setBackground(container.getBackground());
		programDisplay.setBackground(container.getBackground());

		//container.add(fieldWatchDisplayAndControlPanel, fieldWatchDisplayAndControlPanelConstraints);
		container.add(memoryDisplayScrollPanel, memoryDisplayConstraints);
		container.add(programDisplayAndControlPanel, programDisplayAndControlsConstraints);
		container.add(displayPanel, displayPanelConstraints);
		container.add(registerTable, registerTableConstraints);
		container.add(keyboardDisplay, keyboardDisplayConstraints);

		bindVirtualKeyboard();
	}

	public void run() {
		lock.lock();

		blocked = false;
		updateUI();
		unblocked.signal();

		lock.unlock();
	}

	public void pause() {
		lock.lock();

		blocked = true;
		updateUI();
		programDisplay.ensureCurrentInstructionIsVisible(vm.getProcessor().getProgramCounter().asInt() - Memory.ROM_BEGIN_OFFSET);

		lock.unlock();
	}

	public void step() {
		lock.lock();

		stepVM();
		blocked = true;
		updateUI();
		programDisplay.ensureCurrentInstructionIsVisible(vm.getProcessor().getProgramCounter().asInt() - Memory.ROM_BEGIN_OFFSET);

		lock.unlock();
	}


	private void reset() {
		lock.lock();

		vm = initialVMState;
		blocked = true;
		updateUI();

		lock.unlock();
	}

	private void updateUI() {
		memoryDisplay.update(vm.getMemory());
		displayPanel.update(vm.getDisplay());
		registerTable.update(vm.getProcessor());
		controlPanel.update(blocked);
		programDisplay.update((vm.getProcessor().getProgramCounter().asInt() - Memory.ROM_BEGIN_OFFSET) / 2, blocked);
		//fieldWatchDisplay.update(fieldWatches.toArray(new FieldWatch[0]), fieldRecentlyChanged.toArray(new Boolean[0]), Collections.unmodifiableSet(fieldBreakpoints));

		if(vm.getNextInstruction() instanceof DrawSpriteInstruction drawSpriteInstruction) {
			int spriteAddress = vm.getProcessor().getIndexRegister().asInt();
			int spriteHeight = drawSpriteInstruction.constant.asInt();
			memoryDisplay.setHighlightedSprite(spriteAddress, spriteHeight);
		} else {
			memoryDisplay.clearSpriteHighlight();
		}
	}

	private void threadRoutine() {
		while(true) {
			lock.lock();
			updateUI();

			while(blocked) {
				try {
					unblocked.await();
				} catch (InterruptedException e) {
					return;
				}
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}

			stepVM();

			if(vm.getProcessor().getSoundTimer().asInt() > 0) {
				audioOutput.startSound(buzzerSound);
			} else {
				audioOutput.stopSound(buzzerSound);
			}

			blocked |= programDisplay.isBreakSet((vm.getProcessor().getProgramCounter().asInt() - Memory.ROM_BEGIN_OFFSET) / 2);

			lock.unlock();
		}
	}

	private void stepVM() {
		long now = System.currentTimeMillis();

		if(lastStepTime == -1)
			vm = vm.step(keyboard, 0);
		else
			vm = vm.step(keyboard, (int) (controlPanel.getTimeFactor() * (now - lastStepTime)));

		/*for(int i = 0; i < fieldWatches.size(); i++) {
			if(fieldWatches.get(i).updateAndCheckForChange(vm));
		}*/

		lastStepTime = now;
	}

	private void bindVirtualKeyboard() {
		ActionMap actionMap = container.getActionMap();
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = container.getInputMap(condition );

		for(int keyCode = KeyEvent.VK_0; keyCode <= KeyEvent.VK_9; keyCode++) {
			int virtualKey = keyCode - KeyEvent.VK_0;
			bindKeyToVirtualKeyboard(actionMap, inputMap, keyCode, virtualKey);
		}

		for(int keyCode = KeyEvent.VK_A; keyCode <= KeyEvent.VK_F; keyCode++) {
			int virtualKey = keyCode - KeyEvent.VK_A + 0xA;
			bindKeyToVirtualKeyboard(actionMap, inputMap, keyCode, virtualKey);
		}
	}

	private void bindKeyToVirtualKeyboard(ActionMap actionMap, InputMap inputMap, int keyCode, int virtualKey) {
		KeyAction keyPressAction = new KeyAction(virtualKey, true);
		KeyAction keyReleaseAction = new KeyAction(virtualKey, false);

		inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), keyPressAction.getActionMapKey());
		inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), keyReleaseAction.getActionMapKey());

		actionMap.put(keyReleaseAction.getActionMapKey(), keyReleaseAction);
		actionMap.put(keyPressAction.getActionMapKey(), keyPressAction);
	}

	/*public void addFieldWatch(int i, FieldWatch fieldWatch) {
		fieldWatches.add(i, fieldWatch);
		fieldRecentlyChanged.remove(i);
		updateUI();
	}*/

/*	public void removeFieldWatch(int i) {
		fieldWatches.remove(i);
		fieldRecentlyChanged.remove(i);
		updateUI();
	}*/

	private class KeyAction extends AbstractAction {

		private int virtualKey;
		private boolean pressed;

		public KeyAction(int virtualKey, boolean pressed) {
			this.virtualKey = virtualKey;
			this.pressed = pressed;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvt) {
			keyboard.setKeyState(virtualKey, pressed);
		}

		public Object getActionMapKey() {
			return virtualKey + " " + pressed;
		}
	}
}
