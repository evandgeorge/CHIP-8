package evandgeorge.chip8.vm.debug.program;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

	private JButton runButton, pauseButton, stepButton, resetProgramButton;
	private Runnable runButtonRoutine, pauseButtonRoutine, stepButtonRoutine, clearAllBreakpointsButtonRoutine, resetProgramButtonRoutine;
	//private TimescaleAdjuster timescaleAdjuster = new TimescaleAdjuster();

	public ControlPanel() {
		runButton = new JButton("Run");
		pauseButton = new JButton("Pause");
		stepButton = new JButton("Step");
		resetProgramButton = new JButton("Reset");

		this.setLayout(new GridBagLayout());
		this.add(runButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0),  0, 0));
		this.add(pauseButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0),  0, 0));
		this.add(stepButton, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0),  0, 0));
		this.add(resetProgramButton, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0),  0, 0));
		//this.add(timescaleAdjuster, new GridBagConstraints(0, 2, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),  0, 0));

		runButton.getModel().addActionListener(e -> {
			if(!runButton.getModel().isPressed() && runButtonRoutine != null) {
				runButtonRoutine.run();
			}
		});

		pauseButton.getModel().addActionListener(e -> {
			if(!pauseButton.getModel().isPressed() && pauseButtonRoutine != null) {
				pauseButtonRoutine.run();
			}
		});

		stepButton.getModel().addActionListener(e -> {
			if(!stepButton.getModel().isPressed() && stepButtonRoutine != null) {
				stepButtonRoutine.run();
			}
		});

		resetProgramButton.getModel().addActionListener(e -> {
			if(!resetProgramButton.getModel().isPressed() && resetProgramButtonRoutine != null) {
				resetProgramButtonRoutine.run();
			}
		});
	}

	public double getTimeFactor() {
		return 1;
	}

	public void update(boolean blocked) {
		runButton.setEnabled(blocked);
		pauseButton.setEnabled(!blocked);
	}

	public void setRunButtonRoutine(Runnable runButtonRoutine) {
		this.runButtonRoutine = runButtonRoutine;
	}

	public void setPauseButtonRoutine(Runnable pauseButtonRoutine) {
		this.pauseButtonRoutine = pauseButtonRoutine;
	}

	public void setStepButtonRoutine(Runnable stepButtonRoutine) {
		this.stepButtonRoutine = stepButtonRoutine;
	}

	public void setResetProgramButtonRoutine(Runnable resetProgramButtonRoutine) {
		this.resetProgramButtonRoutine = resetProgramButtonRoutine;
	}

	public void setClearAllBreakpointsButtonRoutine(Runnable clearAllBreakpointsButtonRoutine) {
		this.clearAllBreakpointsButtonRoutine = clearAllBreakpointsButtonRoutine;
	}
}
