package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.state.Display;
import evandgeorge.chip8.vm.state.VirtualMachine;

/**----------------------------------------------------------------**
 * 00E0 - CLS
 *
 * Clear the display.
 **----------------------------------------------------------------**/

public class ClearInstruction implements Instruction {

	@Override
	public String getDisassembly() {
		return "CLS";
	}

	@Override
	public String getDescription() {
		return "Clears the display";
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var transformer = vm.createTransformer().setDisplay(Display.blankDisplay);

		return transformer.transform();
	}
}
