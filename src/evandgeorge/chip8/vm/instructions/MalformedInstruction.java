package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.state.VirtualMachine;

public class MalformedInstruction implements Instruction {

	@Override
	public String getDisassembly() {
		return "-";
	}

	@Override
	public String getDescription() {
		return "-";
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return null;
	}

}
