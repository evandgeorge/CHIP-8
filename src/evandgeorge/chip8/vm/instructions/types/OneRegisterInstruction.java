package evandgeorge.chip8.vm.instructions.types;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

public abstract class OneRegisterInstruction implements Instruction {

	public final Unsigned8Bit register;

	public OneRegisterInstruction(Unsigned8Bit register) {
		this.register = register;
	}

}
